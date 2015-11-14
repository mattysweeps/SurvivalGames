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

package io.github.m0pt0pmatt.spongesurvivalgames.backups;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfigSerializer;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.SurvivalGameException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A backup of a game.
 *
 * @author Skyler
 */
public class Backup implements ConfigurationSerializable {

    private SurvivalGameConfig config;
    private SurvivalGameState gameState;
    private final Map<UUID, PlayerRecord> players = new HashMap<>();

    private Backup() {
    }

    public Backup(SurvivalGame game) {
        this.gameState = game.getState();
        this.config = new SurvivalGameConfig(game.getConfig());

        for (Player player : BukkitSurvivalGamesPlugin.getPlayers(game.getPlayerUUIDs())) {
            players.put(player.getUniqueId(), new PlayerRecord(player));
        }
    }

    public static void registerAliases() {
        //Register this serializable class with some aliases too
        ConfigurationSerialization.registerClass(Backup.class);
        ConfigurationSerialization.registerClass(Backup.class, "backup");
        ConfigurationSerialization.registerClass(Backup.class, "BACKUP");
        PlayerRecord.registerAliases();
    }

    public static Backup load(File inputFile) throws IOException, InvalidConfigurationException {
        YamlConfiguration config = new YamlConfiguration();

        config.load(inputFile);

        Object backup = config.get("backup");
        if (backup instanceof Backup) {
            return (Backup) backup;
        } else {
            Bukkit.getLogger().warning("Bad backup file: " + inputFile.getName());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Backup valueOf(Map<String, Object> configMap) {
        Backup backup = new Backup();
        SurvivalGameConfigSerializer serializer = new SurvivalGameConfigSerializer();
        YamlConfiguration section = new YamlConfiguration();
        section.createSection("config", (HashMap<String, Object>) configMap.get("config"));
        SurvivalGameConfig config = new SurvivalGameConfig();

        serializer.deserialize(config, section.getConfigurationSection("config"), true);
        backup.config = config;
        backup.gameState = SurvivalGameState.valueOf((String) configMap.get("state"));

        for (String key : configMap.keySet()) {
            if (!key.startsWith("player-")) {
                continue;
            }

            backup.players.put(UUID.fromString(key.substring(7)), //start at 7 to get rid of player-
                    (PlayerRecord) configMap.get(key));
        }

        return backup;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        SurvivalGameConfigSerializer serializer = new SurvivalGameConfigSerializer();
        map.put("config", serializer.serialize(config));
        map.put("state", gameState.name());

        for (UUID id : players.keySet()) {
            map.put("player-" + id.toString(), players.get(id));
        }

        return map;
    }

    /**
     * Saves the output to the provided file.<br />
     * <b>Will erase the file if it already exists before writing</b>
     *
     * @param outputFile The output file
     */
    public void save(File outputFile) {
        if (outputFile.exists()) {
            if (!outputFile.delete()) {
                Bukkit.getLogger().warning("Unable to delete file: " + outputFile.getName());
            }
        }

        BackupDumper.dumpBackup(this, outputFile);
    }

    /**
     * Creates a game and restores it to the state kept by this backup.
     *
     * @param id The id to give the new game
     * @return the game made from the backup
     */
    public SurvivalGame restore(String id) {
        SurvivalGame game = new SurvivalGame(id);
        BukkitSurvivalGamesPlugin.survivalGameMap.put(id, game);

        game.setConfig(config);
        game.setState(gameState);

        if (gameState == SurvivalGameState.RUNNING || gameState == SurvivalGameState.DEATHMATCH) {
            game.setTakeBackups(true);
        }

        for (UUID playerID : players.keySet()) {
            //try and get the online player.
            Player player = Bukkit.getPlayer(playerID);
            if (player == null) {
                BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Couldn't find player for restore: " + playerID);
                continue;
            }

            //set their inventory, position, health from record
            PlayerRecord record = players.get(playerID);

            player.setHealth(record.getPlayerHealth());
            player.setMaxHealth(record.getPlayerMaxHealth());
            player.getInventory().setContents(record.getPlayerInventory());
            player.getInventory().setArmorContents(record.getPlayerArmor());

            player.teleport(record.getLocation());

            try {
                game.addPlayer(playerID);
            } catch (SurvivalGameException e) {
                Bukkit.getLogger().warning(e.getDescription());
            }
        }

        return game;
    }

    public YamlConfiguration asConfig() {
        YamlConfiguration outcfg = new YamlConfiguration();

        outcfg.set("backup", this);

        return outcfg;
    }

}
