/*
 * This file is part of SurvivalGamesPlugin, licensed under the MIT License (MIT).
 *
 * Copyright (c) Matthew Broomfield <m0pt0pmatt17@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.m0pt0pmatt.survivalgames.mobspawn;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.data.MobSpawnArea;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ActiveMobSpawnRepository {

    private static final Map<UUID, SpongeExecutorService.SpongeFuture> FUTURE_MAP = new ConcurrentHashMap<>();

    private ActiveMobSpawnRepository() {

    }

    public static UUID start(SurvivalGame survivalGame, MobSpawnArea mobSpawnArea, World world) {
        int millisecondsPerSpawn = mobSpawnArea.getSpawnRatePerMinute()
                .map(integer -> 1000 * 60 / integer)
                .orElse(0);

        SpongeExecutorService.SpongeFuture future =
                SurvivalGamesPlugin.EXECUTOR.scheduleAtFixedRate(
                        new MobSpawnRunnable(survivalGame, mobSpawnArea, world),
                        0,
                        millisecondsPerSpawn,
                        TimeUnit.MILLISECONDS);

        FUTURE_MAP.put(mobSpawnArea.getId(), future);

        return mobSpawnArea.getId();
    }

    public static void stop(UUID uuid) {
        SpongeExecutorService.SpongeFuture future = FUTURE_MAP.remove(uuid);
        if (future != null) {
            future.cancel(true);
        }
    }
}
