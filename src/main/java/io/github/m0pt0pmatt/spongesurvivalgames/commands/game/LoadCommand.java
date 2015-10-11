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

package io.github.m0pt0pmatt.spongesurvivalgames.commands.game;

import com.google.common.reflect.TypeToken;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfigSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class LoadCommand extends GameCommand {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Optional<String> fileName = args.getOne("fileName");
        if (!fileName.isPresent()) {
            SpongeSurvivalGamesPlugin.logger.error("No file name given.");
            return CommandResult.empty();
        }

        File file = new File(fileName.get());
        ConfigurationLoader<CommentedConfigurationNode> loader =
                HoconConfigurationLoader.builder().setFile(file).build();
        SurvivalGameConfigSerializer serializer = new SurvivalGameConfigSerializer();

        ConfigurationNode node;
        try {
            node = loader.load();
        } catch (IOException e) {
            SpongeSurvivalGamesPlugin.logger.error("Unable to load config file");
            return CommandResult.empty();
        }

        SurvivalGameConfig config;
        try {
            config = serializer.deserialize(TypeToken.of(SurvivalGameConfig.class), node);
        } catch (ObjectMappingException e) {
            SpongeSurvivalGamesPlugin.logger.error("Mapping error for config file");
            return CommandResult.empty();
        }

        SpongeSurvivalGamesPlugin.survivalGameMap.get(id).setConfig(config);
        SpongeSurvivalGamesPlugin.logger.info("Config file loaded");
        return CommandResult.success();
    }
}
