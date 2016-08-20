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
package io.github.m0pt0pmatt.spongesurvivalgames.game;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.util.ConcurrentRepository;
import io.github.m0pt0pmatt.spongesurvivalgames.util.Repository;

public class SurvivalGameRepository {

    private static final Repository<SurvivalGame> REPOSITORY = new ConcurrentRepository<>();

    public static void put(UUID uuid, SurvivalGame survivalGame) {
        REPOSITORY.put(uuid, survivalGame);
    }

    public static Optional<SurvivalGame> get(UUID uuid) {
        return REPOSITORY.get(uuid);
    }

    public static Collection<SurvivalGame> values() {
        return REPOSITORY.values();
    }
}
