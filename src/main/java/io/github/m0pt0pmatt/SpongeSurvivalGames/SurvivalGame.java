package io.github.m0pt0pmatt.SpongeSurvivalGames;

import com.google.common.base.Optional;
import org.spongepowered.api.entity.living.player.Player;
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
    private Optional<World> world = Optional.absent();
    private Optional<Location<World>> centerLocation = Optional.absent();
    private Set<Location<World>> spawnLocations = new HashSet<Location<World>>();

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

    public void setRunning() {
        gameState = SurvivalGameState.RUNNING;
    }

    public void setStopped() {
        gameState = SurvivalGameState.STOPPED;
        playerSet.clear();
    }

    public void addPlayer(UUID player){
        playerSet.add(player);
    }

    public void removePlayer(UUID player){
        playerSet.remove(player);
    }

    public void setCenterLocation(Location<World> location){
        centerLocation = Optional.of(location);
    }

    public void addSpawnLocation(Location<World> location){
        spawnLocations.add(location);
    }

    public void clearSpawnLocations(){
        spawnLocations.clear();
    }

}
