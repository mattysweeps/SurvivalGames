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

package io.github.m0pt0pmatt.survivalgames.task.player;

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.game.PlayerRestorer;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.world.World;

/** Spawns spectators. */
public class SpawnSpectatorsTask extends SpectatorTask {

    private static final AbstractPlayerTask INSTANCE = new SpawnSpectatorsTask();

    private List<Vector3d> spawnPoints;
    private Vector3d centerVector;

    @Override
    public void execute(SurvivalGame survivalGame, Player player) throws TextMessageException {
        String worldName =
                getOrThrow(survivalGame.getConfig().getWorldName(), CommandKeys.WORLD_NAME);
        World world = getOrThrow(Sponge.getServer().getWorld(worldName), CommandKeys.WORLD);

        if (!spawnPoints.isEmpty()) {
            Vector3d spawnPoint = spawnPoints.remove(0);

            spawnPlayer(survivalGame,
                    player,
                    world,
                    new Vector3d(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ()),
                    new Vector3d(centerVector.getX(), centerVector.getY(), centerVector.getZ()));
        }
    }

    private static void spawnPlayer(SurvivalGame survivalGame,
            Player player, World world, Vector3d spawnPoint, Vector3d centerVector) {

        survivalGame.getPlayerSnapshots().put(player.getUniqueId(), new PlayerRestorer(player));
        player.setLocation(world.getLocation(spawnPoint).add(new Vector3d(0.5, 10, 0.5)));
        player.lookAt(centerVector);
        player.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
    }

    @Override
    protected void before(SurvivalGame survivalGame) throws TextMessageException {
        spawnPoints = new ArrayList<>(survivalGame.getConfig().getSpawnPoints());
        centerVector =
                getOrThrow(survivalGame.getConfig().getCenterVector(), CommandKeys.CENTER_VECTOR);
    }

    public static AbstractPlayerTask getInstance() {
        return INSTANCE;
    }
}
