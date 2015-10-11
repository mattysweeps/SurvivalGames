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

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import io.github.m0pt0pmatt.spongesurvivalgames.tasks.*;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    private final Set<UUID> playerUUIDs = new HashSet<>();
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
        Optional<World> world = SpongeSurvivalGamesPlugin.game.getServer().getWorld(config.getWorldName().get());
        if (!world.isPresent()) throw new NoWorldException();
        if (playerUUIDs.size() > config.getSpawns().size()) throw new NotEnoughSpawnPointsException();
        if (!config.getExit().isPresent()) throw new NoExitLocationException();
        if (!config.getChestMidpoint().isPresent()) throw new NoChestMidpointException();
        if (!config.getChestRange().isPresent()) throw new NoChestRangeException();

        // Set the state
        state = SurvivalGameState.RUNNING;

        //Execute each task
        executeTasks(startTasks);
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
        Optional<World> world = SpongeSurvivalGamesPlugin.game.getServer().getWorld(config.getWorldName().get());
        if (!world.isPresent()) throw new NoWorldException();

        config.setCenter(new Vector3d(x, y, z));
    }

    public void addSpawnLocation(int x, int y, int z) throws WorldNotSetException, NoWorldException {
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        Optional<World> world = SpongeSurvivalGamesPlugin.game.getServer().getWorld(config.getWorldName().get());
        if (!world.isPresent()) throw new NoWorldException();

        config.getSpawns().add(new Vector3d(x, y, z));
    }

    public void clearSpawnLocations() {
        config.getSpawns().clear();
    }

    public void setWorld(String worldName) throws NoWorldException {
        Optional<World> world = SpongeSurvivalGamesPlugin.game.getServer().getWorld(worldName);
        if (!world.isPresent()) throw new NoWorldException();

        config.setWorldName(world.get().getName());
    }

    public void setExitLocation(String worldName, int x, int y, int z) throws NoWorldException {
        Optional<World> world = SpongeSurvivalGamesPlugin.game.getServer().getWorld(worldName);
        if (!world.isPresent()) throw new NoWorldException();

        config.setExit(new Vector3d(x, y, z));
        config.setExitWorld(worldName);
    }

    public Optional<Location<World>> getCenter() {

        Optional<Vector3d> center = config.getCenter();
        if (!center.isPresent()) return Optional.empty();
        if (!config.getWorldName().isPresent()) return Optional.empty();
        Optional<World> world = SpongeSurvivalGamesPlugin.game.getServer().getWorld(config.getWorldName().get());
        if (!world.isPresent()) return Optional.empty();

        return Optional.of(new Location<>(world.get(), center.get()));
    }

    public Set<Vector3d> getSpawns() {
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

    public Optional<Location<World>> getExit() {

        Optional<Vector3d> exit = config.getExit();
        if (!exit.isPresent()) return Optional.empty();
        if (!config.getWorldName().isPresent()) return Optional.empty();
        Optional<World> world = SpongeSurvivalGamesPlugin.game.getServer().getWorld(config.getWorldName().get());
        if (!world.isPresent()) return Optional.empty();

        return Optional.of(new Location<>(world.get(), exit.get()));
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

    public Set<Vector3d> getSurroundingVectors() {
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
}
