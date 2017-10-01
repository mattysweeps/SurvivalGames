/*
 * This file is part of SurvivalGames, licensed under the MIT License (MIT).
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
package io.github.m0pt0pmatt.survivalgames.interval;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.event.IntervalEvent;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.SpongeExecutorService;

public final class ActiveIntervalRepository {

    private static final Map<UUID, SpongeExecutorService.SpongeFuture> FUTURE_MAP =
            new ConcurrentHashMap<>();

    private ActiveIntervalRepository() {}

    public static UUID start(SurvivalGame survivalGame, String intervalName, int intervalSeconds) {

        SpongeExecutorService.SpongeFuture future =
                SurvivalGamesPlugin.EXECUTOR.scheduleAtFixedRate(
                        () ->
                                Sponge.getEventManager()
                                        .post(new IntervalEvent(survivalGame, intervalName)),
                        0,
                        intervalSeconds,
                        TimeUnit.SECONDS);

        UUID uuid = UUID.randomUUID();
        FUTURE_MAP.put(uuid, future);
        return uuid;
    }

    public static void stop(UUID uuid) {
        SpongeExecutorService.SpongeFuture future = FUTURE_MAP.remove(uuid);
        if (future != null) {
            future.cancel(true);
        }
    }
}
