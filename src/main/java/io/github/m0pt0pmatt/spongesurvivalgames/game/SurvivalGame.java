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

package io.github.m0pt0pmatt.spongesurvivalgames.game;

import com.flowpowered.math.vector.Vector3i;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.exception.SurvivalGameException;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.LootGenerator;
import io.github.m0pt0pmatt.spongesurvivalgames.task.SurvivalGameTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.Tasks;
import io.github.m0pt0pmatt.spongesurvivalgames.util.Util;

import static io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin.LOGGER;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    public static final int backupTime = 60 * 5; //make a backup every 5 minutes

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

    private final String id;
    private final Set<UUID> playerUUIDs = new HashSet<>();
    private final Set<UUID> spectatorUUIDs = new HashSet<>();
    private final LootGenerator lootGenerator = new LootGenerator();
    private SurvivalGameStateManager.SurvivalGameState state = SurvivalGameStateManager.SurvivalGameState.STOPPED;
    private SurvivalGameConfig config = new SurvivalGameConfig();
    private boolean chestsFilled = false;

    public SurvivalGame(String id) {
        this.id = id;
    }

    public void ready() throws SurvivalGameException {
        if (!config.getChestMidpoint().isPresent()) throw new IllegalArgumentException();
        if (!config.getChestRange().isPresent()) throw new IllegalArgumentException();
        if (!config.getPlayerLimit().isPresent()) throw new IllegalArgumentException();
        if (!config.getWorldName().isPresent()) throw new IllegalArgumentException();
        World world = Sponge.getServer().getWorld(config.getWorldName().get()).get();
        if (world == null) throw new IllegalArgumentException(config.getWorldName().get());
        if (!config.getExitVector().isPresent()) throw new IllegalArgumentException();
        if (!config.getXMin().isPresent()) throw new IllegalArgumentException();
        if (!config.getXMax().isPresent()) throw new IllegalArgumentException();
        if (!config.getZMin().isPresent()) throw new IllegalArgumentException();
        if (!config.getZMax().isPresent()) throw new IllegalArgumentException();
        if (!config.getCenterVector().isPresent()) throw new IllegalArgumentException();
        if (!config.getCountdownTime().isPresent()) throw new IllegalArgumentException();
        if (!config.getDeathmatchRadius().isPresent()) throw new IllegalArgumentException();
        if (!config.getDeathmatchTime().isPresent()) throw new IllegalArgumentException();
        if (config.getChestLocations().isEmpty())
            throw new IllegalArgumentException();
        if (config.getPlayerLimit().get() > config.getSpawns().size()) throw new IllegalArgumentException();
        state = SurvivalGameStateManager.SurvivalGameState.READY;

        //Execute each task
        if (!executeTasks(readyTasks)) throw new IllegalArgumentException();
    }

    public void start() throws SurvivalGameException {

        // Check all prerequisites for starting the game
        if (playerUUIDs.isEmpty()) throw new IllegalArgumentException();
        World world = Sponge.getServer().getWorld(config.getWorldName().get()).get();
        if (world == null) throw new IllegalArgumentException(config.getWorldName().get());
        if (!chestsFilled) throw new IllegalArgumentException();

        // Set the state
        state = SurvivalGameStateManager.SurvivalGameState.RUNNING;

        //Execute each task
        if (!executeTasks(startTasks)) throw new IllegalArgumentException();

    }

    public void stop() throws SurvivalGameException {

        // Execute force stop tasks if the game is RUNNING
        if (state.equals(SurvivalGameStateManager.SurvivalGameState.RUNNING) || state.equals(SurvivalGameStateManager.SurvivalGameState.DEATHMATCH)) {
            executeTasks(forceStopTasks);
        }

        // Set the state
        state = SurvivalGameStateManager.SurvivalGameState.STOPPED;

        chestsFilled = false;

        if (!executeTasks(stopTasks)) throw new IllegalArgumentException();
    }

    public void startDeathMatch() {
        if (!state.equals(SurvivalGameStateManager.SurvivalGameState.DEATHMATCH)) {
            state = SurvivalGameStateManager.SurvivalGameState.DEATHMATCH;

            SpongeSurvivalGamesPlugin.executorService.schedule(
                    () -> {
                        try {
                            beginDeathMatch();
                        } catch (Exception e) {
                            LOGGER.warn(e.getMessage());
                        }
                    },
                    config.getDeathmatchTime().get(),
                    TimeUnit.SECONDS
            );

            Util.getPlayers(playerUUIDs).forEach(
                player -> player.sendMessage(Text.of("Deathmatch starting in " + config.getDeathmatchTime().get() + " seconds."))
            );
        }
    }

    private void beginDeathMatch() throws SurvivalGameException {
        if (!executeTasks(deathmatchTasks)) throw new IllegalArgumentException();
    }

    private boolean executeTasks(List<SurvivalGameTask> tasks) {
        for (SurvivalGameTask task : tasks) {
            if (!task.execute(this)) {
                LOGGER.info("Error executing task: " + task.getClass().getName());
                return false;
            }
        }
        return true;
    }

    public void reportDeath(UUID playerUUID) {

        if (!playerUUIDs.contains(playerUUID)) {
            return;
        }

        playerUUIDs.remove(playerUUID);

        Player player = Sponge.getServer().getPlayer(playerUUID).get();
        if (player != null) {
            SpongeSurvivalGamesPlugin.executorService.schedule(
                    () -> {
                        if (player.isOnline()) player.setLocation(getExitLocation().get());
                    },
                    10,
                    TimeUnit.MILLISECONDS);
        }

        if (state.equals(SurvivalGameStateManager.SurvivalGameState.RUNNING) || state.equals(SurvivalGameStateManager.SurvivalGameState.DEATHMATCH)) {

            Util.getPlayers(playerUUIDs).forEach(
                p -> p.playSound(SoundTypes.ENTITY_LIGHTNING_THUNDER, p.getLocation().getPosition(), 1)
            );
            Util.getPlayers(spectatorUUIDs).forEach(
                s -> s.playSound(SoundTypes.ENTITY_LIGHTNING_THUNDER, s.getLocation().getPosition(), 1)
            );

            executeTasks(checkWinTask);
        }
    }

    public String getID() {
        return id;
    }

    public SurvivalGameStateManager.SurvivalGameState getState() {
        return state;
    }

    /**
     * Sets the game's state.<br />
     * This method is <b>not for conventional use!</b><br />
     * To advance the game's state, us the {@link #start()}, {@link #ready()}, and {@link #stop()} commands!<br />
     * This method does nothing to make certain that the state is valid given the parameters of the game,
     * perform any of the tasks that usually come with moving through the states, or anything else.<br />
     */
    public void setState(SurvivalGameStateManager.SurvivalGameState state) {
        this.state = state;
    }

    public SurvivalGameConfig getConfig() {
        return config;
    }

    public void setConfig(SurvivalGameConfig config) {
        this.config = config;
    }

    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }

    public void addPlayer(UUID player)  {
        if (!config.getPlayerLimit().isPresent()) throw new IllegalArgumentException();
        if (playerUUIDs.size() >= config.getPlayerLimit().get()) throw
            new IllegalArgumentException();

        playerUUIDs.add(player);
    }

    public void removePlayer(UUID player) {
        playerUUIDs.remove(player);
    }

    public Set<UUID> getSpectatorUUIDs() {
        return spectatorUUIDs;
    }

    public void addSpectator(UUID player) {
        spectatorUUIDs.add(player);
    }

    public void removeSpectator(UUID player) {
        spectatorUUIDs.remove(player);
    }

    public LootGenerator getLootGenerator() {
        return lootGenerator;
    }

    public void setChestsFilled() {
        this.chestsFilled = true;
    }

    public Optional<String> getWorldName() {
        return config.getWorldName();
    }

    public void setWorldName(String worldName) {
        World world = Sponge.getServer().getWorld(worldName).get();
        if (world == null) throw new IllegalArgumentException(worldName);

        config.setWorldName(world.getName());
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

    public void setBounds(int xMin, int xMax, int zMin, int zMax) {
        config.setXMin(Math.min(xMin, xMax));
        config.setXMax(Math.max(xMin, xMax));
        config.setZMin(Math.min(zMin, zMax));
        config.setZMax(Math.max(zMin, zMax));
    }

    public Optional<Location> getExitLocation() {
        Optional<Vector3i> exit = config.getExitVector();
        if (!exit.isPresent()) return Optional.empty();
        if (!config.getWorldName().isPresent()) return Optional.empty();
        World world = Sponge.getServer().getWorld(config.getExitWorld().get()).get();
        if (world == null) return Optional.empty();

        return Optional.of(new Location<>(world, exit.get().getX(), exit.get().getY(), exit.get().getZ()));
    }

    public void setExitLocation(String worldName, int x, int y, int z) {
        World world = Sponge.getServer().getWorld(worldName).get();
        if (world == null) throw new IllegalArgumentException(worldName);

        config.setExitVector(new Vector3i(x, y, z));
        config.setExitWorld(worldName);
    }

    public Optional<Location> getCenterLocation() {
        Optional<Vector3i> center = config.getCenterVector();
        if (!center.isPresent()) return Optional.empty();
        if (!config.getWorldName().isPresent()) return Optional.empty();
        World world = Sponge.getServer().getWorld(config.getWorldName().get()).get();
        if (world == null) return Optional.empty();

        return Optional.of(new Location<>(world, center.get().getX(), center.get().getY(), center.get().getZ()));
    }

    public void setCenterLocation(int x, int y, int z) {
        if (!config.getWorldName().isPresent()) throw new IllegalArgumentException();
        World world = Sponge.getServer().getWorld(config.getWorldName().get()).get();
        if (world == null) throw new IllegalArgumentException(config.getWorldName().get());

        config.setCenterVector(new Vector3i(x, y, z));
    }

    public Optional<Integer> getPlayerLimit() {
        return config.getPlayerLimit();
    }

    public void setPlayerLimit(Integer playerLimit) {
        config.setPlayerLimit(playerLimit);
    }

    public Optional<Integer> getCountdownTime() {
        return config.getCountdownTime();
    }

    public void setCountdownTime(int countdownTime) {
        if (countdownTime < 0) throw new IllegalArgumentException();
        config.setCountdownTime(countdownTime);
    }

    public Optional<Double> getChestMidpoint() {
        return config.getChestMidpoint();
    }

    public void setChestMidpoint(Double chestMidpoint) {
        if (chestMidpoint < 0) throw new IllegalArgumentException();
        config.setChestMidpoint(chestMidpoint);
    }

    public Optional<Double> getChestRange() {
        return config.getChestRange();
    }

    public void setChestRange(Double chestRange) {
        if (chestRange < 0) throw new IllegalArgumentException();
        config.setChestRange(chestRange);
    }

    public Optional<Integer> getDeathmatchRadius() {
        return config.getDeathmatchRadius();
    }

    public void setDeathmatchRadius(int deathmatchRadius) {
        if (deathmatchRadius < 0) throw new IllegalArgumentException();
        config.setDeathmatchRadius(deathmatchRadius);
    }

    public Optional<Integer> getDeathmatchTime() {
        return config.getDeathmatchTime();
    }

    public void setDeathmatchTime(int deathmatchTime) {
        if (deathmatchTime < 0) throw new IllegalArgumentException();
        config.setDeathmatchTime(deathmatchTime);
    }

    public Set<Vector3i> getSpawnVectors() {
        return config.getSpawns();
    }

    public void addSpawnVector(int x, int y, int z) {
        if (!config.getWorldName().isPresent()) throw new IllegalArgumentException("World Not Set");
        World world = Sponge.getServer().getWorld(config.getWorldName().get()).get();
        if (world == null) throw new IllegalArgumentException(config.getWorldName().get());

        config.getSpawns().add(new Vector3i(x, y, z));
    }

    public void clearSpawnVectors() {
        config.getSpawns().clear();
    }

    public Set<Loot> getLoot() {
        return config.getLoot();
    }

    public void addLoot(Loot loot) {
        if (loot != null) config.getLoot().add(loot);
    }
}
