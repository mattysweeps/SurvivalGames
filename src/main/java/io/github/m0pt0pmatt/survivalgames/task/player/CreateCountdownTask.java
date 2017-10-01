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

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.event.PostCountdownEvent;
import io.github.m0pt0pmatt.survivalgames.event.PreCountdownEvent;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.TextMessageException;

/** Create countdown titles for the start of the game . */
public class CreateCountdownTask extends PlayerTask {

    private static final PlayerTask INSTANCE = new CreateCountdownTask();

    @Override
    public void execute(SurvivalGame survivalGame, Player player) throws TextMessageException {
        int countDown =
                getOrThrow(
                        survivalGame.getConfig().getCountdownSeconds(),
                        CommandKeys.COUNT_DOWN_SECONDS);

        List<Title> titles = new ArrayList<>();

        for (int i = 0; i < countDown + 1; i++) {
            titles.add(
                    Title.builder()
                            .fadeIn(5)
                            .stay(20)
                            .fadeOut(5)
                            .title(Text.of(TextColors.RED, "Game begins in..."))
                            .subtitle(Text.of(TextColors.RED, countDown - i))
                            .build());
        }

        for (int i = 0; i < countDown + 1; i++) {
            final int j = i;
            SurvivalGamesPlugin.EXECUTOR.schedule(
                    () -> player.sendTitle(titles.get(j)), i, TimeUnit.SECONDS);
        }

        Sponge.getEventManager().post(new PreCountdownEvent(survivalGame));

        SurvivalGamesPlugin.EXECUTOR.schedule(
                () -> Sponge.getEventManager().post(new PostCountdownEvent(survivalGame)),
                countDown,
                TimeUnit.SECONDS);
    }

    public static PlayerTask getInstance() {
        return INSTANCE;
    }
}
