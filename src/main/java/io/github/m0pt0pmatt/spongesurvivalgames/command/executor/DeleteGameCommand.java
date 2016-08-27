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
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import io.github.m0pt0pmatt.spongesurvivalgames.command.ArgumentProvider;
import io.github.m0pt0pmatt.spongesurvivalgames.command.Argument;
import io.github.m0pt0pmatt.spongesurvivalgames.command.Arguments;
import io.github.m0pt0pmatt.spongesurvivalgames.command.ArgumentValue;
import io.github.m0pt0pmatt.spongesurvivalgames.command.ArgumentValues;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameRepository;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameStateManager;

public class DeleteGameCommand implements SurvivalGamesCommand {

    @Override
    public String getName() {
        return "delete";
    }

    @ArgumentProvider
    public List<Argument<?>> argumentList() {
        return Collections.singletonList(Arguments.SURVIVAL_GAME);
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull ArgumentValues arguments) throws CommandException {

        ArgumentValue<SurvivalGame> game = arguments.get(Arguments.SURVIVAL_GAME);
        if (!game.getValue().isPresent()) {
            source.sendMessage(Text.of("No such SurvivalGame with the name: " + game.getArgument()));
            return CommandResult.empty();
        }

        SurvivalGame g = game.getValue().get();
        if (g.getState() != SurvivalGameStateManager.SurvivalGameState.STOPPED) {
            source.sendMessage(Text.of("Cannot delete the SurvivalGame: " + game.getArgument() + ". State must be STOPPED."));
            return CommandResult.empty();
        }

        SurvivalGameRepository.remove(g.getID());
        source.sendMessage(Text.of("Deleted the SurvivalGame: " + game.getArgument()));
        return CommandResult.success();
    }
}
