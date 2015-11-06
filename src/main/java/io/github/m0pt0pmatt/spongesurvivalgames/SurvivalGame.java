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
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.LootGenerator;
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

    private static final Set<Vector> surroundingVectors = new HashSet<>(Arrays.asList(
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
    private static final List<SurvivalGameTask> startTasks = new LinkedList<>(Arrays.asList(
            new ResetLootGeneratorTask(),
            new CreateCenterChestsTask(),
            new FillChestsTask(),
            new CreateCageSnapshotsTask(),
            new SpawnPlayersTask(),
            new RotatePlayersTask(),
            new ReadyPlayerTask(),
            new CreateCountdownTask()
    ));
    private static final List<SurvivalGameTask> forceStopTasks = new LinkedList<>(Collections.singletonList(
            new DespawnPlayersTask()
    ));
    private static final List<SurvivalGameTask> stopTasks = new LinkedList<>(Collections.singletonList(
            new ClearPlayersTask()
    ));
    private final Set<UUID> playerUUIDs = new HashSet<>();
    private SurvivalGameState state = SurvivalGameState.STOPPED;
    private SurvivalGameConfig config = new SurvivalGameConfig();
    private LootGenerator lootGenerator = new LootGenerator();

    public SurvivalGameState getState() {
        return state;
    }

    public LootGenerator getLootGenerator() {
        return lootGenerator;
    }

    public void ready() {
        state = SurvivalGameState.READY;
    }

    public void start() throws WorldNotSetException, NoWorldException, NotEnoughSpawnPointsException, NoExitLocationException, TaskException, NoChestMidpointException, NoChestRangeException, NoBoundsException {

        // Check all prerequisites for starting the game
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());
        if (playerUUIDs.size() > config.getSpawns().size()) throw new NotEnoughSpawnPointsException
        	(playerUUIDs.size(), config.getSpawns().size());
        if (!config.getExit().isPresent()) throw new NoExitLocationException();
        if (!config.getChestMidpoint().isPresent()) throw new NoChestMidpointException();
        if (!config.getChestRange().isPresent()) throw new NoChestRangeException();
        if (!config.getXMin().isPresent()) throw new NoBoundsException();
        if (!config.getXMax().isPresent()) throw new NoBoundsException();
        if (!config.getYMin().isPresent()) throw new NoBoundsException();
        if (!config.getYMax().isPresent()) throw new NoBoundsException();
        if (!config.getZMin().isPresent()) throw new NoBoundsException();
        if (!config.getZMax().isPresent()) throw new NoBoundsException();

        // Set the state
        state = SurvivalGameState.RUNNING;

        //Execute each task
        executeTasks(startTasks);

        //Check if the game is over as soon as it starts
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
        if (playerUUIDs.size() >= config.getPlayerLimit().get()) throw 
        	new PlayerLimitReachedException(config.getPlayerLimit().get());

        playerUUIDs.add(player);
    }

    public void removePlayer(UUID player) {
        playerUUIDs.remove(player);
    }

    public void setCenterLocation(int x, int y, int z) throws NoWorldNameException, NoWorldException {
        if (!config.getWorldName().isPresent()) throw new NoWorldNameException(
        		"World for map empty when trying to set up center location!");
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());

        config.setCenter(new Vector(x, y, z));
    }

    public void addSpawnLocation(int x, int y, int z) throws WorldNotSetException, NoWorldException {
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());

        config.getSpawns().add(new Vector(x, y, z));
    }

    public void clearSpawnLocations() {
        config.getSpawns().clear();
    }

    public void setWorld(String worldName) throws NoWorldException {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) throw new NoWorldException(worldName);

        config.setWorldName(world.getName());
    }

    public void setExitLocation(String worldName, int x, int y, int z) throws NoWorldException {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) throw new NoWorldException(worldName);

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
        if (countdownTime < 0) throw new NegativeCountdownTimeException(countdownTime);
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
            Bukkit.getScheduler().runTaskLater(
                    BukkitSurvivalGamesPlugin.plugin,
                    () -> {
                        if (player.isOnline()) player.teleport(getExit().get());
                    },
                    10);
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
            Bukkit.getScheduler().runTaskLater(
                    BukkitSurvivalGamesPlugin.plugin,
                    () -> {
                        if (winner.isOnline()) winner.teleport(getExit().get());
                    },
                    200);
            winner.sendMessage("Congratulations! You won!");
        }

        Bukkit.getScheduler().runTaskLater(
                BukkitSurvivalGamesPlugin.plugin,
                () -> {
                    try {
                        stop();
                    } catch (TaskException e) {
                        e.printStackTrace();
                    }
                },
                200);
    }

    public void setBounds(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
        config.setXMin(Math.min(xMin, xMax));
        config.setXMax(Math.max(xMin, xMax));
        config.setYMin(Math.min(yMin, yMax));
        config.setYMax(Math.max(yMin, yMax));
        config.setZMin(Math.min(zMin, zMax));
        config.setZMax(Math.max(zMin, zMax));
    }

    public Optional<Integer> getXMin(){
        return config.getXMin();
    }

    public Optional<Integer> getXMax(){
        return config.getXMax();
    }

    public Optional<Integer> getYMin(){
        return config.getYMin();
    }

    public Optional<Integer> getYMax(){
        return config.getYMax();
    }

    public Optional<Integer> getZMin(){
        return config.getZMin();
    }

    public Optional<Integer> getZMax(){
        return config.getZMax();
    }

    public List<Loot> getLoot() {
        return config.getLoot();
    }

    public void addLoot(Loot loot) {
        if (loot != null) config.getLoot().add(loot);
    }
}
