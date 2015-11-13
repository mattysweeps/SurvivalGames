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

package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandArgs;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.Sponsor;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.Sponsors;
import io.github.m0pt0pmatt.spongesurvivalgames.util.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class SponsorCommand extends RunningCommand {

    @Override
    public boolean execute(CommandSender sender, Map<CommandArgs, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        if (!arguments.containsKey(CommandArgs.SPONSOR)) {
            sender.sendMessage("No Sponsor was given");
            return false;
        }
        String sponsorName = arguments.get(CommandArgs.SPONSOR);

        Optional<Sponsor> sponsor = Sponsors.get(sponsorName);
        if (!sponsor.isPresent()) {
            sender.sendMessage("The sponsor \"" + sponsorName + "\" does not exist");
            return false;
        }

        if (!arguments.containsKey(CommandArgs.PLAYERNAME)) {
            sender.sendMessage("No Playername was given");
            return false;
        }
        String playerName = arguments.get(CommandArgs.PLAYERNAME);

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("Player \"" + playerName + "\" is not a valgame.getID() player");
            return false;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                BukkitSurvivalGamesPlugin.plugin,
                () -> sponsor.get().execute(player),
                20L * 5
        );

        Title.displayTitle(player, "You have been sponsored", "This is either good or bad", ChatColor.BLUE, ChatColor.BLUE, 20, 20);
        sender.sendMessage("Player " + playerName + " has been sponsored.");
        return true;
    }
}
