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
package io.github.m0pt0pmatt.survivalgames.command.executor;

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;
import static io.github.m0pt0pmatt.survivalgames.Util.sendSuccess;

import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.command.element.ConfigFileCommandElement;
import io.github.m0pt0pmatt.survivalgames.data.GameConfig;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameRepository;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import javax.annotation.Nonnull;

public class LoadConfigCommand extends BaseCommand {

    private static SurvivalGamesCommand INSTANCE = new LoadConfigCommand();

    private LoadConfigCommand() {
        super(
                RootCommand.getInstance(),
                "load",
                GenericArguments.seq(
                        GenericArguments.string(CommandKeys.SURVIVAL_GAME_NAME),
                        ConfigFileCommandElement.getInstance()),
                Collections.emptyMap());
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {

        Path potentialFile = (Path) getOrThrow(args, CommandKeys.FILE_PATH);
        String survivalGameName = (String) getOrThrow(args, CommandKeys.SURVIVAL_GAME_NAME);

        if (SurvivalGameRepository.contains(survivalGameName)) {
            throw new CommandException(Text.of("Already exists a game of the same name"));
        }

        ConfigurationLoader<CommentedConfigurationNode> loader =
                HoconConfigurationLoader.builder().setPath(potentialFile).build();
        try {

            CommentedConfigurationNode node = loader.load(ConfigurationOptions.defaults());

            ObjectMapper.BoundInstance i = GameConfig.OBJECT_MAPPER.bindToNew();
            GameConfig config = (GameConfig) i.populate(node);
            SurvivalGame game = new SurvivalGame(survivalGameName, config);
            SurvivalGameRepository.put(survivalGameName, game);

        } catch (IOException | ObjectMappingException | RuntimeException e) {
            e.printStackTrace();
            throw new CommandException(Text.of("An error occurred when loading"));

        }

        sendSuccess(src, "Survival Game Loaded", survivalGameName);
        return CommandResult.success();
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
