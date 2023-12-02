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

package io.github.m0pt0pmatt.survivalgames.task;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.TextMessageException;

/** Updates a player's scoreboard with the current players and their kill counts. */
public class UpdateScoreBoardTask implements Task {

    private static final UpdateScoreBoardTask INSTANCE = new UpdateScoreBoardTask();

    private Player deadPlayer;
    private Player killingPlayer;

    public void update(SurvivalGame survivalGame, Player deadPlayer, Player killingPlayer)
            throws TextMessageException {
        this.deadPlayer = deadPlayer;
        this.killingPlayer = killingPlayer;
        execute(survivalGame);
    }

    @Override
    public void execute(SurvivalGame survivalGame) throws CommandException {
        deadPlayer
                .getScoreboard()
                .getObjective(DisplaySlots.SIDEBAR)
                .ifPresent(
                        objective -> {
                            objective.removeScore(Text.of(deadPlayer.getName()));
                            if (killingPlayer != null) {
                                Score k =
                                        objective.getOrCreateScore(
                                                Text.of(killingPlayer.getName()));
                                k.setScore(k.getScore() + 1);
                            }
                        });
    }

    public static UpdateScoreBoardTask getInstance() {
        return INSTANCE;
    }
}
