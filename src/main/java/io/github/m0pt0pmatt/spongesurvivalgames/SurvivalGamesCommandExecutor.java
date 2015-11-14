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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Command Executor for the plugin
 */
class SurvivalGamesCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        List<String> args = new ArrayList<>(strings.length);
        Collections.addAll(args, strings);
        args = args.parallelStream().map(String::toLowerCase).collect(Collectors.toList());

        //Gather command
        SurvivalGamesCommand c = BukkitSurvivalGamesPlugin.commandTrie.match(args);
        if (c == null) {
            commandSender.sendMessage("No command found");
            return false;
        }

        //Gather arguments
        Map<CommandArgs, String> argumentMap = new HashMap<>();
        List<CommandArgs> formalArgs = BukkitSurvivalGamesPlugin.commandArgs.get(c);
        if (formalArgs == null) {
            formalArgs = new LinkedList<>();
        }

        for (int i = 0; i < args.size() && i < formalArgs.size(); i++) {
            argumentMap.put(formalArgs.get(i), args.get(i));
        }

        if (!c.execute(commandSender, argumentMap)) {
            sendUsageMessage(commandSender, formalArgs);
        }

        return true;
    }

    private void sendUsageMessage(CommandSender commandSender, List<CommandArgs> formalArgs) {
        StringBuilder builder = new StringBuilder();
        builder.append("Usage: /ssg");
        formalArgs.stream().map(a -> " " + a).forEach(builder::append);
        commandSender.sendMessage(builder.toString());
    }
}
