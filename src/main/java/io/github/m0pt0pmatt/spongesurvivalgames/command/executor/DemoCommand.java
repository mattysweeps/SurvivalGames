/*
 * This file is part of SpongeSurvivalGamesPlugin, licensed under the MIT License (MIT).
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
package io.github.m0pt0pmatt.spongesurvivalgames.command.executor;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Nonnull;

class DemoCommand extends BaseCommand {

    private static final DemoCommand INSTANCE = new DemoCommand();

    private static final String DEMO_LOBBY_WORLD_NAME = "ssg-lobby";
    private static final String DEMO_MAP_WORLD_NAME = "ssg-map";

    private static final int BUFFER_SIZE = 4096;

    private DemoCommand() {
        super(
                RootCommand.getInstance(),
                "demo", GenericArguments.none(), Collections.emptyMap());
    }

    @Override
    @Nonnull
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {

        try {
            loadMap(src, DEMO_LOBBY_WORLD_NAME);
        } catch (Exception e) {
            throw new CommandException(Text.of("Error Loading Lobby: " + e.getMessage()), e);
        }
        try {
            loadMap(src, DEMO_MAP_WORLD_NAME);
        } catch (Exception e) {
            throw new CommandException(Text.of("Error Loading Map: " + e.getMessage()), e);
        }

        downloadConfig();

        src.sendMessage(Text.of("Done"));
        return CommandResult.success();
    }

    private void downloadConfig() throws CommandException {
        try {
            URL url = new URL("http://com.cloudcraftnetwork.survivalgames.maps.s3-website-us-east-1.amazonaws.com/demo.yml");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("config" + File.separator + "sponge-survival-games" + File.separator + "demo.yml");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (MalformedURLException e) {
            throw new CommandException(Text.of("URL is malformed"), e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMap(CommandSource src, String name) throws Exception {
        if (Sponge.getServer().getWorld(name).isPresent()) {

            WorldProperties properties = Sponge.getServer().getWorld(name).get().getProperties();

            src.sendMessage(Text.of("Unloading: ", name));
            Sponge.getServer().unloadWorld(Sponge.getServer().getWorld(name).get());

            src.sendMessage(Text.of("Deleting: ", name));
            Sponge.getServer().deleteWorld(properties);
        }

        src.sendMessage(Text.of("Downloading: ", name));
        URL url;
        try {
            url = new URL("http://com.cloudcraftnetwork.survivalgames.maps.s3-website-us-east-1.amazonaws.com/" + name + ".zip");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(name + ".zip");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (MalformedURLException e) {
            throw new CommandException(Text.of(name + " URL is malformed"), e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File destDir = new File("world", name);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        src.sendMessage(Text.of("Unzipping: ", name));
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(name + ".zip"));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = "world" + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (Exception e) {

        }

        src.sendMessage(Text.of("Loading: ", name));
        Sponge.getServer().loadWorld(name);

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
