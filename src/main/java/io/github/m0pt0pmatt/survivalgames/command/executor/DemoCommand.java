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

import static io.github.m0pt0pmatt.survivalgames.Util.sendSuccess;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Collections;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Nonnull;

class DemoCommand extends BaseCommand {

    private static final DemoCommand INSTANCE = new DemoCommand();

    private static final String DEMO_MAP_WORLD_NAME = "ssg-demo-map";
    private static final String S3_BUCKET = "http://com.cloudcraftnetwork.survivalgames.maps.s3-website-us-east-1.amazonaws.com/";

    private static final int BUFFER_SIZE = 4096;

    private DemoCommand() {
        super(
                RootCommand.getInstance(),
                "demo", GenericArguments.none(), Collections.emptyMap());
    }

    @Override
    @Nonnull
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {
        deleteExistingWorld(src, DEMO_MAP_WORLD_NAME);
        loadMap(src, DEMO_MAP_WORLD_NAME);
        downloadConfig();
        Sponge.getCommandManager().process(src, "/ssg create demo demo.yml");
        Sponge.getCommandManager().process(src, "/ssg set blocks demo");
        sendSuccess(src, "Loaded Demo Map and Config");
        return CommandResult.success();
    }

    private void downloadConfig() throws CommandException {
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(S3_BUCKET + "demo.yml").openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("config" + File.separator + "survival-games" + File.separator + "demo.yml");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
        } catch (Exception e) {
            throw new CommandException(Text.of("Unable to download config"), e);
        }
    }

    private void loadMap(CommandSource src, String worldName) throws CommandException {

        src.sendMessage(Text.of("Downloading: ", worldName));
        URL url;
        try {
            url = new URL(S3_BUCKET + worldName + ".zip");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(worldName + ".zip");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Exception e) {
            throw new CommandException(Text.of("Unable to download world"), e);
        }

        File destDir = new File("world", worldName);
        if (!destDir.exists()) {
            if (!destDir.mkdir()) {
                throw new CommandException(Text.of("Unable to create folder for world"));
            }
        }

        src.sendMessage(Text.of("Unzipping: ", worldName));
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(worldName + ".zip"));
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = "world" + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(Text.of("Unable to unzip world"), e);
        }

        src.sendMessage(Text.of("Loading: ", worldName));
        Sponge.getServer().loadWorld(worldName);
    }

    private void deleteExistingWorld(CommandSource src, String worldName) {
        Optional<World> existingWorld = Sponge.getServer().getWorld(worldName);
        if (existingWorld.isPresent()) {
            WorldProperties properties = existingWorld.get().getProperties();
            src.sendMessage(Text.of("Unloading: ", worldName));
            Sponge.getServer().unloadWorld(existingWorld.get());
            src.sendMessage(Text.of("Deleting: ", worldName));
            Sponge.getServer().deleteWorld(properties);
        }
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    static DemoCommand getInstance(){
        return INSTANCE;
    }
}
