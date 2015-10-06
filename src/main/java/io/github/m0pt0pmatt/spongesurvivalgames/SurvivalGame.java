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
import io.github.m0pt0pmatt.spongesurvivalgames.tasks.MessagePlayerTask;
import org.spongepowered.api.block.*;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
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
    private SurvivalGameState state;
    private Optional<UUID> worldUUID = Optional.empty();
    private Set<Location<World>> spawns = new HashSet<>();
    private Optional<Location<World>> exit = Optional.empty();
    private Optional<Location<World>> center = Optional.empty();
    private final Set<UUID> playerSet = new HashSet<>();
    private int playerLimit = 25; //Default player limit
    private int countdownTime = 10; //Default countdown time

    public SurvivalGame(SpongeSurvivalGamesPlugin plugin) {
        this.plugin = plugin;
        state = SurvivalGameState.STOPPED;
    }

    public SurvivalGameState getState() {
        return state;
    }

    public void setReady() {
        state = SurvivalGameState.READY;
    }

    public void setRunning() throws WorldNotSetException, NoWorldException, NotEnoughSpawnPointsException, NoExitLocationException {

        if (!worldUUID.isPresent()) {
            throw new WorldNotSetException();
        }

        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) {
            throw new NoWorldException();
        }

        if (playerSet.size() > spawns.size()) {
            throw new NotEnoughSpawnPointsException();
        }

        if (!exit.isPresent()) {
            throw new NoExitLocationException();
        }

        startGame();

        state = SurvivalGameState.RUNNING;
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
        spawnLocations.addAll(this.spawns);

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
            if (center.isPresent()) {
                player.setRotation(player.getRotation().add(center.get().getPosition())); //TODO: compute real rotation
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

            location.add(1, 0, 0).setBlockType(BlockTypes.BARRIER);
            location.add(1, 1, 0).setBlockType(BlockTypes.BARRIER);
            location.add(-1, 0, 0).setBlockType(BlockTypes.BARRIER);
            location.add(-1, 1, 0).setBlockType(BlockTypes.BARRIER);
            location.add(0, 0, 1).setBlockType(BlockTypes.BARRIER);
            location.add(0, 1, 1).setBlockType(BlockTypes.BARRIER);
            location.add(0, 0, -1).setBlockType(BlockTypes.BARRIER);
            location.add(0, 1, -1).setBlockType(BlockTypes.BARRIER);
            location.add(0, 2, 0).setBlockType(BlockTypes.BARRIER);
            location.add(0, -1, 0).setBlockType(BlockTypes.BARRIER);

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

        for (int i = countdownTime; i > 0; i--){
            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(countdownTime - i, TimeUnit.SECONDS)
                    .execute(new MessagePlayerTask(Integer.toString(i), players))
                    .submit(plugin);
        }

        plugin.getGame().getScheduler().createTaskBuilder()
                .delay(countdownTime, TimeUnit.SECONDS)
                .execute(new MessagePlayerTask("Go!", players))
                .submit(plugin);

    }

    public void setStopped() {
        state = SurvivalGameState.STOPPED;

        // Spawn players to the exit location
        for (UUID playerUUID : playerSet) {
            Optional<Player> player = plugin.getGame().getServer().getPlayer(playerUUID);
            if (player.isPresent() && exit.isPresent()) {
                player.get().setLocation(exit.get());
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

        center = Optional.of(new Location<>(world.get(), x, y, z));
    }

    public void addSpawnLocation(int x, int y, int z) throws WorldNotSetException, NoWorldException {

        if (!worldUUID.isPresent()) {
            throw new WorldNotSetException();
        }

        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) {
            throw new NoWorldException();
        }

        spawns.add(new Location<>(world.get(), x, y, z));
    }

    public void clearSpawnLocations() {
        spawns.clear();
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

        this.exit = Optional.of(new Location<>(world.get(), x, y, z));
    }

    public Optional<Location<World>> getCenter() {
        return center;
    }

    public Set<Location<World>> getSpawns() {
        return spawns;
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

    public void setCountdownTime(int countdownTime) throws NegativeCountdownTimeException {

        if (countdownTime < 0){
            throw new NegativeCountdownTimeException();
        }

        this.countdownTime = countdownTime;
    }

    public Optional<Location<World>> getExit() {
        return exit;
    }

    public int getCountdownTime() {
        return countdownTime;
    }
}
