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

import com.google.common.collect.Sets;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.LootGenerator;
import io.github.m0pt0pmatt.spongesurvivalgames.tasks.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    private static final List<SurvivalGameTask> startTasks = Arrays.asList(
            new CreateCageSnapshotsTask(),
            new SpawnPlayersTask(),
            new RotatePlayersTask(),
            new ReadyPlayerTask(),
            new SpawnSpectatorsTask(),
            new ReadySpectatorsTask(),
            new CreateCountdownTask(),
            new CreateScoreboardTask(),
            new CreateWorldBorderTask()
    );
    private static final List<SurvivalGameTask> readyTasks = Arrays.asList(
            new ResetLootGeneratorTask(),
            new FillChestsTask()
    );
    private static final List<SurvivalGameTask> forceStopTasks = Arrays.asList(
            new DespawnPlayersTask(),
            new DeleteScoreboardTask(),
            new ClearWorldBorderTask()
    );
    private static final List<SurvivalGameTask> stopTasks = Collections.singletonList(
            new ClearPlayersTask()
    );
    private static final List<SurvivalGameTask> deathmatchTasks = Arrays.asList(
            new CreateCageSnapshotsTask(),
            new SpawnPlayersTask(),
            new RotatePlayersTask(),
            new CreateCountdownTask(),
            new CreateDeathmatchBorderTask()
    );
    public static final List<SurvivalGameTask> checkWinTask = Collections.singletonList(
            new CheckWinTask()
    );
    private final Set<UUID> playerUUIDs = new HashSet<>();
    private final Set<UUID> spectatorUUIDs = new HashSet<>();
    private SurvivalGameState state = SurvivalGameState.STOPPED;
    private SurvivalGameConfig config = new SurvivalGameConfig();
    private final LootGenerator lootGenerator = new LootGenerator();
    private final Scoreboard playersScoreboard;
    private boolean chestsFilled = false;

    public SurvivalGame() {
        playersScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = playersScoreboard.registerNewObjective("lobby", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Scoreboard getLobbyScoreboard() {
        return playersScoreboard;
    }

    public SurvivalGameState getState() {
        return state;
    }

    public LootGenerator getLootGenerator() {
        return lootGenerator;
    }

    public void ready() throws SurvivalGameException {

        if (config.getChestLocations().isEmpty())
            throw new NoChestsException();

        state = SurvivalGameState.READY;

        //Execute each task
        executeTasks(readyTasks);
    }

    public void start() throws SurvivalGameException {

        // Check all prerequisites for starting the game
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());
        if (playerUUIDs.size() > config.getSpawns().size()) throw new NotEnoughSpawnPointsException
                (playerUUIDs.size(), config.getSpawns().size());
        if (playerUUIDs.isEmpty()) throw new NoPlayerException();
        if (!config.getExit().isPresent()) throw new NoExitLocationException();
        if (!config.getChestMidpoint().isPresent()) throw new NoChestMidpointException();
        if (!config.getChestRange().isPresent()) throw new NoChestRangeException();
        if (!config.getXMin().isPresent()) throw new NoBoundsException();
        if (!config.getXMax().isPresent()) throw new NoBoundsException();
        if (!config.getZMin().isPresent()) throw new NoBoundsException();
        if (!config.getZMax().isPresent()) throw new NoBoundsException();
        if (!config.getCenter().isPresent()) throw new NoCenterException();
        if (!config.getCountdownTime().isPresent()) throw new NoCountdownException();
        if (!config.getDeathmatchRadius().isPresent()) throw new NoDeathmatchRadiusException();
        if (!config.getDeathmatchTime().isPresent()) throw new NoDeathmatchTimeException();
        if (!chestsFilled) throw new ChestsNotFinishedException();

        // Set the state
        state = SurvivalGameState.RUNNING;

        //Execute each task
        executeTasks(startTasks);
    }

    public void stop() throws SurvivalGameException {

        // Execute force stop tasks if the game is RUNNING
        if (state.equals(SurvivalGameState.RUNNING) || state.equals(SurvivalGameState.DEATHMATCH)) {
            executeTasks(forceStopTasks);
        }

        // Execute the rest of the tasks
        executeTasks(stopTasks);

        // Set the state
        state = SurvivalGameState.STOPPED;

        chestsFilled = false;
    }

    public void startDeathMatch() {
        if (!state.equals(SurvivalGameState.DEATHMATCH)) {
            state = SurvivalGameState.DEATHMATCH;

            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    BukkitSurvivalGamesPlugin.plugin,
                    this::beginDeathMatch,
                    20L * config.getDeathmatchTime().get()
            );

            BukkitSurvivalGamesPlugin.getPlayers(playerUUIDs).forEach(
                    player -> player.sendMessage("Deathmatch starting in " + config.getDeathmatchTime().get() + " seconds.")
            );
        }
    }

    private void beginDeathMatch() {
        try {
            executeTasks(deathmatchTasks);
        } catch (SurvivalGameException e) {
            Bukkit.getLogger().warning("Deathmatch task failed: " + e.getDescription());
        }
    }

    private void executeTasks(List<SurvivalGameTask> tasks) throws SurvivalGameException {
        for (SurvivalGameTask task: tasks){
            task.execute(this);
        }
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
        if (!config.getWorldName().isPresent()) throw new NoWorldNameException();
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

    public void setCountdownTime(int countdownTime) throws NegativeNumberException {
        if (countdownTime < 0) throw new NegativeNumberException();
        config.setCountdownTime(countdownTime);
    }

    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
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

    public void setChestMidpoint(Double chestMidpoint) throws NegativeNumberException {
        if (chestMidpoint < 0) throw new NegativeNumberException();
        config.setChestMidpoint(chestMidpoint);
    }

    public Optional<Double> getChestRange() {
        return config.getChestRange();
    }

    public void setChestRange(Double chestRange) throws NegativeNumberException {
        if (chestRange < 0) throw new NegativeNumberException();
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

        for (Player p : BukkitSurvivalGamesPlugin.getPlayers(playerUUIDs)) {
            p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1, 0);
        }
        BukkitSurvivalGamesPlugin.getPlayers(spectatorUUIDs).forEach(
                s -> s.playSound(s.getLocation(), Sound.AMBIENCE_THUNDER, 1, 0)
        );

        try {
            executeTasks(checkWinTask);
        } catch (SurvivalGameException e) {
            e.printStackTrace();
        }
    }

    public void setBounds(int xMin, int xMax, int zMin, int zMax) {
        config.setXMin(Math.min(xMin, xMax));
        config.setXMax(Math.max(xMin, xMax));
        config.setZMin(Math.min(zMin, zMax));
        config.setZMax(Math.max(zMin, zMax));
    }

    public Optional<Integer> getXMin() {
        return config.getXMin();
    }

    public Optional<Integer> getXMax() {
        return config.getXMax();
    }

    public Optional<Integer> getZMin() {
        return config.getZMin();
    }

    public Optional<Integer> getZMax() {
        return config.getZMax();
    }

    public List<Loot> getLoot() {
        return config.getLoot();
    }

    public void addLoot(Loot loot) {
        if (loot != null) config.getLoot().add(loot);
    }

    public Optional<Integer> getDeathmatchRadius() {
        return config.getDeathmatchRadius();
    }

    public void setDeathmatchRadius(int deathmatchRadius) throws NegativeNumberException {
        if (deathmatchRadius < 0) throw new NegativeNumberException();
        config.setDeathmatchRadius(deathmatchRadius);
    }

    public Optional<Integer> getDeathmatchTime() {
        return config.getDeathmatchTime();
    }

    public void setDeathmatchTime(int deathmatchTime) throws NegativeNumberException {
        if (deathmatchTime < 0) throw new NegativeNumberException();
        config.setDeathmatchTime(deathmatchTime);
    }

    public void setChestsFilled() {
        this.chestsFilled = true;
    }

    public void addSpectator(UUID player) {
        spectatorUUIDs.add(player);
    }

    public void removeSpectator(UUID player) {
        spectatorUUIDs.remove(player);
    }

    public Set<UUID> getSpectatorUUIDs() {
        return spectatorUUIDs;
    }
}
