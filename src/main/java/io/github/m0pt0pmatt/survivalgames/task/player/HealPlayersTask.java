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
package io.github.m0pt0pmatt.survivalgames.task.player;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.TextMessageException;

/** Restore health, hunger, and exhaustion for players. */
public class HealPlayersTask extends PlayerTask {

    private static final PlayerTask INSTANCE = new HealPlayersTask();

    @Override
    public void execute(SurvivalGame survivalGame, Player player) throws TextMessageException {
        survivalGame
                .getPlayerUUIDs()
                .forEach(
                        playerId ->
                                Sponge.getServer()
                                        .getPlayer(playerId)
                                        .ifPresent(HealPlayersTask::healPlayer));
    }

    private static void healPlayer(Player player) {
        player.offer(Keys.HEALTH, 20.0);
        player.offer(Keys.EXHAUSTION, 0.0);
        player.offer(Keys.FOOD_LEVEL, 20);
        player.offer(Keys.SATURATION, 20.0);
    }

    public static PlayerTask getInstance() {
        return INSTANCE;
    }
}
