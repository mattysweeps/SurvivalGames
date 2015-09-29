package io.github.m0pt0pmatt.spongesurvivalgames;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.scheduler.SchedulerService;
import org.spongepowered.api.service.scheduler.TaskBuilder;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    private final SpongeSurvivalGamesPlugin plugin;
    private SurvivalGameState gameState;
    private final Set<UUID> playerSet = new HashSet<UUID>();
    private Optional<String> worldName = Optional.absent();
    private Optional<Location<World>> centerLocation = Optional.absent();
    private Set<Location<World>> spawnLocations = new HashSet<Location<World>>();
    private Optional<Location<World>> exitLocation = Optional.absent();
    private int playerLimit = 25; //Default player limit
    private Optional<World> world = Optional.absent();

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

    public void setRunning() throws NoWorldNameException, NoWorldException, NotEnoughSpawnPointsException, NoExitLocationException {

        if (!worldName.isPresent()) {
            throw new NoWorldNameException();
        }

        world = plugin.getGame().getServer().getWorld(worldName.get());
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

    private void startGame() {

        //Spawn players

        Set<UUID> missingPlayers = new HashSet<UUID>();
        Set<Location<World>> spawnLocations = new HashSet<Location<World>>();
        spawnLocations.addAll(this.spawnLocations);

        Iterator<Location<World>> spawnIterator = spawnLocations.iterator();
        for (UUID playerUUID : playerSet) {
            Optional<Player> player = plugin.getGame().getServer().getPlayer(playerUUID);
            if (!player.isPresent()) {
                missingPlayers.add(playerUUID);
            } else {
                Location<World> spawnPoint = spawnIterator.next();

                //Teleport player
                ((Entity) player).setLocation(spawnPoint);

                //Postion player to look at center
                if (centerLocation.isPresent()) {
                    ((Entity) player).setRotation(new Vector3d(0, 0, 0)); //TODO: compute real rotation
                }
            }

        }

        playerSet.removeAll(missingPlayers);

    }

    public void setStopped() {
        gameState = SurvivalGameState.STOPPED;

        // Spawn players to the exit location
        for (UUID playerUUID: playerSet){
            Optional<Player> player = plugin.getGame().getServer().getPlayer(playerUUID);
            if (player.isPresent()) {
                ((Entity) player).setLocation(exitLocation.get());
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

    public void setCenterLocation(Location<World> location) {
        centerLocation = Optional.of(location);
    }

    public void addSpawnLocation(Location<World> location) {
        spawnLocations.add(location);
    }

    public void clearSpawnLocations() {
        spawnLocations.clear();
    }

    public void setWorld(String worldName) {
        this.worldName = Optional.of(worldName);
    }

    public void setExitLocation(Location<World> exitLocation) {
        this.exitLocation = Optional.of(exitLocation);
    }

    public Optional<String> getWorldName() {
        return worldName;
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
}
