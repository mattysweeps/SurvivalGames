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
package io.github.m0pt0pmatt.spongesurvivalgames.listener;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.event.SurvivalGameEvent;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.event.Listener;

public class SurvivalGameEventListener {

    private static SurvivalGameEventListener INSTANCE = new SurvivalGameEventListener();

    private SurvivalGameEventListener() {

    }

    @Listener
    public void fireCommandBlocks(SurvivalGameEvent event) {
        String eventName = event.getClass().getSimpleName();
        SpongeSurvivalGamesPlugin.LOGGER.info(eventName);

        for(CommandBlock commandBlock: event.getSurvivalGame().getCommandBlocks()) {

            Value<String> storedCommand = commandBlock.storedCommand();
            if (storedCommand.exists()) {
                if (storedCommand.get().startsWith("/ssg commandblock listen ")) {

                    String commandBlockCommandName = storedCommand.get().substring("/ssg commandblock listen ".length() - 1);
                    SpongeSurvivalGamesPlugin.LOGGER.info(commandBlockCommandName);

                    if (eventName.equalsIgnoreCase(commandBlockCommandName)) {
                        commandBlock.execute();
                    }


                }
            }


        }



    }

    public static SurvivalGameEventListener getInstance() {
        return INSTANCE;
    }
}
