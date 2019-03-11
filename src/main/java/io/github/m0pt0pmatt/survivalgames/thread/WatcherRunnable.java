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

package io.github.m0pt0pmatt.survivalgames.thread;

import com.google.common.base.Strings;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.chat.ChatTypes;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Runnable which watches another runnable.
 */
public class WatcherRunnable implements Runnable {

    private final Progressable progressable;
    private final String name;
    private final TemporalAmount timeout;
    private final MessageChannel messageChannel;
    private final Executor executor;

    WatcherRunnable(
            Progressable progressable,
            String name,
            TemporalAmount timeout,
            MessageChannel messageChannel,
            Executor executor) {
        this.progressable = checkNotNull(progressable, "future");
        this.name = checkNotNull(name, "name");
        this.timeout = timeout;
        this.messageChannel = checkNotNull(messageChannel, "messageChannel");
        this.executor = checkNotNull(executor, "executor");
    }

    @Override
    public void run() {

        Set<Throwable> throwable = new HashSet<>();

        CompletableFuture<Void> future = CompletableFuture.runAsync(progressable, executor).handle((r, t) -> {
            throwable.add(t);
            return r;
        });


        Optional<Instant> later = Optional.ofNullable(timeout).map(t -> Instant.now().plus(t));

        while (true) {
            sendMessage();

            if (future.isDone()) {
                break;
            }

            if (later.isPresent() && Instant.now().isAfter(later.get())) {
                messageChannel.send(Text.of("Timed out"), ChatTypes.CHAT);
                future.cancel(true);
                throw new RuntimeException("timeout");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        Throwable t = throwable.iterator().next();
        if (t != null) {
            throw new RuntimeException(t);
        }
    }

    private void sendMessage() {
        if (!Strings.isNullOrEmpty(name)) {
            messageChannel.send(Text.of(name + ": " + progressable.getProgress()), ChatTypes.ACTION_BAR);
        }
    }
}
