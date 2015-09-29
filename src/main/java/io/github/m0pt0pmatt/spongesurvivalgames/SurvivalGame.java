package io.github.m0pt0pmatt.spongesurvivalgames;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoExitLocationException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldNameException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NotEnoughSpawnPointsException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashSet;
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

        if (!worldName.isPresent()){
            throw new NoWorldNameException();
        }

        Optional<World> world = plugin.getGame().getServer().getWorld(worldName.get());
        if (!world.isPresent()){
            throw new NoWorldException();
        }

        if (playerSet.size() > spawnLocations.size()){
            throw new NotEnoughSpawnPointsException();
        }

        if (!exitLocation.isPresent()){
            throw new NoExitLocationException();
        }

        //Spawn players, facing them towards center if it exists, holding them in place till match starts

        gameState = SurvivalGameState.RUNNING;
    }

    public void setStopped() {
        gameState = SurvivalGameState.STOPPED;
        playerSet.clear();
    }

    public void addPlayer(UUID player) {
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

    public void setExitLocation(Location<World> exitLocation){
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
}
