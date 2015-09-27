package io.github.m0pt0pmatt.SpongeSurvivalGames;

import com.google.common.base.Optional;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.LinkedList;
import java.util.List;

/**
 * represents a Survival Game.
 */
public class SurvivalGame {

    private final SpongeSurvivalGamesPlugin plugin;
    private SurvivalGameState gameState;
    private final List<Player> playerList = new LinkedList<Player>();
    private Optional<World> worldOptional = Optional.absent();

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
    }
}
