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
package io.github.m0pt0pmatt.spongesurvivalgames.util;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentRepository<T> implements Repository<T> {

    private final Map<UUID, T> MAP = new ConcurrentHashMap<>();

    @Override
    public void put(UUID uuid, T object) {
        MAP.put(uuid, object);
    }

    @Override
    public Optional<T> get(UUID uuid) {
        return Optional.ofNullable(MAP.get(uuid));
    }

    @Override
    public Collection<T> values() {
        return MAP.values();
    }
}
