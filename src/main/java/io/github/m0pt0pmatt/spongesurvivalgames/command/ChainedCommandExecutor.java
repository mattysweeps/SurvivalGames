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
package io.github.m0pt0pmatt.spongesurvivalgames.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChainedCommandExecutor implements CommandExecutor {

    private final List<CommandExecutor> commandExecutors;

    public ChainedCommandExecutor(List<CommandExecutor> commandExecutors) {
        this.commandExecutors = checkNotNull(commandExecutors);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        int totalSuccessCount = 0;
        int totalAffectedBlocks = 0;
        int totalAffectedEntities = 0;
        int totalAffectedItems = 0;

        for (CommandExecutor commandExecutor: commandExecutors) {

            CommandResult result = commandExecutor.execute(src, args);

            Optional<Integer> successCount = result.getSuccessCount();
            Optional<Integer> affectedBlocks = result.getAffectedBlocks();
            Optional<Integer> affectedEntities = result.getAffectedEntities();
            Optional<Integer> affectedItems = result.getAffectedItems();

            if (affectedBlocks.isPresent()) {
                totalAffectedBlocks += affectedBlocks.get();
            }

            if (affectedEntities.isPresent()) {
                totalAffectedEntities += affectedEntities.get();
            }

            if (affectedItems.isPresent()) {
                totalAffectedItems += affectedItems.get();
            }

            if (!successCount.isPresent()) {

                return CommandResult.builder()
                        .affectedBlocks(totalAffectedBlocks)
                        .affectedEntities(totalAffectedEntities)
                        .affectedItems(totalAffectedItems)
                        .build();
            }

            totalSuccessCount += successCount.get();
        }

        return CommandResult.builder()
                .successCount(totalSuccessCount)
                .affectedBlocks(totalAffectedBlocks)
                .affectedEntities(totalAffectedEntities)
                .affectedItems(totalAffectedItems)
                .build();
    }
}
