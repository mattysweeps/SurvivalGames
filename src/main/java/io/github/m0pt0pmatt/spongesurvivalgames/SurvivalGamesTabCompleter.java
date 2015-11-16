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

package io.github.m0pt0pmatt.spongesurvivalgames;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandArgs;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.SurvivalGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.Sponsors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Tab completer for the plugin
 */
class SurvivalGamesTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        List<String> args = new ArrayList<>(strings.length);
        Collections.addAll(args, strings);
        args = args.parallelStream().map(String::toLowerCase).collect(Collectors.toList());

        List<String> matches = new LinkedList<>();

        SurvivalGamesCommand c = BukkitSurvivalGamesPlugin.commandTrie.partialMatch(args, matches);

        if (c == null) {
            // Not a full command
            return filter(strings, matches);
        }

        // A full command
        if (args.size() < 1) {
            return Collections.emptyList();
        }

        List<CommandArgs> formalArgs = BukkitSurvivalGamesPlugin.commandArgs.get(c);
        if (args.size() > formalArgs.size()) {
            return Collections.emptyList();
        }

        CommandArgs formalArg = formalArgs.get(args.size() - 1);
        switch (formalArg.getType()) {

            case ID:
                matches = getIDs();
                break;
            case PLAYER:
                matches = getPlayers();
                break;
            case WORLD:
                matches = getWorlds();
                break;
            case FILE:
                matches = getFiles();
                break;
            case SPONSOR:
                matches = getSponsors();
                break;
            case BACKUP:
                matches = getBackups();
                break;
            case NONE:
            default:
                return Collections.emptyList();
        }

        return filter(strings, matches);
    }

    private List<String> filter(String[] strings, List<String> matches) {
        List<String> list = matches.stream()
                .map(String::toLowerCase)
                .filter(string -> string.startsWith(strings[strings.length - 1]))
                .collect(Collectors.toList());
        if (list.size() == 1) {
            return list.stream().map(s -> s + " ").collect(Collectors.toList());
        }
        return list;
    }

    private List<String> getIDs() {
        return new ArrayList<>(BukkitSurvivalGamesPlugin.survivalGameMap.keySet());
    }

    private List<String> getPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
    }

    private List<String> getWorlds() {
        return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
    }

    private List<String> getFiles() {
        return Arrays.asList(BukkitSurvivalGamesPlugin.plugin.getDataFolder().list());
    }

    private List<String> getSponsors() {
        return new ArrayList<>(Sponsors.listAll());
    }

    private List<String> getBackups() {
        File backupsFolder = new File(BukkitSurvivalGamesPlugin.plugin.getDataFolder(), "Backups");

        if (!backupsFolder.isDirectory()) {
            if (!backupsFolder.delete()) {
                return Collections.emptyList();
            }
        }

        if (!backupsFolder.exists()) {
            if (!backupsFolder.mkdirs()) {
                return Collections.emptyList();
            }
        }

        return Arrays.asList(backupsFolder.list());
    }

}
