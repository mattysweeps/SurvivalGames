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
package io.github.m0pt0pmatt.spongesurvivalgames;

import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.SurvivalGamesCommand;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Utility methods used across the plugin. */
public final class Util {

    private Util() {

    }

    /**
     * Create a {@link Map.Entry} from a {@link SurvivalGamesCommand}, where the key is the {@link SurvivalGamesCommand}'s aliases.
     * This is useful when adding child commands.
     * @param command The CommandCallable.
     * @return A single {@link Map.Entry}
     */
    public static Map.Entry<List<String>, ? extends CommandCallable> toEntry(
            SurvivalGamesCommand command) {
        return new AbstractMap.SimpleImmutableEntry<>(
                command.getAliases(),
                toCommandCallable(command));
    }

    /**
     * Creates a {@link CommandCallable} from a {@link SurvivalGamesCommand}.
     * @param command the {@link SurvivalGamesCommand}.
     * @return a {@link CommandCallable}.
     */
    static CommandCallable toCommandCallable(SurvivalGamesCommand command) {

        CommandSpec.Builder builder = CommandSpec.builder();
        builder.executor(command);
        if (command.getChildren().size() > 0) {
            // This is a parent command.
            builder.children(command.getChildren());
        } else {
            // This is a leaf command.
            builder.arguments(command.getArguments());
        }

        builder.permission(command.getPermission());
        return builder.build();
    }

    /**
     * Throws a {@link CommandException} if a given {@link Optional} is empty.
     * @param optional The optional.
     * @param name A name to give the Optional in case it it empty.
     * @param <T> The type of the Optional.
     * @return The value of the Optional.
     * @throws CommandException If the Optional is empty.
     */
    public static <T> T getOrThrow(Optional<T> optional, Object name) throws CommandException {
        return optional.orElseThrow(() -> new CommandException(
                Text.of(TextColors.DARK_RED, "No value found", ": ", TextColors.BLUE, name)));
    }

    /**
     * Throws a {@link CommandException} if a given {@link Optional} is empty.
     * @param optional The optional.
     * @param name A name to give the Optional in case it it empty.
     * @param value A value to display.
     * @param <T> The type of the Optional.
     * @return The value of the Optional.
     * @throws CommandException If the Optional is empty.
     */
    public static <T> T getOrThrow(Optional<T> optional, Object name, Object value) throws CommandException {
        return optional.orElseThrow(() -> new CommandException(
                Text.of(TextColors.DARK_RED, "No ", TextColors.BLUE, name, ": ", TextColors.BLUE, value)));
    }

    /**
     * Throws a {@link CommandException} if a argument is missing.
     * @param args The command arguments.
     * @param key The key of the argument to return
     * @return The argument if it exists.
     * @throws CommandException If the argument doesn't exist.
     */
    public static Object getOrThrow(CommandContext args, Text key) throws CommandException {
        return args.getOne(key).orElseThrow(() -> new CommandException(
                Text.of(TextColors.DARK_RED, "Argument not found", ": ", TextColors.BLUE, key)));
    }

    /**
     * Sends a success message.
     * @param source The source to send the message to.
     * @param message The message to send.
     */
    public static void sendSuccess(CommandSource source, Object message) {
        source.sendMessage(Text.of(TextColors.GREEN, message));
    }

    /**
     * Sends a success message.
     * @param source The source to send the message to.
     * @param message The message to send.
     * @param object A value to be printed with the message.
     */
    public static void sendSuccess(CommandSource source, Object message, Object object) {
        source.sendMessage(Text.of(TextColors.GREEN, message, ": ", TextColors.BLUE, object));
    }

    /**
     * Sends an error message.
     * @param source The source to send the message to.
     * @param message The message to send.
     */
    public static void sendError(CommandSource source, String message) {
        source.sendMessage(Text.of(TextColors.DARK_RED, message));
    }

    /**
     * Sends an error message.
     * @param source The source to send the message to.
     * @param message The message to send.
     * @param object A value to be printed with the message.
     */
    public static void sendError(CommandSource source, Object message, Object object) {
        source.sendMessage(Text.of(TextColors.DARK_RED, message, ": ", TextColors.BLUE, object));
    }
}
