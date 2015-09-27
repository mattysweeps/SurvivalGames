package io.github.m0pt0pmatt.SpongeSurvivalGames;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import org.slf4j.Logger;

@Plugin(id = "spongesurvivalgames", name = "Sponge Survival Games", version = "1.0")
public class SpongeSurvivalGamesPlugin {

    // Plugin Logger
    private Logger logger;

    @Inject
    public SpongeSurvivalGamesPlugin(Logger logger) {
        this.logger = logger;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Sponge Survival Games Plugin Enabled");
    }

    public Logger getLogger() {
        return logger;
    }

}
