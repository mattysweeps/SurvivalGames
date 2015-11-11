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


import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to Serialize and DeSerialize a SurvivalGameConfig
 */
public class SurvivalGameConfigSerializer {


    public void deserialize(SurvivalGameConfig baseConfig, ConfigurationSection config, boolean loadDefaults) {
        if (loadDefaults) {
            loadWithDefaults(baseConfig, config);
        }

        loadWithoutDefaults(baseConfig, config);
    }

    @SuppressWarnings("unchecked")
    public void loadWithoutDefaults(SurvivalGameConfig baseConfig, ConfigurationSection config) {

        SurvivalGameConfigBuilder builder = new SurvivalGameConfigBuilder(baseConfig);
        builder.worldName(config.getString(Fields.WORLD.getKey(), null));
        builder.exitWorld(config.getString(Fields.EXITWORLD.getKey(), null));
        builder.exitLocation(config.getVector(Fields.EXIT.getKey(), null));
        builder.centerLocation(config.getVector(Fields.CENTER.getKey(), null));
        builder.playerLimit(config.contains(Fields.PLAYERLIMIT.getKey()) ? config.getInt(Fields.PLAYERLIMIT.getKey()) : null);
        builder.countdownTime(config.contains(Fields.COUNTDOWNTIME.getKey()) ? config.getInt(Fields.COUNTDOWNTIME.getKey()) : null);
        builder.xMin(config.contains(Fields.XMIN.getKey()) ? config.getInt(Fields.XMIN.getKey()) : null);
        builder.xMax(config.contains(Fields.XMAX.getKey()) ? config.getInt(Fields.XMAX.getKey()) : null);
        builder.zMin(config.contains(Fields.ZMIN.getKey()) ? config.getInt(Fields.ZMIN.getKey()) : null);
        builder.zMax(config.contains(Fields.ZMIN.getKey()) ? config.getInt(Fields.ZMAX.getKey()) : null);
        builder.deathmatchRadius(config.contains(Fields.DEATHMATCHRADIUS.getKey()) ? config.getInt(Fields.DEATHMATCHRADIUS.getKey()) : null);
        builder.deathmatchTime(config.contains(Fields.DEATHMATCHTIME.getKey()) ? config.getInt(Fields.DEATHMATCHTIME.getKey()) : null);
        builder.chestMidpoint(config.contains(Fields.CHEST_MIDPOINT.getKey()) ? config.getDouble(Fields.CHEST_MIDPOINT.getKey()) : null);
        builder.chestRange(config.contains(Fields.CHEST_MIDPOINT.getKey()) ? config.getDouble(Fields.CHEST_RANGE.getKey()) : (Double) Fields.CHEST_RANGE.getDefault());

        List<Vector> vectorList = (List<Vector>) config.getList(Fields.SPAWNS.getKey(), new LinkedList<Vector>());
        if (!vectorList.isEmpty()) {
            vectorList.forEach(builder::addSpawn);
        }

        for (Object item : config.getList(Fields.LOOT.getKey(), new LinkedList<Loot>())) {
            if (!(item instanceof Loot)) {
                BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Error encountered when parsing loot!"
                        + " List item not a LOOT object! Skipping...");
                continue;
            }
            builder.addLoot((Loot) item);
        }

        vectorList = (List<Vector>) config.getList(Fields.CHESTS.getKey(), (List<?>) Fields.CHESTS.getDefault());
        if (!vectorList.isEmpty()) {
            vectorList.forEach(builder::addChestLocation);
        }

        builder.build();
    }

    @SuppressWarnings("unchecked")
    public void loadWithDefaults(SurvivalGameConfig baseConfig, ConfigurationSection config) {

        SurvivalGameConfigBuilder builder = new SurvivalGameConfigBuilder(baseConfig);
        builder.worldName(config.getString(Fields.WORLD.getKey(), (String) Fields.WORLD.getDefault()));
        builder.exitWorld(config.getString(Fields.EXITWORLD.getKey(), (String) Fields.EXITWORLD.getDefault()));
        builder.exitLocation(config.getVector(Fields.EXIT.getKey(), (Vector) Fields.EXIT.getDefault()));
        builder.centerLocation(config.getVector(Fields.CENTER.getKey(), (Vector) Fields.CENTER.getDefault()));
        builder.playerLimit(config.getInt(Fields.PLAYERLIMIT.getKey(), (Integer) Fields.PLAYERLIMIT.getDefault()));
        builder.countdownTime(config.getInt(Fields.COUNTDOWNTIME.getKey(), (Integer) Fields.COUNTDOWNTIME.getDefault()));
        builder.xMin(config.getInt(Fields.XMIN.getKey(), (Integer) Fields.XMIN.getDefault()));
        builder.xMax(config.getInt(Fields.XMAX.getKey(), (Integer) Fields.XMAX.getDefault()));
        builder.zMin(config.getInt(Fields.ZMIN.getKey(), (Integer) Fields.ZMIN.getDefault()));
        builder.zMax(config.getInt(Fields.ZMAX.getKey(), (Integer) Fields.ZMAX.getDefault()));
        builder.deathmatchRadius(config.getInt(Fields.DEATHMATCHRADIUS.getKey(), (Integer) Fields.DEATHMATCHRADIUS.getDefault()));
        builder.deathmatchTime(config.getInt(Fields.DEATHMATCHTIME.getKey(), (Integer) Fields.DEATHMATCHTIME.getDefault()));
        builder.chestMidpoint(config.getDouble(Fields.CHEST_MIDPOINT.getKey(), (Double) Fields.CHEST_MIDPOINT.getDefault()));
        builder.chestRange(config.getDouble(Fields.CHEST_RANGE.getKey(), (Double) Fields.CHEST_RANGE.getDefault()));

        List<Vector> vectorList = (List<Vector>) config.getList(Fields.SPAWNS.getKey(), (List<Vector>) Fields.SPAWNS.getDefault());
        if (!vectorList.isEmpty()) {
            vectorList.forEach(builder::addSpawn);
        }

        for (Object item : config.getList(Fields.LOOT.getKey(), (List<?>) Fields.LOOT.getDefault())) {
            if (!(item instanceof Loot)) {
                BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Error encountered when parsing loot!"
                        + " List item not a LOOT object! Skipping...");
                continue;
            }
            builder.addLoot((Loot) item);
        }

        vectorList = (List<Vector>) config.getList(Fields.CHESTS.getKey(), (List<?>) Fields.CHESTS.getDefault());
        if (!vectorList.isEmpty()) {
            vectorList.forEach(builder::addChestLocation);
        }

        builder.build();
    }

    public YamlConfiguration serialize(SurvivalGameConfig obj) {

        YamlConfiguration config = new YamlConfiguration();

        config.set(Fields.WORLD.getKey(), obj.getWorldName().get());
        config.set(Fields.EXITWORLD.getKey(), obj.getExitWorld().get());
        config.set(Fields.EXIT.getKey(), obj.getExit().get());
        config.set(Fields.CENTER.getKey(), obj.getCenter().get());
        config.set(Fields.PLAYERLIMIT.getKey(), obj.getPlayerLimit().get());
        config.set(Fields.COUNTDOWNTIME.getKey(), obj.getCountdownTime().get());
        config.set(Fields.SPAWNS.getKey(), new ArrayList<>(obj.getSpawns()));
        config.set(Fields.CHEST_MIDPOINT.getKey(), obj.getChestMidpoint().get());
        config.set(Fields.CHEST_RANGE.getKey(), obj.getChestRange().get());
        config.set(Fields.LOOT.getKey(), obj.getLoot());
        config.set(Fields.XMIN.getKey(), obj.getXMin().get());
        config.set(Fields.XMAX.getKey(), obj.getXMax().get());
        config.set(Fields.ZMIN.getKey(), obj.getZMin().get());
        config.set(Fields.ZMAX.getKey(), obj.getZMax().get());
        config.set(Fields.DEATHMATCHRADIUS.getKey(), obj.getDeathmatchRadius().get());
        config.set(Fields.DEATHMATCHTIME.getKey(), obj.getDeathmatchTime().get());
        config.set(Fields.CHESTS.getKey(), obj.getChestLocations());

        return config;
    }

    private enum Fields {

        WORLD("world", ""),
        PLAYERLIMIT("playerLimit", 16),
        COUNTDOWNTIME("countdownTime", 30),
        EXITWORLD("exitWorld", ""),
        EXIT("exit", new Vector(0, 0, 0)),
        CENTER("center", new Vector(0, 0, 0)),
        SPAWNS("spawns", new LinkedList<Vector>()),
        CHEST_MIDPOINT("chest.midpoint", 0.0),
        CHEST_RANGE("chest.range", 0.0),
        LOOT("loot", new LinkedList<Loot>()),
        XMIN("xmin", 0),
        XMAX("xmax", 0),
        ZMIN("zmin", 0),
        ZMAX("zmax", 0),
        DEATHMATCHRADIUS("deathmatchRadius", 100),
        DEATHMATCHTIME("deathmatchTime", 60),
        CHESTS("chests", new LinkedList<Vector>());

        private final String key;

        private final Object def;

        Fields(String key, Object def) {
            this.key = key;
            this.def = def;
        }

        public String getKey() {
            return key;
        }

        public Object getDefault() {
            return def;
        }
    }
}
