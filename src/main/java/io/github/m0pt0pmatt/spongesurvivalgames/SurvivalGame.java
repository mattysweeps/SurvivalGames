/*
 * This file is part of SpongeSurvivalGamesPlugin, licensed under the MIT License (MIT).
 *
 * Copyright (c) Matthew Broomfield <m0pt0pmatt17@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.m0pt0pmatt.spongesurvivalgames;

import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import io.github.m0pt0pmatt.spongesurvivalgames.tasks.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    //TODO make these fields STATIC (surroundingVectors, etc) to save some space

    private class exitTask implements Runnable {

        private Player player;

        public exitTask(Player player) {
            this.player = player;
        }

        public void run() {
            if (!player.isOnline()) {
                return;
            }

            player.teleport(getExit().get());
        }
    }

    private class stopTask implements Runnable {

        private SurvivalGame game;

        public stopTask(SurvivalGame game) {
            this.game = game;
        }

        public void run() {
            try {
                game.stop();
            } catch (TaskException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private final Set<UUID> playerUUIDs = new HashSet<>();
    private final Set<Vector> surroundingVectors = new HashSet<>(Arrays.asList(
            new Vector(1, 0, 0),
            new Vector(1, 1, 0),
            new Vector(-1, 0, 0),
            new Vector(-1, 1, 0),
            new Vector(0, 0, 1),
            new Vector(0, 1, 1),
            new Vector(0, 0, -1),
            new Vector(0, 1, -1),
            new Vector(0, 2, 0),
            new Vector(0, -1, 0)
    ));
    private final List<SurvivalGameTask> startTasks = new LinkedList<>(Arrays.asList(
            new SpawnPlayersTask(),
            new RotatePlayersTask(),
            new SetGameModeTask(),
            new CreateCageSnapshotsTask(),
            new CreateCenterChestsTask(),
            new FillChestsTask(),
            new CreateCountdownTask()
    ));
    private final List<SurvivalGameTask> forceStopTasks = new LinkedList<>(Arrays.asList(
            new DespawnPlayersTask()
    ));
    private final List<SurvivalGameTask> stopTasks = new LinkedList<>(Collections.singletonList(
            new ClearPlayersTask()
    ));

    private SurvivalGameState state = SurvivalGameState.STOPPED;
    private SurvivalGameConfig config = new SurvivalGameConfig();

    public SurvivalGameState getState() {
        return state;
    }

    public void ready() {
        state = SurvivalGameState.READY;
    }

    public void start() throws WorldNotSetException, NoWorldException, NotEnoughSpawnPointsException, NoExitLocationException, TaskException, NoChestMidpointException, NoChestRangeException {

        // Check all prerequisites for starting the game
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException();
        if (playerUUIDs.size() > config.getSpawns().size()) throw new NotEnoughSpawnPointsException();
        if (!config.getExit().isPresent()) throw new NoExitLocationException();
        if (!config.getChestMidpoint().isPresent()) throw new NoChestMidpointException();
        if (!config.getChestRange().isPresent()) throw new NoChestRangeException();

        // Set the state
        state = SurvivalGameState.RUNNING;

        //Execute each task
        executeTasks(startTasks);

        checkWin();
    }

    public void stop() throws TaskException {

        // Execute force stop tasks if the game is RUNNING
        if (state.equals(SurvivalGameState.RUNNING)) {
            executeTasks(forceStopTasks);
        }

        // Execute the rest of the tasks
        executeTasks(stopTasks);

        // Set the state
        state = SurvivalGameState.STOPPED;
    }

    private void executeTasks(List<SurvivalGameTask> tasks) throws TaskException {
        //Execute each task
        Optional<TaskException> exception = tasks.stream()
                .map(task -> {
                    try {
                        task.execute(this);
                        return null;
                    } catch (TaskException e) {
                        return e;
                    }
                })
                .filter(e -> e != null)
                .findFirst();

        //Throw the first caught exception
        if (exception.isPresent()) throw exception.get();
    }

    public void addPlayer(UUID player) throws NoPlayerLimitException, PlayerLimitReachedException {

        if (!config.getPlayerLimit().isPresent()) throw new NoPlayerLimitException();
        if (playerUUIDs.size() >= config.getPlayerLimit().get()) throw new PlayerLimitReachedException();

        playerUUIDs.add(player);
    }

    public void removePlayer(UUID player) {
        playerUUIDs.remove(player);
    }

    public void setCenterLocation(int x, int y, int z) throws NoWorldNameException, NoWorldException {
        if (!config.getWorldName().isPresent()) throw new NoWorldNameException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException();

        config.setCenter(new Vector(x, y, z));
    }

    public void addSpawnLocation(int x, int y, int z) throws WorldNotSetException, NoWorldException {
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException();

        config.getSpawns().add(new Vector(x, y, z));
    }

    public void clearSpawnLocations() {
        config.getSpawns().clear();
    }

    public void setWorld(String worldName) throws NoWorldException {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) throw new NoWorldException();

        config.setWorldName(world.getName());
    }

    public void setExitLocation(String worldName, int x, int y, int z) throws NoWorldException {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) throw new NoWorldException();

        config.setExit(new Vector(x, y, z));
        config.setExitWorld(worldName);
    }

    public Optional<Location> getCenter() {

        Optional<Vector> center = config.getCenter();
        if (!center.isPresent()) return Optional.empty();
        if (!config.getWorldName().isPresent()) return Optional.empty();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) return Optional.empty();

        return Optional.of(new Location(world, center.get().getX(), center.get().getY(), center.get().getZ()));
    }

    public Set<Vector> getSpawns() {
        return config.getSpawns();
    }

    public Optional<Integer> getPlayerLimit() {
        return config.getPlayerLimit();
    }

    public void setPlayerLimit(Integer playerLimit) {
        config.setPlayerLimit(playerLimit);
    }

    public Optional<String> getWorldName() {
        return config.getWorldName();
    }

    public Optional<Location> getExit() {

        Optional<Vector> exit = config.getExit();
        if (!exit.isPresent()) return Optional.empty();
        if (!config.getWorldName().isPresent()) return Optional.empty();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) return Optional.empty();

        return Optional.of(new Location(world, exit.get().getX(), exit.get().getY(), exit.get().getZ()));
    }

    public Optional<Integer> getCountdownTime() {
        return config.getCountdownTime();
    }

    public void setCountdownTime(int countdownTime) throws NegativeCountdownTimeException {
        if (countdownTime < 0) throw new NegativeCountdownTimeException();
        config.setCountdownTime(countdownTime);
    }

    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }

    public Set<Vector> getSurroundingVectors() {
        return surroundingVectors;
    }

    public SurvivalGameConfig getConfig() {
        return config;
    }

    public void setConfig(SurvivalGameConfig config) {
        this.config = config;
    }

    public Optional<Double> getChestMidpoint() {
        return config.getChestMidpoint();
    }

    public void setChestMidpoint(Double chestMidpoint) {
        config.setChestMidpoint(chestMidpoint);
    }

    public Optional<Double> getChestRange() {
        return config.getChestRange();
    }

    public void setChestRange(Double chestRange) {
        config.setChestRange(chestRange);
    }

    public void reportDeath(UUID playerUUID) {
        if (!playerUUIDs.contains(playerUUID)) {
            //unregistered player
            return;
        }

        playerUUIDs.remove(playerUUID);

        Player player = Bukkit.getServer().getPlayer(playerUUID);
        if (player != null) {
            Bukkit.getScheduler().runTaskLater(BukkitSurvivalGamesPlugin.plugin,
                    new exitTask(player), 1);
        }

        checkWin();
    }

    private void checkWin() {

        if (playerUUIDs.size() > 1) {
            return;
        }

        UUID winnerUUID = playerUUIDs.stream().findFirst().get();
        Player winner = Bukkit.getServer().getPlayer(winnerUUID);
        if (winner != null) {
            Bukkit.getScheduler().runTaskLater(BukkitSurvivalGamesPlugin.plugin,
                    new exitTask(winner), 200);
            winner.sendMessage("Congratulations! You won!");
        }

        Bukkit.getScheduler().runTaskLater(BukkitSurvivalGamesPlugin.plugin,
                new stopTask(this), 200);

    }
}
