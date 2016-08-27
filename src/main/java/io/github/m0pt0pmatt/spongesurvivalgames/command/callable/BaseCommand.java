/*
 *  QuestManager: An RPG plugin for the Bukkit API.
 *  Copyright (C) 2015-2016 Github Contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.m0pt0pmatt.spongesurvivalgames.command.callable;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.m0pt0pmatt.spongesurvivalgames.command.ArgumentProvider;
import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandRepository;
import io.github.m0pt0pmatt.spongesurvivalgames.command.argument.CommandArgument;
import io.github.m0pt0pmatt.spongesurvivalgames.command.data.CommandValue;
import io.github.m0pt0pmatt.spongesurvivalgames.command.data.CommandValues;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.SurvivalGamesCommand;

import static io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin.LOGGER;

public class BaseCommand implements CommandCallable {

    private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");

    private final Map<String, Map<Integer, List<CommandArgument<?>>>> commandArgumentLists;

    public BaseCommand() {
        commandArgumentLists = new HashMap<>();
    }

    @Override
    public @Nonnull CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments) throws CommandException {
        List<String> args = WHITE_SPACE.splitAsStream(arguments).collect(Collectors.toList());

        if(args.size() == 0) {
            source.sendMessage(Text.of("bro"));
            throw new CommandException(Text.of("No Arguments"));
        }

        String commandName = args.remove(0);
        SurvivalGamesCommand command = CommandRepository.get(commandName)
                .orElseThrow(() -> new CommandException(Text.of("Unknown Command: ", commandName)));

        return command.execute(source, buildArguments(command, args));
    }

    public void registerCommands() {
        commandArgumentLists.clear();
        CommandRepository.values().forEach(command -> commandArgumentLists.put(command.getName(), profileCommandArguments(command)));
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, List<CommandArgument<?>>> profileCommandArguments(SurvivalGamesCommand command) {
        Map<Integer, List<CommandArgument<?>>> argumentsByLength = new HashMap<>();

        for (Method method: command.getClass().getMethods()) {
            if (method.getAnnotation(ArgumentProvider.class) != null) {
                Class<?> returnType = method.getReturnType();
                if (returnType.isAssignableFrom(List.class) && method.getParameterCount() == 0) {
                    try {
                        List list = (List) method.invoke(command);
                        if (list == null) {
                            LOGGER.error("Return list was null for @ArgumentProvider method: " + command.getClass().getName() + "#" + method.getName());
                        } else if (list.size() != 0 && (!(list.get(0) instanceof CommandArgument))) {
                            LOGGER.error("Return type isn't List<CommandArgument> for @ArgumentProvider method: " + command.getClass().getName() + "#" + method.getName());
                        } else if (argumentsByLength.containsKey(list.size())) {
                            LOGGER.error("Command " + command.getClass().getName() + " has more than one @ArgumentProvider with the same length.");
                        } else {
                            argumentsByLength.put(list.size(), list);
                        }
                    } catch (IllegalAccessException e) {
                        LOGGER.error("Cannot access @ArgumentProvider method: " + command.getClass().getName() + "#" + method.getName());
                    } catch (InvocationTargetException e) {
                        LOGGER.error("Error while profiling @ArgumentProvider method: " + command.getClass().getName() + "#" + method.getName());
                    }
                }
            }
        }

        return argumentsByLength;
    }

    private CommandValues buildArguments(SurvivalGamesCommand command, List<String> arguments) throws CommandException {

        if (!commandArgumentLists.containsKey(command.getName())) {
            commandArgumentLists.put(command.getName(), profileCommandArguments(command));
        }

        Map<Integer, List<CommandArgument<?>>> a = commandArgumentLists.get(command.getName());
        if (!a.containsKey(arguments.size())) {
            throw new CommandException(Text.of("Wrong number of arguments."));
        }

        List<CommandArgument<?>> args = a.get(arguments.size());

        CommandValues value = new CommandValues();

        Iterator<CommandArgument<?>> commandIterator = args.iterator();
        Iterator<String> argIterator = arguments.iterator();
        while (commandIterator.hasNext() && argIterator.hasNext()) {
            CommandArgument<?> c = commandIterator.next();
            String arg = argIterator.next();
            addValue(c, arg, value);
        }

        return value;
    }

    private <T> void addValue(CommandArgument<T> argument, String value, CommandValues values) {
        argument.getTabCompleter().getValue(value).ifPresent(v -> values.put(argument, new CommandValue<>(value, v)));
    }


    @Override
    public @Nonnull List<String> getSuggestions(@Nonnull CommandSource source,
                                                @Nonnull String argumentString,
                                                @Nullable Location<World> targetPosition)
            throws CommandException {

        // Break the command by whitespace
        List<String> argumentValues =
                WHITE_SPACE.splitAsStream(argumentString).collect(Collectors.toList());

        // If there's nothing, then return the full list of commands.
        if(argumentValues.size() == 0) {
            return new ArrayList<>(commandArgumentLists.keySet());
        }

        // Get the command name.
        String commandName = argumentValues.remove(0);

        // If there's not a match, return the commands which start with the given command name.
        if (!commandArgumentLists.containsKey(commandName)) {
            return commandArgumentLists.keySet().stream()
                    .filter(s -> s.startsWith(commandName))
                    .collect(Collectors.toList());
        }

        // Whether or not the last character in the argument string was whitespace
        boolean endsWithWhiteSpace =
                Character.isWhitespace(argumentString.charAt(argumentString.length() - 1));

        // If there's only a command, append a space.
        if (argumentValues.size() == 0 && !endsWithWhiteSpace) {
            return Collections.singletonList(commandName + " ");
        }

        // Get the possible argument list executors
        Map<Integer, List<CommandArgument<?>>> argumentsByLength =
                commandArgumentLists.get(commandName);

        // The last word is the word which needs to be completed.
        String lastWord;
        if (argumentValues.size() == 0 || endsWithWhiteSpace) {
            lastWord = "";
        } else {
            lastWord = argumentValues.get(argumentValues.size() - 1);
        }

        // The index determines which argument completer to get.
        int index;
        if (endsWithWhiteSpace) {
            index = argumentValues.size();
        } else {
            index = argumentValues.size() - 1;
        }

        List<String> matches = argumentsByLength.entrySet().stream()
                .filter(e -> e.getKey() >= index + 1)
                .map(e -> e.getValue().get(index))
                .map(CommandArgument::getTabCompleter)
                .flatMap(t -> t.getSuggestions(lastWord).stream())
                .collect(Collectors.toList());

        if (matches.size() == 1 && matches.get(0).equals(lastWord)) {
            return Collections.singletonList(lastWord + " ");
        }

        return matches;
    }

    @Override
    public boolean testPermission(@Nonnull CommandSource source) {
        return true;
    }

    @Override
    public @Nonnull Optional<Text> getShortDescription(@Nonnull CommandSource source) {
        return Optional.empty();
    }

    @Override
    public @Nonnull Optional<Text> getHelp(@Nonnull CommandSource source) {
        return Optional.empty();
    }

    @Override
    public @Nonnull Text getUsage(@Nonnull CommandSource source) {
        return Text.of("usage");
    }

}
