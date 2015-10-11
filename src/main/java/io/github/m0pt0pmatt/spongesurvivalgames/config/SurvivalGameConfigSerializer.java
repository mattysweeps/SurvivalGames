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

package io.github.m0pt0pmatt.spongesurvivalgames.config;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import paulscode.sound.Vector3D;

public class SurvivalGameConfigSerializer implements TypeSerializer<SurvivalGameConfig> {
    @Override
    public SurvivalGameConfig deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        SurvivalGameConfigBuilder builder = new SurvivalGameConfigBuilder();

        try{
            builder = builder
                    .worldName(value.getNode("world").getString(""))
                    .playerLimit(value.getNode("playerLimit").getInt())
                    .countdownTime(value.getNode("countdownTime").getInt());

            ConfigurationNode exitNode = value.getNode("exit");
            builder = builder
                    .exitWorld(exitNode.getNode("world").getString(""))
                    .exitLocation(new Vector3d(
                            exitNode.getNode("X").getDouble(),
                            exitNode.getNode("Y").getDouble(),
                            exitNode.getNode("Z").getDouble()
                    ));

            ConfigurationNode centerNode = value.getNode("center");
            builder = builder
                    .centerLocation(new Vector3d(
                            centerNode.getNode("X").getDouble(),
                            centerNode.getNode("Y").getDouble(),
                            centerNode.getNode("Z").getDouble()
                    ));

            ConfigurationNode spawnsNode = value.getNode("spawns");
            while (spawnsNode.hasListChildren()){
                builder = builder
                        .addSpawn(new Vector3d(
                                spawnsNode.getNode("X").getDouble(),
                                spawnsNode.getNode("Y").getDouble(),
                                spawnsNode.getNode("Z").getDouble()
                        ));
                spawnsNode = spawnsNode.getAppendedNode();
            }

        } catch (Exception e){
            SpongeSurvivalGamesPlugin.logger.error("Error loading config");
        }

        return builder.build();
    }

    @Override
    public void serialize(TypeToken<?> type, SurvivalGameConfig obj, ConfigurationNode value) throws ObjectMappingException {
        if (obj.getWorldName().isPresent()) value.getNode("world").setValue(obj.getWorldName().get());

        ConfigurationNode exitNode = value.getNode("exit");
        if (obj.getExitWorld().isPresent()) exitNode.getNode("world").setValue(obj.getExitWorld().get());
        if (obj.getExit().isPresent()) {
            exitNode.getNode("X").setValue(obj.getExit().get().getX());
            exitNode.getNode("Y").setValue(obj.getExit().get().getY());
            exitNode.getNode("Z").setValue(obj.getExit().get().getZ());
        }

        ConfigurationNode centerNode = value.getNode("center");
        if (obj.getCenter().isPresent()){
            centerNode.getNode("X").setValue(obj.getCenter().get().getX());
            centerNode.getNode("Y").setValue(obj.getCenter().get().getY());
            centerNode.getNode("Z").setValue(obj.getCenter().get().getZ());
        }

        if (obj.getPlayerLimit().isPresent()) value.getNode("playerLimit").setValue(obj.getPlayerLimit().get());
        if (obj.getCountdownTime().isPresent()) value.getNode("countdownTime").setValue(obj.getCountdownTime().get());

        ConfigurationNode spawnsNode = value.getNode("spawns");
        for (Vector3d spawn: obj.getSpawns()) {
            ConfigurationNode spawnNode = spawnsNode.getAppendedNode();
            spawnNode.getNode("X").setValue(spawn.getX());
            spawnNode.getNode("Y").setValue(spawn.getY());
            spawnNode.getNode("Z").setValue(spawn.getZ());
        }
    }
}
