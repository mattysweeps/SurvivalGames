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

package io.github.m0pt0pmatt.spongesurvivalgames.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Utility class used to display titles.
 *
 * @author Skyler
 */
public final class Title {

    private static final int defaultTime = 10;

    private static final String playerToken = "[PLAYER]";

    private static final String titleToken = "[TITLE]";

    private static final String subtitleToken = "[SUBTITLE]";

    private static final String titleColorToken = "[TITLECOLOR]";

    private static final String subtitleColorToken = "[SUBTITLECOLOR]";

    private static final String timeInTickToken = "[TIMEIN]";

    private static final String timeOutTickToken = "[TIMEOUT]";

    private static final String[] titleCommands = new String[]{
            "title " + playerToken + " times " + timeInTickToken + " 0 " + timeOutTickToken,
            "title " + playerToken + " subtitle {text:\"" + subtitleToken + "\",color:\"" + subtitleColorToken + "\"}",
            "title " + playerToken + " title {text:\"" + titleToken + "\",color:\"" + titleColorToken + "\"}"
    };

    public static void displayTitle(Player player, String titleText, ChatColor titleColor) {
        displayTitle(player, titleText, "", titleColor, ChatColor.MAGIC, defaultTime, defaultTime);
    }


    public static void displayTitle(Player player, String titleText, String subtitleText, ChatColor titleColor,
                                    ChatColor subtitleColor, int timeIn, int timeOut) {
        for (String command : titleCommands) {

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    command.replace(playerToken, player.getName())
                            .replace(titleToken, titleText)
                            .replace(subtitleToken, subtitleText)
                            .replace(titleColorToken, titleColor.name().toLowerCase())
                            .replace(subtitleColorToken, subtitleColor.name().toLowerCase())
                            .replace(timeInTickToken, timeIn + "")
                            .replace(timeOutTickToken, timeOut + "")
            );

        }
    }

}
