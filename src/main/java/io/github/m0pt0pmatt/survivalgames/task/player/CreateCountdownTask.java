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

import com.google.common.collect.ImmutableMap;
import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.event.*;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameRunningState;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.TextMessageException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;

/** Create countdown titles for the start of the game . */
public class CreateCountdownTask extends PlayerAndSpectatorTask {

    private static final Map<SurvivalGameRunningState, Function<SurvivalGame, PostCountdownEvent>> postEvents
            = ImmutableMap.<SurvivalGameRunningState, Function<SurvivalGame, PostCountdownEvent>>builder()
            .put(SurvivalGameRunningState.STOPPED, GameStartedPostCountdownEvent::new)
            .put(SurvivalGameRunningState.IN_PROGRESS, DeathmatchPostCountdownEvent::new)
            .build();

    private static final Map<SurvivalGameRunningState, Function<SurvivalGame, PreCountdownEvent>> preEvents
            = ImmutableMap.<SurvivalGameRunningState, Function<SurvivalGame, PreCountdownEvent>>builder()
            .put(SurvivalGameRunningState.STOPPED, GameStartedPreCountdownEvent::new)
            .put(SurvivalGameRunningState.IN_PROGRESS, DeathmatchPreCountdownEvent::new)
            .build();

    private static final Map<SurvivalGameRunningState, Text> text = ImmutableMap.<SurvivalGameRunningState, Text>builder()
            .put(SurvivalGameRunningState.STOPPED, Text.of("Game"))
            .put(SurvivalGameRunningState.IN_PROGRESS, Text.of("Deathmatch"))
            .build();


    private CreateCountdownTask() {
    }

    @Override
    public void execute(SurvivalGame survivalGame, Player player) throws CommandException {
        int countDown =
                getOrThrow(
                        survivalGame.getConfig().getCountdownSeconds(),
                        CommandKeys.COUNT_DOWN_SECONDS);

        List<Title> titles = new ArrayList<>();

        Text title = Optional.ofNullable(text.get(survivalGame.getRunningState())).orElse(Text.of("Game"));

        for (int i = 0; i < countDown + 1; i++) {
            titles.add(
                    Title.builder()
                            .fadeIn(5)
                            .stay(20)
                            .fadeOut(5)
                            .title(Text.of(TextColors.RED, title, " begins in..."))
                            .subtitle(Text.of(TextColors.RED, countDown - i))
                            .build());
        }

        for (int i = 0; i < countDown + 1; i++) {
            final int j = i;
            SurvivalGamesPlugin.SYNC_EXECUTOR.schedule(
                    () -> player.sendTitle(titles.get(j)), i, TimeUnit.SECONDS);
        }

        Optional.ofNullable(preEvents.get(survivalGame.getRunningState()))
                .map(f -> f.apply(survivalGame))
                .ifPresent(e -> Sponge.getEventManager().post(e));

        Optional.ofNullable(postEvents.get(survivalGame.getRunningState()))
                .map(f -> f.apply(survivalGame))
                .ifPresent(e ->
                        SurvivalGamesPlugin.SYNC_EXECUTOR.schedule(
                                () -> Sponge.getEventManager().post(e),
                                countDown,
                                TimeUnit.SECONDS));
    }

    public static CreateCountdownTask getInstance() {
        return new CreateCountdownTask();
    }
}
