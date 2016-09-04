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
package io.github.m0pt0pmatt.spongesurvivalgames.command.element;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.SelectorCommandElement;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameRepository;

public class SurvivalGameNameCommandElement extends SelectorCommandElement {

    private static final CommandElement INSTANCE = new SurvivalGameNameCommandElement();

    private SurvivalGameNameCommandElement() {
        super(Keys.SURVIVAL_GAME_NAME);
    }

    @Nonnull
    @Override
    protected Iterable<String> getChoices(@Nonnull CommandSource source) {
        return SurvivalGameRepository.values().stream()
                .map(SurvivalGame::getName)
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    protected Object getValue(@Nonnull String choice) throws IllegalArgumentException {
        Optional<SurvivalGame> survivalGame = SurvivalGameRepository.get(choice);
        if (survivalGame.isPresent()) {
            return survivalGame.get().getName();
        }
        throw new IllegalArgumentException();
    }

    public static CommandElement getInstance() {
        return INSTANCE;
    }
}
