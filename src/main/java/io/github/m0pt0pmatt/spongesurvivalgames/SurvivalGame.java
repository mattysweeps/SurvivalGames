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

import io.github.m0pt0pmatt.spongesurvivalgames.backups.BackupTaker;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.LootGenerator;
import io.github.m0pt0pmatt.spongesurvivalgames.tasks.SurvivalGameTask;
import io.github.m0pt0pmatt.spongesurvivalgames.tasks.Tasks;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.*;

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
    private SurvivalGameState state = SurvivalGameState.STOPPED;
    private SurvivalGameConfig config = new SurvivalGameConfig();
    private final Set<UUID> playerUUIDs = new HashSet<>();
    private final Set<UUID> spectatorUUIDs = new HashSet<>();
    private final LootGenerator lootGenerator = new LootGenerator();
    private boolean chestsFilled = false;
    private final Scoreboard playersScoreboard;
    private BackupTaker backupTaker;

    public SurvivalGame(String id) {
        this.id = id;
        playersScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = playersScoreboard.registerNewObjective("lobby", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void ready() throws SurvivalGameException {
        if (!config.getChestMidpoint().isPresent()) throw new NoChestMidpointException();
        if (!config.getChestRange().isPresent()) throw new NoChestRangeException();
        if (!config.getPlayerLimit().isPresent()) throw new NoPlayerLimitException();
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());
        if (!config.getExitVector().isPresent()) throw new NoExitLocationException();
        if (!config.getXMin().isPresent()) throw new NoBoundsException();
        if (!config.getXMax().isPresent()) throw new NoBoundsException();
        if (!config.getZMin().isPresent()) throw new NoBoundsException();
        if (!config.getZMax().isPresent()) throw new NoBoundsException();
        if (!config.getCenterVector().isPresent()) throw new NoCenterException();
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
        backupTaker = new BackupTaker(this);
    }

    public void stop() throws SurvivalGameException {

        // Execute force stop tasks if the game is RUNNING
        if (state.equals(SurvivalGameState.RUNNING) || state.equals(SurvivalGameState.DEATHMATCH)) {
            executeTasks(forceStopTasks);
        }

        // Set the state
        state = SurvivalGameState.STOPPED;

        chestsFilled = false;

        if (backupTaker != null){
            backupTaker.cancel();
        }

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
            if (!task.execute(this)) {
                Bukkit.getLogger().warning("Error executing task: " + task.getClass().getName());
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

        Player player = Bukkit.getServer().getPlayer(playerUUID);
        if (player != null) {
            Bukkit.getScheduler().runTaskLater(
                    BukkitSurvivalGamesPlugin.plugin,
                    () -> {
                        if (player.isOnline()) player.teleport(getExitLocation().get());
                    },
                    10);
            player.getScoreboard().resetScores(player.getName());
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        if (state.equals(SurvivalGameState.RUNNING) || state.equals(SurvivalGameState.DEATHMATCH)){

            if (player != null){
                doDeathDisplay(player.getWorld(), player.getLocation());
            }

            BukkitSurvivalGamesPlugin.getPlayers(playerUUIDs).forEach(
                    p -> p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1, 0)
            );
            BukkitSurvivalGamesPlugin.getPlayers(spectatorUUIDs).forEach(
                    s -> s.playSound(s.getLocation(), Sound.AMBIENCE_THUNDER, 1, 0)
            );

            executeTasks(checkWinTask);
        }
    }

    private void doDeathDisplay(World world, Location location) {
        Firework firework = world.spawn(location, Firework.class);
        FireworkMeta fm = firework.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(false)
                .withColor(Color.RED)
                .trail(false)
                .with(Type.BALL)
                .with(Type.BALL_LARGE)
                .with(Type.STAR)
                .withFade(Color.RED)
                .build());
        fm.setPower(0);
        firework.setFireworkMeta(fm);
    }

    public void setTakeBackups(boolean backups) {
        if (backups && (state == SurvivalGameState.RUNNING || state == SurvivalGameState.DEATHMATCH)) {
            if (backupTaker == null) {
                backupTaker = new BackupTaker(this);
            }
            return;
        }

        if (!backups && backupTaker != null) {
            backupTaker.cancel();
            backupTaker = null;
        }
    }

    public String getID() {
        return id;
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
     */
    public void setState(SurvivalGameState state) {
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

    public void addPlayer(UUID player) throws NoPlayerLimitException, PlayerLimitReachedException {
        if (!config.getPlayerLimit().isPresent()) throw new NoPlayerLimitException();
        if (playerUUIDs.size() >= config.getPlayerLimit().get()) throw
                new PlayerLimitReachedException(config.getPlayerLimit().get());

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

    public Scoreboard getLobbyScoreboard() {
        return playersScoreboard;
    }

    public Optional<String> getWorldName() {
        return config.getWorldName();
    }

    public void setWorldName(String worldName) throws NoWorldException {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) throw new NoWorldException(worldName);

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
        Optional<Vector> exit = config.getExitVector();
        if (!exit.isPresent()) return Optional.empty();
        if (!config.getWorldName().isPresent()) return Optional.empty();
        World world = Bukkit.getServer().getWorld(config.getExitWorld().get());
        if (world == null) return Optional.empty();

        return Optional.of(new Location(world, exit.get().getX(), exit.get().getY(), exit.get().getZ()));
    }

    public void setExitLocation(String worldName, int x, int y, int z) throws NoWorldException {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) throw new NoWorldException(worldName);

        config.setExitVector(new Vector(x, y, z));
        config.setExitWorld(worldName);
    }

    public Optional<Location> getCenterLocation() {
        Optional<Vector> center = config.getCenterVector();
        if (!center.isPresent()) return Optional.empty();
        if (!config.getWorldName().isPresent()) return Optional.empty();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) return Optional.empty();

        return Optional.of(new Location(world, center.get().getX(), center.get().getY(), center.get().getZ()));
    }

    public void setCenterLocation(int x, int y, int z) throws NoWorldNameException, NoWorldException {
        if (!config.getWorldName().isPresent()) throw new NoWorldNameException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());

        config.setCenterVector(new Vector(x, y, z));
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

    public void setCountdownTime(int countdownTime) throws NegativeNumberException {
        if (countdownTime < 0) throw new NegativeNumberException();
        config.setCountdownTime(countdownTime);
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

    public Set<Vector> getSpawnVectors() {
        return config.getSpawns();
    }

    public void addSpawnVector(int x, int y, int z) throws WorldNotSetException, NoWorldException {
        if (!config.getWorldName().isPresent()) throw new WorldNotSetException();
        World world = Bukkit.getServer().getWorld(config.getWorldName().get());
        if (world == null) throw new NoWorldException(config.getWorldName().get());

        config.getSpawns().add(new Vector(x, y, z));
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
