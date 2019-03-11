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
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.command.executor.set.SetBlocksCommand;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameRepository;
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
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetype;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static io.github.m0pt0pmatt.survivalgames.Util.sendSuccess;

class DemoCommand extends LeafCommand {

    private static final DemoCommand INSTANCE = new DemoCommand();

    private static final String DEMO_MAP_OBJECT = "ssg-demo-map_v4.zip";

    private static final String DEMO_MAP_WORLD_NAME = "ssg-demo-map";

    private static final String S3_BUCKET =
            "https://s3.amazonaws.com/com.cloudcraftnetwork.survivalgames.maps/";

    private DemoCommand() {
        super(RootCommand.getInstance(), "demo", GenericArguments.none());
    }

    @Override
    @Nonnull
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args)
            throws CommandException {

        URL url;
        try {
            url = new URL(S3_BUCKET + DEMO_MAP_OBJECT);
        } catch (MalformedURLException e) {
            throw new CommandException(Text.of("Bad url"), e);
        }

        UrlDownloadRunnable urlDownloadRunnable = new UrlDownloadRunnable(url, DEMO_MAP_OBJECT, 25657717);
        TemporalAmount timeout = Duration.of(5, ChronoUnit.MINUTES);

        ProgressBuilder.builder(src, SurvivalGamesPlugin.SYNC_EXECUTOR, SurvivalGamesPlugin.ASYNC_EXECUTOR)
                .runSync(this::deleteExistingWorld, "Deleting Old World", timeout)
                .runAsync(urlDownloadRunnable, "Downloading World", timeout)
                .runAsync(this::unzipWorld, "Unzipping World", timeout)
                .runSync(this::loadWorld, "Loading World", timeout)
                .runSync(this::downloadConfig, "Downloading Config", timeout)
                .runSync(() -> loadConfig(src), "Loading Config", timeout)
                .runSync(() -> setBlocks(src), "Setting Blocks", timeout)
                .runAsync(this::downloadSchedule, "Downloading Schedule", timeout)
                .runSync(() -> scheduleGame(src), "Scheduling Game", timeout)
                .runSync(() -> sendSuccess(src, "Demo ready! Join with /ssg join demo"), "", timeout)
                .start();

        return CommandResult.success();
    }

    private void deleteExistingWorld() {

        Optional<World> world = Sponge.getServer().getWorld(DEMO_MAP_WORLD_NAME);
        if (world.isPresent()) {
            World w = world.get();
            if (!Sponge.getServer().unloadWorld(w)) {
                throw new RuntimeException("Could not unload demo world");
            }
        }

        Optional<WorldProperties> worldProperties = Sponge.getServer().getWorldProperties(DEMO_MAP_WORLD_NAME);
        if (worldProperties.isPresent()) {
            WorldProperties wp = worldProperties.get();
            CompletableFuture<Boolean> future = Sponge.getServer().deleteWorld(wp);
            try {
                if (!future.get(60, TimeUnit.SECONDS)) {
                    throw new RuntimeException("Could not delete demo world");
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Could not delete demo world");
            }
        }
    }

    private void unzipWorld() {
        File existing = new File(Sponge.getServer().getDefaultWorldName(), DEMO_MAP_WORLD_NAME);
        if (existing.exists()) {
            try {
                MoreFiles.deleteRecursively(existing.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        new UnzipRunnable(DEMO_MAP_OBJECT, Sponge.getServer().getDefaultWorldName()).run();
    }

    private void loadWorld() {
        try {
            WorldProperties props = Sponge.getServer().createWorldProperties(DEMO_MAP_WORLD_NAME, WorldArchetype.builder()
                    .dimension(DimensionTypes.OVERWORLD)
                    .enabled(true)
                    .pvp(true)
                    .build(UUID.randomUUID().toString(), DEMO_MAP_WORLD_NAME));

            if (!Sponge.getServer().loadWorld(props).isPresent()) {
                throw new RuntimeException("Could not load world, check logs");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private void loadConfig(CommandSource src) {
        CommandContext loadContext = new CommandContext();
        loadContext.putArg(CommandKeys.SURVIVAL_GAME_NAME, "demo");
        loadContext.putArg(CommandKeys.FILE_PATH, new File("config"
                + File.separator
                + "survival-games"
                + File.separator
                + "demo.yml").toPath());

        try {
            LoadConfigCommand.getInstance().execute(src, loadContext);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    private void setBlocks(CommandSource src) {
        CommandContext context = new CommandContext();
        context.putArg(CommandKeys.SURVIVAL_GAME, SurvivalGameRepository.get("demo")
                .orElseThrow(() -> new RuntimeException("Demo map didn't exist")));

        try {
            SetBlocksCommand.getInstance().execute(src, context);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadSchedule() {
        try {
            ReadableByteChannel readableByteChannel =
                    Channels.newChannel(new URL(S3_BUCKET + "demo-schedule.yml").openStream());
            FileOutputStream fileOutputStream =
                    new FileOutputStream(
                            "config"
                                    + File.separator
                                    + "survival-games"
                                    + File.separator
                                    + "demo-schedule.yml");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to download config", e);
        }
    }

    private void scheduleGame(CommandSource src) {
        CommandContext context = new CommandContext();
        context.putArg(CommandKeys.SURVIVAL_GAME, SurvivalGameRepository.get("demo")
                .orElseThrow(() -> new RuntimeException("Demo map didn't exist")));

        context.putArg(CommandKeys.FILE_PATH, new File("config"
                + File.separator
                + "survival-games"
                + File.separator
                + "demo-schedule.yml").toPath());

        try {
            ScheduleCommand.getInstance().execute(src, context);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    static DemoCommand getInstance() {
        return INSTANCE;
    }
}
