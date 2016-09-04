/*
 *  QuestManager: An RPG plugin for the Bukkit API.
 *  Copyright (C) 2015-2016 Github Contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.m0pt0pmatt.spongesurvivalgames.command;

import com.google.common.collect.ImmutableMap;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.CreateGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.DeleteGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.ListGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.SurvivalGamesCommand;

public class CommandInitializer {

    private static final List<String> COMMAND_ALIASES = Arrays.asList(
            "spongesurvivalgames",
            "ssg");

    private static final Map<List<String>, ? extends CommandCallable> CHILDREN =
            ImmutableMap.<List<String>, CommandCallable>builder()
                    .put(toEntry(CreateGameCommand.getInstance()))
                    .put(toEntry(DeleteGameCommand.getInstance()))
                    .put(toEntry(ListGameCommand.getInstance()))
                    .build();

    private CommandInitializer() {

    }

    private static Map.Entry<List<String>, ? extends CommandCallable> toEntry(
            SurvivalGamesCommand command) {
        return new AbstractMap.SimpleImmutableEntry<>(
                command.getAliases(), toCommandCallable(command));
    }

    private static CommandCallable toCommandCallable(SurvivalGamesCommand command) {
        return CommandSpec.builder()
                .arguments(command.getArguments())
                .executor(command)
                .build();
    }

    private static CommandSpec buildCommandSpec() {
        return CommandSpec.builder()
                .children(CHILDREN)
                .build();
    }

    public static void initializeCommands(Object plugin) {
        Sponge.getCommandManager().register(plugin,buildCommandSpec(), COMMAND_ALIASES);
    }
}
