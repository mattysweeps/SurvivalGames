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
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import io.github.m0pt0pmatt.spongesurvivalgames.tasks.*;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    private final SpongeSurvivalGamesPlugin plugin;
    private final Set<Location<World>> spawns = new HashSet<>();
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
            new CreateCountdownTask()
    ));
    private final List<SurvivalGameTask> forceStopTasks = new LinkedList<>(Collections.singletonList(
            new DespawnPlayersTask()
    ));
    private final List<SurvivalGameTask> stopTasks = new LinkedList<>(Collections.singletonList(
            new ClearPlayersTask()
    ));


    private SurvivalGameState state;
    private Optional<UUID> worldUUID = Optional.empty();
    private Optional<Location<World>> exit = Optional.empty();
    private Optional<Location<World>> center = Optional.empty();
    private int playerLimit = 25; //Default player limit
    private int countdownTime = 10; //Default countdown time

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

    public void start() throws WorldNotSetException, NoWorldException, NotEnoughSpawnPointsException, NoExitLocationException, TaskException {

        // Check all prerequisites for starting the game
        if (!worldUUID.isPresent()) throw new WorldNotSetException();
        Optional<World> world = plugin.getGame().getServer().getWorld(worldUUID.get());
        if (!world.isPresent()) throw new NoWorldException();
        if (playerUUIDs.size() > spawns.size()) throw new NotEnoughSpawnPointsException();
        if (!exit.isPresent()) throw new NoExitLocationException();

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

    public void addPlayer(UUID player) throws PlayerLimitReachedException {
        if (playerUUIDs.size() >= playerLimit) {
            throw new PlayerLimitReachedException();
        }
        playerUUIDs.add(player);
    }

    public void removePlayer(UUID player) {
        playerUUIDs.remove(player);
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

    public int getPlayerLimit() {
        return playerLimit;
    }

    public void setPlayerLimit(Integer playerLimit) {
        this.playerLimit = playerLimit;
    }

    public Optional<UUID> getWorldUUID() {
        return worldUUID;
    }

    public Optional<Location<World>> getExit() {
        return exit;
    }

    public int getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(int countdownTime) throws NegativeCountdownTimeException {
        if (countdownTime < 0) throw new NegativeCountdownTimeException();

        this.countdownTime = countdownTime;
    }

    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }

    public Set<Vector3d> getSurroundingVectors() {
        return surroundingVectors;
    }

    public SpongeSurvivalGamesPlugin getPlugin() {
        return plugin;
    }
}
