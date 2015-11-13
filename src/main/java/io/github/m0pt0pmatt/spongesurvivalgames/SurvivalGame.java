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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.github.m0pt0pmatt.spongesurvivalgames.tasks.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import io.github.m0pt0pmatt.spongesurvivalgames.backups.BackupTaker;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.BeginDeathmatchException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.ChestsNotFinishedException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NegativeNumberException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoBoundsException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoCenterException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoChestMidpointException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoChestRangeException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoChestsException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoCountdownException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoDeathmatchRadiusException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoDeathmatchTimeException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoExitLocationException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoPlayerException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoPlayerLimitException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldNameException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NotEnoughSpawnPointsException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.PlayerLimitReachedException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.ReadyException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.StartException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.StopException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.SurvivalGameException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.WorldNotSetException;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.LootGenerator;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {
	
	private static final int backupTime = 60 * 5; //make a backup every 5 minutes

    private static final List<SurvivalGameTask> startTasks = Arrays.asList(
            Tasks.CREATE_CAGE,
            Tasks.SPAWN_PLAYERS,
            Tasks.ROTATE_PLAYERS,
            Tasks.READY_PLAYERS,
            Tasks.SPAWN_SPECTATORS,
            Tasks.READY_SPECTATORS,
            Tasks.CREATE_COUNTDOWN,
            Tasks.CREATE_SCOREBOARD,
            Tasks.CREATE_WORLD_BORDER
    );
    private static final List<SurvivalGameTask> readyTasks = Arrays.asList(
            Tasks.RESET_LOOT_GENERATOR,
            Tasks.FILL_CHESTS
    );
    private static final List<SurvivalGameTask> forceStopTasks = Arrays.asList(
            Tasks.DESPAWN_PLAYERS,
            Tasks.CLEAR_SCOREBOARD,
            Tasks.CLEAR_WORLD_BORDER
    );
    private static final List<SurvivalGameTask> stopTasks = Collections.singletonList(
            Tasks.CLEAR_PLAYERS
    );
    private static final List<SurvivalGameTask> deathmatchTasks = Arrays.asList(
            Tasks.CREATE_CAGE,
            Tasks.SPAWN_PLAYERS,
            Tasks.ROTATE_PLAYERS,
            Tasks.CREATE_COUNTDOWN,
            Tasks.CREATE_DEATHMATCH_BORDER
    );
    private static final List<SurvivalGameTask> checkWinTask = Collections.singletonList(
            Tasks.CHECK_WIN
    );
    private final Set<UUID> playerUUIDs = new HashSet<>();
    private final Set<UUID> spectatorUUIDs = new HashSet<>();
    private SurvivalGameState state = SurvivalGameState.STOPPED;
    private SurvivalGameConfig config = new SurvivalGameConfig();
    private final LootGenerator lootGenerator = new LootGenerator();
    private final Scoreboard playersScoreboard;
    private boolean chestsFilled = false;
    private BackupTaker backupTaker;
    private final String id;

    public SurvivalGame(String id) {
    	this.id = id;
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
    
    /**
     * Sets the game's state.<br />
     * This method is <b>not for conventional use!</b><br />
     * To advance the game's state, us the {@link #start()}, {@link #ready()}, and {@link #stop()} commands!<br />
     * This method does nothing to make certain that the state is valid given the parameters of the game,
     * perform any of the tasks that usually come with moving through the states, or anything else.<br />
     * It instead is for internal use by {@link io.github.m0pt0pmatt.spongesurvivalgames.backups.Backup Backups}
     * only!
     * @param state
     */
    public void setState(SurvivalGameState state) {
    	this.state = state;
    }

    public LootGenerator getLootGenerator() {
        return lootGenerator;
    }
    
    public String getID() {
    	return id;
    }

    public void ready() throws SurvivalGameException {
        if (!config.getChestMidpoint().isPresent()) throw new NoChestMidpointException();
        if (!config.getChestRange().isPresent()) throw new NoChestRangeException();
        if (!config.getPlayerLimit().isPresent()) throw new NoPlayerLimitException();
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());
        if (!config.getExit().isPresent()) throw new NoExitLocationException();
        if (!config.getXMin().isPresent()) throw new NoBoundsException();
        if (!config.getXMax().isPresent()) throw new NoBoundsException();
        if (!config.getZMin().isPresent()) throw new NoBoundsException();
        if (!config.getZMax().isPresent()) throw new NoBoundsException();
        if (!config.getCenter().isPresent()) throw new NoCenterException();
        if (!config.getCountdownTime().isPresent()) throw new NoCountdownException();
        if (!config.getDeathmatchRadius().isPresent()) throw new NoDeathmatchRadiusException();
        if (!config.getDeathmatchTime().isPresent()) throw new NoDeathmatchTimeException();
        if (config.getChestLocations().isEmpty())
            throw new NoChestsException();
        if (config.getPlayerLimit().get() > config.getSpawns().size()) throw new NotEnoughSpawnPointsException
                (config.getSpawns().size(), config.getPlayerLimit().get());
        state = SurvivalGameState.READY;

        //Execute each task
        if (!executeTasks(readyTasks)) throw new ReadyException();
    }

    public void start() throws SurvivalGameException {

        // Check all prerequisites for starting the game
        if (playerUUIDs.isEmpty()) throw new NoPlayerException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());
        if (!chestsFilled) throw new ChestsNotFinishedException();

        // Set the state
        state = SurvivalGameState.RUNNING;

        //Execute each task
        if (!executeTasks(startTasks)) throw new StartException();
        
        //Start backups
        backupTaker = new BackupTaker(this, SurvivalGame.backupTime);
    }

    public void stop() throws SurvivalGameException{

        // Execute force stop tasks if the game is RUNNING
        if (state.equals(SurvivalGameState.RUNNING) || state.equals(SurvivalGameState.DEATHMATCH)) {
            executeTasks(forceStopTasks);
        }

        // Set the state
        state = SurvivalGameState.STOPPED;

        chestsFilled = false;
        
        backupTaker.cancel();

        if (!executeTasks(stopTasks)) throw new StopException();
    }

    public void startDeathMatch() {
        if (!state.equals(SurvivalGameState.DEATHMATCH)) {
            state = SurvivalGameState.DEATHMATCH;

            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    BukkitSurvivalGamesPlugin.plugin,
                    () -> {
                        try {
                            beginDeathMatch();
                        } catch (SurvivalGameException e) {
                            Bukkit.getLogger().warning(e.getDescription());
                        }
                    },
                    20L * config.getDeathmatchTime().get()
            );

            BukkitSurvivalGamesPlugin.getPlayers(playerUUIDs).forEach(
                    player -> player.sendMessage("Deathmatch starting in " + config.getDeathmatchTime().get() + " seconds.")
            );
        }
    }

    private void beginDeathMatch() throws SurvivalGameException {
        if (!executeTasks(deathmatchTasks)) throw new BeginDeathmatchException();
    }

    private boolean executeTasks(List<SurvivalGameTask> tasks) {
        for (SurvivalGameTask task : tasks) {
            if (!task.execute(this)){
                Bukkit.getLogger().warning("Error executing task: " + task.getClass().getName());
                return false;
            }
        }
        return true;
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
            player.getScoreboard().resetScores(player.getName());
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        for (Player p : BukkitSurvivalGamesPlugin.getPlayers(playerUUIDs)) {
            p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1, 0);
        }
        BukkitSurvivalGamesPlugin.getPlayers(spectatorUUIDs).forEach(
                s -> s.playSound(s.getLocation(), Sound.AMBIENCE_THUNDER, 1, 0)
        );

        executeTasks(checkWinTask);
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
    
    public void setTakeBackups(boolean backups) {
    	if (backups && (state == SurvivalGameState.RUNNING || state == SurvivalGameState.DEATHMATCH)) {
	    	if (backupTaker == null) {
	    		backupTaker = new BackupTaker(this, SurvivalGame.backupTime);
	    	}
	    	return;
    	}
    	
    	if (!backups && backupTaker != null) {
    		backupTaker.cancel();
    		backupTaker = null;
    	}
    }
}
