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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameRepository;

class SurvivalGameTabCompleter implements TabCompleter<SurvivalGame> {

    private static final TabCompleter<SurvivalGame> INSTANCE = new SurvivalGameTabCompleter();

    private SurvivalGameTabCompleter() {

    }

    @Override
    public List<String> getSuggestions(String argument) {
        return SurvivalGameRepository.values().stream()
                .map(SurvivalGame::getID)
                .filter(s -> s.startsWith(argument))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SurvivalGame> getValue(String argument) {
        return SurvivalGameRepository.get(argument);
    }

    public static TabCompleter<SurvivalGame> get() {
        return INSTANCE;
    }
}
