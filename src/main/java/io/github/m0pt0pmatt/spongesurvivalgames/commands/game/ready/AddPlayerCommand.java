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

package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandArgs;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoPlayerLimitException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.PlayerLimitReachedException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

/**
 * Command to add a player to a game
 */
public class AddPlayerCommand extends ReadyCommand {

    private static final String allKeyword = "@a";

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean execute(CommandSender sender, Map<CommandArgs, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        if (!arguments.containsKey(CommandArgs.PLAYERNAME)) {
            sender.sendMessage("Player name is not present.");
            return false;
        }
        String playerName = arguments.get(CommandArgs.PLAYERNAME);

        if (playerName.equals(allKeyword)) {
            addAllPlayers(sender);
            return true;
        } else {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                sender.sendMessage("No such player \"" + playerName + "\".");
                return false;
            }
            return addPlayer(sender, player);
        }
    }

    private boolean addPlayer(CommandSender commandSender, Player player) {

        try {
            game.addPlayer(player.getUniqueId());
        } catch (NoPlayerLimitException e) {
            commandSender.sendMessage("No player limit sey for game \"" + game.getID() + "\".");
            return false;
        } catch (PlayerLimitReachedException e) {
            commandSender.sendMessage("Player limit reached for game \"" + game.getID() + "\".");
            return false;
        }

        Scoreboard board = game.getLobbyScoreboard();
        player.setScoreboard(board);
        board.getObjective(DisplaySlot.SIDEBAR).getScore(player.getName()).setScore(0);

        commandSender.sendMessage("Player \"" + player.getName() + "\" added to survival game \"" + game.getID() + "\".");
        return true;
    }

    private void addAllPlayers(CommandSender commandSender) {
        Bukkit.getOnlinePlayers().forEach(player -> addPlayer(commandSender, player));
    }

}
