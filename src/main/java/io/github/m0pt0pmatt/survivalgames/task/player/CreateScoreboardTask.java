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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.TextMessageException;

/** Creates a player's scoreboard with the current players and their kill counts. */
public class CreateScoreboardTask extends PlayerTask {

    private static final PlayerTask INSTANCE = new CreateScoreboardTask();

    private Scoreboard scoreboard;
    private Objective objective;

    @Override
    public void execute(SurvivalGame survivalGame, Player player) throws TextMessageException {
        objective.getOrCreateScore(Text.of(player.getName())).setScore(0);
    }

    @Override
    public void before(SurvivalGame survivalGame) {
        scoreboard = Scoreboard.builder().build();

        objective =
                Objective.builder()
                        .criterion(Criteria.DUMMY)
                        .name("ssg-" + survivalGame.getName())
                        .displayName(Text.of("Remaining Players"))
                        .build();

        scoreboard.addObjective(objective);
        scoreboard.updateDisplaySlot(objective, DisplaySlots.SIDEBAR);
    }

    @Override
    public void after(SurvivalGame survivalGame) {
        survivalGame
                .getPlayerUUIDs()
                .forEach(
                        id ->
                                Sponge.getServer()
                                        .getPlayer(id)
                                        .ifPresent(player -> player.setScoreboard(scoreboard)));
    }

    public static PlayerTask getInstance() {
        return INSTANCE;
    }
}
