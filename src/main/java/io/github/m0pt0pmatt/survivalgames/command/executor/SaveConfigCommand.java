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

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;
import static io.github.m0pt0pmatt.survivalgames.Util.sendSuccess;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.command.element.ConfigFileCommandElement;
import io.github.m0pt0pmatt.survivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.survivalgames.data.GameConfig;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.Nonnull;
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

public class SaveConfigCommand extends LeafCommand {
    private static SurvivalGamesCommand INSTANCE = new SaveConfigCommand();

    private SaveConfigCommand() {
        super(
                RootCommand.getInstance(),
                "save",
                GenericArguments.seq(
                        SurvivalGameCommandElement.getInstance(),
                        GenericArguments.firstParsing(
                                ConfigFileCommandElement.getInstance(),
                                GenericArguments.string(CommandKeys.FILE_NAME))));
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args)
            throws CommandException {

        Path potentialFile;

        if (args.hasAny(CommandKeys.FILE_PATH)) {
            potentialFile = (Path) getOrThrow(args, CommandKeys.FILE_PATH);
        } else if (args.hasAny(CommandKeys.FILE_NAME)) {
            String potentialFileName = (String) getOrThrow(args, CommandKeys.FILE_NAME);
            potentialFile = SurvivalGamesPlugin.CONFIG_DIRECTORY.resolve(potentialFileName);
        } else {
            throw new CommandException(Text.of("No file name"));
        }

        SurvivalGame survivalGame = (SurvivalGame) getOrThrow(args, CommandKeys.SURVIVAL_GAME);
        ConfigurationLoader<CommentedConfigurationNode> loader =
                HoconConfigurationLoader.builder().setPath(potentialFile).build();
        try {
            CommentedConfigurationNode node = loader.load(ConfigurationOptions.defaults());
            ObjectMapper.BoundInstance i = GameConfig.OBJECT_MAPPER.bind(survivalGame.getConfig());
            i.serialize(node);
            loader.save(node);
        } catch (IOException | ObjectMappingException | RuntimeException e) {
            e.printStackTrace();
            throw new CommandException(Text.of("Error saving to file"), e);
        }

        sendSuccess(src, "Survival Game Saved", potentialFile.getFileName());
        return CommandResult.success();
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
