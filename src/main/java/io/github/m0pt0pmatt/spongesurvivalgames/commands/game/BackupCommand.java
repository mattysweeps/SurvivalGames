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

package io.github.m0pt0pmatt.spongesurvivalgames.commands.game;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.backups.Backup;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandArgs;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Map;

/**
 * Command to backup to a file
 */
public class BackupCommand extends GameCommand {

    @Override
    public boolean execute(CommandSender sender, Map<CommandArgs, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        if (!arguments.containsKey(CommandArgs.FILENAME)) {
            sender.sendMessage("You must define a filename!");
            return false;
        }

        String fileName = arguments.get(CommandArgs.FILENAME);
        File backupsFolder = new File(BukkitSurvivalGamesPlugin.plugin.getDataFolder(), "Backups");

        if (!backupsFolder.isDirectory()) {
            sender.sendMessage("Found file named 'Backup', but need name "
                    + "for backup folders!\nFile will be deleted!");
            if (!backupsFolder.delete()) {
                sender.sendMessage("Unable to delete backup file!");
                return false;
            }
        }

        if (!backupsFolder.exists()) {
            if (!backupsFolder.mkdirs()) {
                BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Unable to make backups directory!");
                return false;
            }
        }

        File file = new File(backupsFolder, fileName);

        if (game.getState() != SurvivalGameState.RUNNING && game.getState() != SurvivalGameState.DEATHMATCH) {
            sender.sendMessage("You can only backup a game while it's running!");
            return false;
        }

        if (!arguments.containsKey(CommandArgs.FILENAME)) {
            sender.sendMessage("No file name given.");
            return false;
        }

        Backup backup = new Backup(game);

        backup.save(file);

        sender.sendMessage("Backup file now saving in the background!");
        return true;
    }
}
