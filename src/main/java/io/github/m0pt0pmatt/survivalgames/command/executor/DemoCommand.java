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
package io.github.m0pt0pmatt.survivalgames.command.executor;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.thread.DotProgressable;
import io.github.m0pt0pmatt.survivalgames.thread.ProgressBuilder;
import io.github.m0pt0pmatt.survivalgames.thread.UnzipRunnable;
import io.github.m0pt0pmatt.survivalgames.thread.UrlDownloadRunnable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

import static io.github.m0pt0pmatt.survivalgames.Util.sendSuccess;

class DemoCommand extends LeafCommand {

    private static final DemoCommand INSTANCE = new DemoCommand();

    private static final String DEMO_MAP_OBJECT = "ssg-demo-map_v2.zip";

    private static final String DEMO_MAP_WORLD_NAME = "ssg-demo-map";

    private static final String DEMO_MAP_ID = "e6514a90-390f-4481-8d4d-56d30660c849";

    private static final String S3_BUCKET =
            "https://s3.amazonaws.com/com.cloudcraftnetwork.survivalgames.maps/";

    private DemoCommand() {
        super(RootCommand.getInstance(), "demo", GenericArguments.none());
    }

    @Override
    @Nonnull
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args)
            throws CommandException {

        deleteExistingWorld(src, DEMO_MAP_WORLD_NAME);

        URL url;
        try {
            url = new URL(S3_BUCKET + DEMO_MAP_OBJECT);
        } catch (MalformedURLException e) {
            throw new CommandException(Text.of("Bad url"), e);
        }

        UrlDownloadRunnable urlDownloadRunnable = new UrlDownloadRunnable(url, DEMO_MAP_OBJECT, 24869722);
        Runnable unzipRunnable = () -> {

            File existing = new File("world", DEMO_MAP_WORLD_NAME);
            if (existing.exists()) {
                try {
                    MoreFiles.deleteRecursively(existing.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            new UnzipRunnable(DEMO_MAP_OBJECT, "world").run();
        };

        TemporalAmount timeout = Duration.of(60, ChronoUnit.SECONDS);

        ProgressBuilder.builder(src, SurvivalGamesPlugin.SYNC_EXECUTOR, SurvivalGamesPlugin.ASYNC_EXECUTOR)
                .runAsync(urlDownloadRunnable, "Downloading World", timeout)
                .runAsync(new DotProgressable(unzipRunnable), "Unzipping World", timeout)
                .runSync(new DotProgressable(() ->  {

                    if (!Sponge.getServer().loadWorld(DEMO_MAP_WORLD_NAME).isPresent()) {
                        throw new RuntimeException("Could not load world, check logs");
                    }

                }), "Loading World", timeout)
                .runSync(new DotProgressable(() -> {
                    downloadConfig();
                    sendSuccess(src, "Loaded Demo Map and Config");
                }), "Downloading Config", timeout)
                .start();

        return CommandResult.success();
    }

    private void downloadConfig() {
        try {
            ReadableByteChannel readableByteChannel =
                    Channels.newChannel(new URL(S3_BUCKET + "demo.yml").openStream());
            FileOutputStream fileOutputStream =
                    new FileOutputStream(
                            "config"
                                    + File.separator
                                    + "survival-games"
                                    + File.separator
                                    + "demo.yml");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to download config", e);
        }
    }

    private void deleteExistingWorld(CommandSource src, String worldName) {
        Optional<World> existingWorld = Sponge.getServer().getWorld(worldName);
        existingWorld.ifPresent(
                world -> {
                    WorldProperties properties = world.getProperties();
                    src.sendMessage(Text.of("Unloading: ", worldName));
                    Sponge.getServer().unloadWorld(world);
                    src.sendMessage(Text.of("Deleting: ", worldName));
                    Sponge.getServer().deleteWorld(properties);
                });
    }

    static DemoCommand getInstance() {
        return INSTANCE;
    }
}
