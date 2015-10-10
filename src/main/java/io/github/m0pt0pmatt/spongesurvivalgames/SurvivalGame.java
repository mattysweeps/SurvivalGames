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

import java.util.*;

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    private final SpongeSurvivalGamesPlugin plugin;
    private SurvivalGameState state;
    private Optional<UUID> worldUUID = Optional.empty();
    private final Set<Location<World>> spawns = new HashSet<>();
    private Optional<Location<World>> exit = Optional.empty();
    private Optional<Location<World>> center = Optional.empty();
    private final Set<UUID> playerSet = new HashSet<>();
    private int playerLimit = 25; //Default player limit
    private int countdownTime = 10; //Default countdown time
    private final Set<Vector3d> surroundingVectors = new HashSet<>(Arrays.asList(
            new Vector3d(1, 0, 0),
            new Vector3d(1, 1, 0),
            new Vector3d(-1, 0, 0),
            new Vector3d(-1, 1, 0),
            new Vector3d(0, 0, 1),
            new Vector3d(0, 1, 1),
            new Vector3d(0, 0, -1),
            new Vector3d(0, 1, -1),
            new Vector3d(0, 2, 0),
            new Vector3d(0, -1, 0)
    ));

    public SurvivalGame(SpongeSurvivalGamesPlugin plugin) {
        this.plugin = plugin;
        state = SurvivalGameState.STOPPED;
    }

    public SurvivalGameState getState() {
        return state;
    }

    public void ready() {
        state = SurvivalGameState.READY;
    }

    public void start() throws WorldNotSetException, NoWorldException, NotEnoughSpawnPointsException, NoExitLocationException {

        // Check all prerequisites for starting the game
        if (!worldUUID.isPresent()) throw new WorldNotSetException();
        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) throw new NoWorldException();
        if (playerSet.size() > spawns.size()) throw new NotEnoughSpawnPointsException();
        if (!exit.isPresent()) throw new NoExitLocationException();

        // Set the state
        state = SurvivalGameState.RUNNING;

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
            player.setLocation(spawnPoint.add(0.5, 0, 0.5));

            //Postion player to look at center
            if (center.isPresent()) {
                player.setRotation(player.getRotation().add(center.get().getPosition())); //TODO: compute real rotation
            }

            player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
        }

        for (Location<World> location: spawnLocations){
            final Set<BlockSnapshot> snapshots = surroundingVectors.stream().map(vector -> location.add(vector).createSnapshot()).collect(Collectors.toSet());
            surroundingVectors.stream().forEach(vector -> location.add(vector).setBlockType(BlockTypes.BARRIER));

            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(countdownTime, TimeUnit.SECONDS)
                    .execute(() -> snapshots.stream().forEach(snapshot -> snapshot.restore(true, false)))
                    .submit(plugin);
        }

        for (int i = countdownTime; i > 0; i--){
            final int j = i;
            plugin.getGame().getScheduler().createTaskBuilder()
                    .delay(countdownTime - i, TimeUnit.SECONDS)
                    .execute(() -> players.stream().forEach(player -> player.sendMessage(Texts.of(j))))
                    .submit(plugin);
        }

        plugin.getGame().getScheduler().createTaskBuilder()
                .delay(countdownTime, TimeUnit.SECONDS)
                .execute(() -> players.stream().forEach(player -> player.sendMessage(Texts.of("Go!"))))
                .submit(plugin);

    }

    public void stop() {
        if (state.equals(SurvivalGameState.RUNNING)){
            // Spawn players to the exit location
            for (UUID playerUUID : playerSet) {
                Optional<Player> player = plugin.getGame().getServer().getPlayer(playerUUID);
                if (player.isPresent()){
                    if (exit.isPresent()) {
                        player.get().setLocation(exit.get());
                    }
                }
            }
        }

        state = SurvivalGameState.STOPPED;

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
        if (!worldUUID.isPresent()) throw new WorldNotSetException();
        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) throw new NoWorldException();

        center = Optional.of(new Location<>(world.get(), x, y, z));
    }

    public void addSpawnLocation(int x, int y, int z) throws WorldNotSetException, NoWorldException {
        if (!worldUUID.isPresent()) throw new WorldNotSetException();
        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) throw new NoWorldException();

        spawns.add(new Location<>(world.get(), x, y, z));
    }

    public void clearSpawnLocations() {
        spawns.clear();
    }

    public void setWorld(String worldName) throws NoWorldException {
        Optional<World> world = plugin.getGame().getServer().getWorld(worldName);
        if (!world.isPresent()) throw new NoWorldException();

        worldUUID = Optional.of(world.get().getUniqueId());
    }

    public void setExitLocation(String worldName, int x, int y, int z) throws NoWorldException {
        Optional<World> world = plugin.getGame().getServer().getWorld(worldName);
        if (!world.isPresent()) throw new NoWorldException();

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
        if (countdownTime < 0) throw new NegativeCountdownTimeException();

        this.countdownTime = countdownTime;
    }

    public Optional<Location<World>> getExit() {
        return exit;
    }

    public int getCountdownTime() {
        return countdownTime;
    }
}
