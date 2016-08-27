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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.SpongeExecutorService;

import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandRepository;
import io.github.m0pt0pmatt.spongesurvivalgames.event.PlayerEventListener;

/**
 * SpongeSurvivalGames Sponge Plugin
 */
@Plugin(id = "sponge-survival-games", name = "Sponge Survival Games", version = "0.1", description = "Survival Games for Sponge.")
public class SpongeSurvivalGamesPlugin {

    public static SpongeSurvivalGamesPlugin plugin;
    public static SpongeExecutorService executorService;

    public static Logger LOGGER = LoggerFactory.getLogger(SpongeSurvivalGamesPlugin.class);

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        SpongeSurvivalGamesPlugin.plugin = this;
        executorService = Sponge.getScheduler().createSyncExecutor(this);
        Sponge.getEventManager().registerListeners(this, PlayerEventListener.getInstance());
        CommandRepository.initializeCommands(this);
        LOGGER.info("Sponge Survival Games Plugin Enabled.");
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        LOGGER.info("Sponge Survival Games Plugin Disabled.");
    }
}

