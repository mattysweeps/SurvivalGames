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
package io.github.m0pt0pmatt.spongesurvivalgames.command.executor.print;

import org.spongepowered.api.text.Text;

import java.util.Collections;

import io.github.m0pt0pmatt.spongesurvivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.SurvivalGamesCommand;

class PrintBoundariesCommand extends AbstractPrintCommand {

    private static final SurvivalGamesCommand INSTANCE = new PrintBoundariesCommand();

    private PrintBoundariesCommand() {
        super(
                Collections.singletonList("boundaries"),
                "",
                SurvivalGameCommandElement.getInstance(),
                Collections.emptyMap(),
                survivalGame -> survivalGame.getConfig().getLesserBoundary().map(Text::of).orElse(Text.of("(No first boundary)"))
                        .concat(Text.of(' ').concat(survivalGame.getConfig().getGreaterBoundary().map(Text::of).orElse(Text.of("(No second boundary)"))))
        );
    }

    static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
