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

import java.util.Optional;

import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import io.github.m0pt0pmatt.spongesurvivalgames.tasks.CountdownTask;
import org.spongepowered.api.block.*;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    private final SpongeSurvivalGamesPlugin plugin;
    private SurvivalGameState gameState;
    private Optional<UUID> worldUUID = Optional.empty();
    private Set<Location<World>> spawnLocations = new HashSet<>();
    private Optional<Location<World>> exitLocation = Optional.empty();
    private Optional<Location<World>> centerLocation = Optional.empty();
    private final Set<UUID> playerSet = new HashSet<>();
    private int playerLimit = 25; //Default player limit

    public SurvivalGame(SpongeSurvivalGamesPlugin plugin) {
        this.plugin = plugin;
        gameState = SurvivalGameState.STOPPED;
    }

    public SurvivalGameState getGameState() {
        return gameState;
    }

    public void setReady() {
        gameState = SurvivalGameState.READY;
    }

    public void setRunning() throws WorldNotSetException, NoWorldException, NotEnoughSpawnPointsException, NoExitLocationException {

        if (!worldUUID.isPresent()) {
            throw new WorldNotSetException();
        }

        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) {
            throw new NoWorldException();
        }

        if (playerSet.size() > spawnLocations.size()) {
            throw new NotEnoughSpawnPointsException();
        }

        if (!exitLocation.isPresent()) {
            throw new NoExitLocationException();
        }

        startGame();

        gameState = SurvivalGameState.RUNNING;
    }

    private void startGame() throws NoWorldException {

        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) {
            throw new NoWorldException();
        }

        //Spawn players

        Set<UUID> missingPlayers = new HashSet<>();
        Set<Player> players = new HashSet<>();
        Set<Location<World>> spawnLocations = new HashSet<>();
        spawnLocations.addAll(this.spawnLocations);

        Iterator<Location<World>> spawnIterator = spawnLocations.iterator();
        for (UUID playerUUID : playerSet) {
            Optional<Player> player = plugin.getGame().getServer().getPlayer(playerUUID);
            if (!player.isPresent()) {
                missingPlayers.add(playerUUID);
            } else {
                players.add(player.get());
            }

        }

        playerSet.removeAll(missingPlayers);

        for (Player player: players) {

            Location<World> spawnPoint = spawnIterator.next();

            //Teleport player
            player.setLocation(spawnPoint.add(0.5, 0, 0.5));

            //Postion player to look at center
            if (centerLocation.isPresent()) {
                player.setRotation(player.getRotation().add(centerLocation.get().getPosition())); //TODO: compute real rotation
            }

            player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);


        }

        for (Location<World> location: spawnLocations){
            final Set<BlockSnapshot> snapshots = new HashSet<>();
            snapshots.add(location.add(1, 0, 0).createSnapshot());
            snapshots.add(location.add(1, 1, 0).createSnapshot());
            snapshots.add(location.add(-1, 0, 0).createSnapshot());
            snapshots.add(location.add(-1, 1, 0).createSnapshot());
            snapshots.add(location.add(0, 0, 1).createSnapshot());
            snapshots.add(location.add(0, 1, 1).createSnapshot());
            snapshots.add(location.add(0, 0, -1).createSnapshot());
            snapshots.add(location.add(0, 1, -1).createSnapshot());
            snapshots.add(location.add(0, 2, 0).createSnapshot());
            snapshots.add(location.add(0, -1, 0).createSnapshot());

            location.add(1, 0, 0).setBlockType(BlockTypes.GLASS);
            location.add(1, 1, 0).setBlockType(BlockTypes.GLASS);
            location.add(-1, 0, 0).setBlockType(BlockTypes.GLASS);
            location.add(-1, 1, 0).setBlockType(BlockTypes.GLASS);
            location.add(0, 0, 1).setBlockType(BlockTypes.GLASS);
            location.add(0, 1, 1).setBlockType(BlockTypes.GLASS);
            location.add(0, 0, -1).setBlockType(BlockTypes.GLASS);
            location.add(0, 1, -1).setBlockType(BlockTypes.GLASS);
            location.add(0, 2, 0).setBlockType(BlockTypes.GLASS);
            location.add(0, -1, 0).setBlockType(BlockTypes.GLASS);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(0, TimeUnit.SECONDS)
                    .execute(new CountdownTask(10, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(1, TimeUnit.SECONDS)
                    .execute(new CountdownTask(9, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(2, TimeUnit.SECONDS)
                    .execute(new CountdownTask(8, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(3, TimeUnit.SECONDS)
                    .execute(new CountdownTask(7, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(4, TimeUnit.SECONDS)
                    .execute(new CountdownTask(6, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(5, TimeUnit.SECONDS)
                    .execute(new CountdownTask(5, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(6, TimeUnit.SECONDS)
                    .execute(new CountdownTask(4, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(7, TimeUnit.SECONDS)
                    .execute(new CountdownTask(3, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(8, TimeUnit.SECONDS)
                    .execute(new CountdownTask(2, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(9, TimeUnit.SECONDS)
                    .execute(new CountdownTask(1, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(10, TimeUnit.SECONDS)
                    .execute(new CountdownTask(0, players))
                    .submit(plugin);

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(10, TimeUnit.SECONDS)
                    .execute(new Runnable() {
                        @Override
                        public void run() {
                            for (BlockSnapshot snapshot : snapshots) snapshot.restore(true, false);
                        }
                    })
                    .submit(plugin);
        }

    }

    public void setStopped() {
        gameState = SurvivalGameState.STOPPED;

        // Spawn players to the exit location
        for (UUID playerUUID : playerSet) {
            Optional<Player> player = plugin.getGame().getServer().getPlayer(playerUUID);
            if (player.isPresent() && exitLocation.isPresent()) {
                player.get().setLocation(exitLocation.get());
            }
        }

        playerSet.clear();
    }

    public void addPlayer(UUID player) throws PlayerLimitReachedException {
        if (playerSet.size() >= playerLimit) {
            throw new PlayerLimitReachedException();
        }
        playerSet.add(player);
    }

    public void removePlayer(UUID player) {
        playerSet.remove(player);
    }

    public void setCenterLocation(int x, int y, int z) throws WorldNotSetException, NoWorldException {

        if (!worldUUID.isPresent()) {
            throw new WorldNotSetException();
        }

        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) {
            throw new NoWorldException();
        }

        centerLocation = Optional.of(new Location<>(world.get(), x, y, z));
    }

    public void addSpawnLocation(int x, int y, int z) throws WorldNotSetException, NoWorldException {

        if (!worldUUID.isPresent()) {
            throw new WorldNotSetException();
        }

        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) {
            throw new NoWorldException();
        }

        spawnLocations.add(new Location<>(world.get(), x, y, z));
    }

    public void clearSpawnLocations() {
        spawnLocations.clear();
    }

    public void setWorld(String worldName) throws NoWorldException {

        Optional<World> world = plugin.getGame().getServer().getWorld(worldName);
        if (!world.isPresent()) {
            throw new NoWorldException();
        }

        worldUUID = Optional.of(world.get().getUniqueId());
    }

    public void setExitLocation(String worldName, int x, int y, int z) throws NoWorldException {
        Optional<World> world = plugin.getGame().getServer().getWorld(worldName);
        if (!world.isPresent()) {
            throw new NoWorldException();
        }

        this.exitLocation = Optional.of(new Location<>(world.get(), x, y, z));
    }

    public Optional<Location<World>> getCenterLocation() {
        return centerLocation;
    }

    public Set<Location<World>> getSpawnLocations() {
        return spawnLocations;
    }

    public void setPlayerLimit(Integer playerLimit) {
        this.playerLimit = playerLimit;
    }

    public int getPlayerLimit() {
        return playerLimit;
    }

    public Optional<UUID> getWorldUUID() {
        return worldUUID;
    }
}
