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
package io.github.m0pt0pmatt.spongesurvivalgames.command.tabcompleter;

import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TestTabCompleter implements TabCompleter<String> {

    Set<String> set = ImmutableSet.<String>builder()
            .add("people")
            .add("pee")
            .add("add")
            .add("adad")
            .build();

    @Override
    public List<String> getSuggestions(String argument) {
        return set.stream().filter(s -> s.startsWith(argument)).collect(Collectors.toList());
    }

    @Override
    public Optional<String> getValue(String argument) {
        if (set.contains(argument)) {
            return Optional.of(argument);
        }
        return Optional.empty();
    }
}
