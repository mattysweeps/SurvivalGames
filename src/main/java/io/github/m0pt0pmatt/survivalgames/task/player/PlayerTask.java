/*
 * This file is part of SurvivalGamesPlugin, licensed under the MIT License (MIT).
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
package io.github.m0pt0pmatt.survivalgames.task.player;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.task.Task;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.TextMessageException;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A Task which does work per player.
 * General work can be done before and after the player specific work by overriding the
 * {@link PlayerTask#before(SurvivalGame)} and {@link PlayerTask#after(SurvivalGame)} (SurvivalGame)}
 *  methods.
 */
public abstract class PlayerTask implements Task {

    @Override
    public final void execute(SurvivalGame survivalGame) throws TextMessageException {
        execute(survivalGame, getPlayerIds(survivalGame));
    }

    public final void execute(SurvivalGame survivalGame, Collection<UUID> playerIds) throws TextMessageException {
        executePlayers(survivalGame, playerIds.stream()
                .map(Sponge.getServer()::getPlayer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
    }

    private void executePlayers(SurvivalGame survivalGame, Collection<Player> players) throws TextMessageException {
        before(survivalGame);
        for (Player player : players) {
            execute(survivalGame, player);
        }
        after(survivalGame);
    }

    public abstract void execute(SurvivalGame survivalGame, Player player) throws TextMessageException;

    protected void before(SurvivalGame survivalGame) throws TextMessageException {

    }

    protected Collection<UUID> getPlayerIds(SurvivalGame survivalGame) {
        return survivalGame.getPlayerUUIDs();
    }

    protected void after(SurvivalGame survivalGame) throws TextMessageException {

    }
}
