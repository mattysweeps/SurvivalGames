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

import org.spongepowered.api.Sponge;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import io.github.m0pt0pmatt.spongesurvivalgames.command.BaseCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.SurvivalGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.util.Util;

public class CommandRepository {

    private static final Map<String, SurvivalGamesCommand> MAP = new ConcurrentHashMap<>();
    private static final BaseCommand baseCommand = new BaseCommand();

    private CommandRepository() {

    }

    public static void put(SurvivalGamesCommand survivalGamesCommand) {
        MAP.put(survivalGamesCommand.getName(), survivalGamesCommand);
    }

    public static Optional<SurvivalGamesCommand> get(String name) {
        return Optional.ofNullable(MAP.get(name));
    }

    public static Collection<SurvivalGamesCommand> values() {
        return MAP.values();
    }

    public static void initializeCommands(Object plugin) {
        Util.addComponents(MAP, SurvivalGamesCommand.class, SurvivalGamesCommand::getName);
        Sponge.getCommandManager().register(plugin, baseCommand, "ssg");
        baseCommand.registerCommands();
    }
}
