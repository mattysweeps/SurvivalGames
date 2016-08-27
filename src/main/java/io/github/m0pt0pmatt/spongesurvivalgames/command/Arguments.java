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

import org.spongepowered.api.entity.living.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import io.github.m0pt0pmatt.spongesurvivalgames.command.Argument;
import io.github.m0pt0pmatt.spongesurvivalgames.command.tabcompleter.NoopTabCompleter;
import io.github.m0pt0pmatt.spongesurvivalgames.command.tabcompleter.OnlinePlayerTabCompleter;
import io.github.m0pt0pmatt.spongesurvivalgames.command.tabcompleter.SurvivalGameNameTabCompleter;
import io.github.m0pt0pmatt.spongesurvivalgames.command.tabcompleter.SurvivalGameTabCompleter;
import io.github.m0pt0pmatt.spongesurvivalgames.command.tabcompleter.TabCompleter;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;

import static io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin.LOGGER;

public class Arguments {

    private static final Map<Class<?>, TabCompleter<?>> MAP = new ConcurrentHashMap<>();
    private static final EmptyTabCompleter EMPTY_TAB_COMPLETER = new EmptyTabCompleter();

    public static final Argument<SurvivalGame> SURVIVAL_GAME = new Argument<>("[survival-game]", get(SurvivalGameTabCompleter.class));
    public static final Argument<Player> ONLINE_PLAYER = new Argument<>("[player-name]", get(OnlinePlayerTabCompleter.class));
    public static final Argument<String> SURVIVAL_GAME_NAME = new Argument<>("[name]", get(SurvivalGameNameTabCompleter.class));
    public static final Argument<String> NEW_SURVIVAL_GAME_NAME = new Argument<>("[name]", get(NoopTabCompleter.class));

    @SuppressWarnings("unchecked")
    private static <T> TabCompleter<T> get(Class<? extends TabCompleter<T>> type) {

        if (MAP.containsKey(type)) {
            return (TabCompleter<T>) MAP.get(type);
        }

        try {
            TabCompleter<T> newInstance = type.newInstance();
            MAP.put(type, newInstance);
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unable to load Tab Completer: " + type, e);
            return (TabCompleter<T>) EMPTY_TAB_COMPLETER;
        }
    }

    private static class EmptyTabCompleter implements TabCompleter<Object> {

        @Override
        public List<String> getSuggestions(String argument) {
            return Collections.emptyList();
        }

        @Override
        public Optional<Object> getValue(String argument) {
            return Optional.empty();
        }
    }
}
