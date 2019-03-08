/*
 * This file is part of SurvivalGames, licensed under the MIT License (MIT).
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
package io.github.m0pt0pmatt.survivalgames;

import static io.github.m0pt0pmatt.survivalgames.Util.toCommandCallable;

import com.google.inject.Inject;
import io.github.m0pt0pmatt.survivalgames.command.executor.RootCommand;
import io.github.m0pt0pmatt.survivalgames.listener.PlayerDeathListener;
import io.github.m0pt0pmatt.survivalgames.listener.PlayerOpenedChestListener;
import io.github.m0pt0pmatt.survivalgames.listener.SurvivalGameEventListener;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.SpongeExecutorService;

/** SurvivalGames Sponge Plugin */
@Plugin(
    id = "survival-games",
    name = "Survival Games",
    version = "1.0.9",
    description = "Survival Games for Sponge."
)
public class SurvivalGamesPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(SurvivalGamesPlugin.class);

    public static SpongeExecutorService EXECUTOR;
    public static PluginContainer PLUGIN_CONTAINER;
    public static SurvivalGamesPlugin PLUGIN;
    public static Path CONFIG_DIRECTORY;
    public static ExecutorService ASYNC_EXECUTOR;
    public static ExecutorService SYNC_EXECUTOR;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path sharedRootConfig;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        PLUGIN = this;

        // Register listeners.
        Sponge.getEventManager().registerListeners(this, PlayerDeathListener.getInstance());
        Sponge.getEventManager().registerListeners(this, SurvivalGameEventListener.getInstance());
        Sponge.getEventManager().registerListeners(this, PlayerOpenedChestListener.getInstance());

        // Register the root command.
        // All other commands exist under the root command.
        Sponge.getCommandManager()
                .register(
                        this,
                        toCommandCallable(RootCommand.getInstance()),
                        RootCommand.getInstance().getAliases());

        // Create an executor
        EXECUTOR = Sponge.getScheduler().createSyncExecutor(this);

        // Get the plugin container. This is useful for making Causes.
        Sponge.getPluginManager()
                .getPlugin("survival-games")
                .ifPresent(pluginContainer -> PLUGIN_CONTAINER = pluginContainer);

        // Create a config directory if one does not already exist.
        setupConfigDirectory();

        // Create an asynchronous executor for background tasks.
        ASYNC_EXECUTOR = Sponge.getScheduler().createAsyncExecutor(this);
        SYNC_EXECUTOR = Sponge.getScheduler().createSyncExecutor(this);

        LOGGER.info("Survival Games Plugin Enabled.");
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        LOGGER.info("Survival Games Plugin Disabled.");
    }

    private void setupConfigDirectory() {

        File configDirectory = new File(sharedRootConfig.toFile(), "survival-games");

        if (!configDirectory.exists()) {
            if (!configDirectory.mkdir()) {
                LOGGER.error("Could not create config directory!");
            }
        }

        CONFIG_DIRECTORY = sharedRootConfig.resolve("survival-games");
    }
}
