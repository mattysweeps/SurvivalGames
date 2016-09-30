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
package io.github.m0pt0pmatt.survivalgames.command.executor.print;

import io.github.m0pt0pmatt.survivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.survivalgames.command.executor.SurvivalGamesCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

class PrintPlayersCommand extends AbstractPrintCommand {

    private static final SurvivalGamesCommand INSTANCE = new PrintPlayersCommand();

    private PrintPlayersCommand() {
        super(
                "players",
                SurvivalGameCommandElement.getInstance(),
                Collections.emptyMap(),
                survivalGame -> Optional.of(Text.joinWith(Text.of('\n'), survivalGame.getPlayerUUIDs().stream()
                        .map(id -> Sponge.getServer().getPlayer(id))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(Text::of).collect(Collectors.toList())))
        );
    }

    static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
