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
package io.github.m0pt0pmatt.spongesurvivalgames.command.executor;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

import java.util.Collections;

import javax.annotation.Nonnull;

import io.github.m0pt0pmatt.spongesurvivalgames.command.element.Keys;
import io.github.m0pt0pmatt.spongesurvivalgames.command.element.SurvivalGameNameCommandElement;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameRepository;

public class DeleteGameCommand extends BaseCommand {

    private static SurvivalGamesCommand INSTANCE = new DeleteGameCommand();

    private DeleteGameCommand() {
        super(
                Collections.singletonList("delete"),
                "",
                Text.of("usage"),
                SurvivalGameNameCommandElement.getInstance()
        );
    }

    @Override
    @Nonnull
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {
        String survivalGameName = (String) args.getOne(Keys.SURVIVAL_GAME_NAME)
                .orElseThrow(() -> new CommandException(Text.of("No Such Survival Game")));

        SurvivalGameRepository.remove(survivalGameName);

        return CommandResult.success();
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
