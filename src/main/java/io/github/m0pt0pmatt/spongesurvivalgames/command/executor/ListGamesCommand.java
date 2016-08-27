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
import io.github.m0pt0pmatt.spongesurvivalgames.command.ArgumentValues;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameRepository;

public class ListGamesCommand implements SurvivalGamesCommand {
    @Override
    public String getName() {
        return "list";
    }

    @ArgumentProvider
    public List<Argument<?>> argumentList() {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull ArgumentValues arguments) throws CommandException {

        source.sendMessage(Text.of("Survival Games:"));
        SurvivalGameRepository.values().stream()
                .map(SurvivalGame::getID)
                .map(Text::of)
                .forEach(source::sendMessage);

        return CommandResult.success();
    }
}
