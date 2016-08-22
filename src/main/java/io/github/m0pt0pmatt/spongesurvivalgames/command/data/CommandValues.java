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
package io.github.m0pt0pmatt.spongesurvivalgames.command.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.github.m0pt0pmatt.spongesurvivalgames.command.argument.CommandArgument;

public class CommandValues {

    private final Map<CommandArgument<?>, CommandValue<?>> arguments = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Optional<CommandValue<T>> get(CommandArgument<T> argument, Class<T> type) {
        return Optional.ofNullable((CommandValue<T>) arguments.get(argument));
    }

    public <T> void put(CommandArgument<T> argument, CommandValue<T> value) {
        arguments.put(argument, value);
    }
}
