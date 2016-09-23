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

import static com.google.common.base.Preconditions.checkNotNull;
import static io.github.m0pt0pmatt.spongesurvivalgames.Util.getOrThrow;
import static io.github.m0pt0pmatt.spongesurvivalgames.Util.sendSuccess;

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.spongesurvivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

public abstract class BlockRayCommand extends BaseCommand {

    private final BiConsumer<SurvivalGame, Location<World>> setter;
    private final Text message;

    protected BlockRayCommand(
            List<String> aliases,
            String permission,
            BiConsumer<SurvivalGame, Location<World>> setter,
            Text message) {
        super(aliases, permission, GenericArguments.seq(
                SurvivalGameCommandElement.getInstance(),
                GenericArguments.optional(GenericArguments.vector3d(CommandKeys.VECTOR))),
                Collections.emptyMap());
        this.setter = checkNotNull(setter, "setter");
        this.message = checkNotNull(message, "message");
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public final CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) getOrThrow(args, CommandKeys.SURVIVAL_GAME);

        Location<World> location;

        if (args.hasAny(CommandKeys.VECTOR)) {
            String worldName = getOrThrow(survivalGame.getConfig().getWorldName(), CommandKeys.WORLD_NAME);
            World world = getOrThrow(Sponge.getServer().getWorld(worldName), CommandKeys.WORLD);
            location = world.getLocation((Vector3d) getOrThrow(args, CommandKeys.VECTOR));
        } else {
            location = getLocation(src);
        }

        try {
            setter.accept(survivalGame, location);
        } catch (RuntimeException e) {
            throw new CommandException(Text.of("Error: " + e.getMessage()), e);
        }

        sendSuccess(src, message, location.getBlockPosition());
        return CommandResult.success();
    }

    private Location<World> getLocation(CommandSource commandSource) throws CommandException {

        if (!(commandSource instanceof Player)) {
            throw new CommandException(Text.of("Only players can execute this command"));
        }

        BlockRay<World> blockRay = BlockRay.from((Entity) commandSource)
                .filter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1))
                .build();

        return blockRay.end()
                .orElseThrow(() -> new CommandException(Text.of("Block Ray Didn't Hit")))
                .getLocation();
    }
}
