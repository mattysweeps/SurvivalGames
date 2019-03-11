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

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

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

public class ProgressBuilder {

    private final MessageChannel messageChannel;
    private final Executor syncExecutor;
    private final Executor asyncExecutor;
    private final List<WatcherRunnable> tasks = new ArrayList<>();

    private boolean error = false;

    private ProgressBuilder(MessageChannel messageChannel, Executor syncExecutor, Executor asyncExecutor) {
        this.messageChannel = checkNotNull(messageChannel, "messageReceiver");
        this.syncExecutor = checkNotNull(syncExecutor, "syncExecutor");
        this.asyncExecutor = checkNotNull(asyncExecutor, "asyncExecutor");
    }

    public ProgressBuilder runSync(Progressable progressable, String name, TemporalAmount timeout) {
        checkNotNull(progressable, "progressable");

        WatcherRunnable watcher = new WatcherRunnable(progressable, name, timeout, messageChannel, syncExecutor);
        tasks.add(watcher);

        return this;
    }

    public ProgressBuilder runSync(Runnable runnable, String name, TemporalAmount timeout) {
        checkNotNull(runnable, "runnable");

        WatcherRunnable watcher = new WatcherRunnable(new DotProgressable(runnable), name, timeout, messageChannel, syncExecutor);
        tasks.add(watcher);

        return this;
    }

    public ProgressBuilder runSync(Consumer<MessageChannel> consumer, String name, TemporalAmount timeout) {
        checkNotNull(consumer, "consumer");

        WatcherRunnable watcher = new WatcherRunnable(new DotProgressable(() -> consumer.accept(messageChannel)), name, timeout, messageChannel, syncExecutor);
        tasks.add(watcher);

        return this;
    }

    public ProgressBuilder runAsync(Progressable progressable, String name, TemporalAmount timeout) {
        checkNotNull(progressable, "progressable");

        WatcherRunnable watcher = new WatcherRunnable(progressable, name, timeout, messageChannel, asyncExecutor);
        tasks.add(watcher);

        return this;
    }

    public ProgressBuilder runAsync(Runnable runnable, String name, TemporalAmount timeout) {
        checkNotNull(runnable, "runnable");

        WatcherRunnable watcher = new WatcherRunnable(new DotProgressable(runnable), name, timeout, messageChannel, asyncExecutor);
        tasks.add(watcher);

        return this;
    }

    public ProgressBuilder runAsync(Consumer<MessageChannel> consumer, String name, TemporalAmount timeout) {
        checkNotNull(consumer, "consumer");

        WatcherRunnable watcher = new WatcherRunnable(new DotProgressable(() -> consumer.accept(messageChannel)), name, timeout, messageChannel, asyncExecutor);
        tasks.add(watcher);

        return this;
    }

    public CompletableFuture<Void> start() {
        CompletableFuture<Void> future = null;
        for (WatcherRunnable task: tasks) {
            if (future == null) {
                future = CompletableFuture.runAsync(handleException(task), asyncExecutor);
            } else {
                future = future.thenRunAsync(handleException(task), asyncExecutor);
            }
        }
        return future;
    }


    private Runnable handleException(Runnable runnable) {
        return () -> {
            if (!error) {
                try {
                    runnable.run();
                } catch (Throwable throwable) {
                    messageChannel.send(Text.of("Error running command. Check the server logs for more details"));
                    Optional.ofNullable(throwable.getCause()).ifPresent(Throwable::printStackTrace);
                    error = true;
                }
            }
        };
    }

    public static ProgressBuilder builder(MessageChannel messageChannel, Executor syncExecutor, Executor asyncExecutor) {
        return new ProgressBuilder(messageChannel, syncExecutor, asyncExecutor);
    }

    public static ProgressBuilder builder(CommandSource commandSource, Executor syncExecutor, Executor asyncExecutor) {
        return new ProgressBuilder(MessageChannel.fixed(commandSource), syncExecutor, asyncExecutor);
    }
}
