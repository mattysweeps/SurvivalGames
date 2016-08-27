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
package io.github.m0pt0pmatt.spongesurvivalgames.command.argument;

import org.spongepowered.api.entity.living.player.Player;

import io.github.m0pt0pmatt.spongesurvivalgames.command.tabcompleter.TabCompleters;
import io.github.m0pt0pmatt.spongesurvivalgames.command.tabcompleter.TestTabCompleter;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;

public class CommandArguments {

    public static final CommandArgument<SurvivalGame> SURVIVAL_GAME = new CommandArgument<>("[survival-game]", TabCompleters.SURVIVAL_GAME);
    public static final CommandArgument<Player> ONLINE_PLAYER = new CommandArgument<>("[player-name]", TabCompleters.ONLINE_PLAYER);
    public static final CommandArgument<String> TEST = new CommandArgument<>("[test]", new TestTabCompleter());
}
