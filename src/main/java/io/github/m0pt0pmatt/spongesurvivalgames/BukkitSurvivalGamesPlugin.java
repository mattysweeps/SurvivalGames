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

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandKeywords;
import io.github.m0pt0pmatt.spongesurvivalgames.events.PlayerDeathEventListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * SpongeSurvivalGames Sponge Plugin
 */
public class BukkitSurvivalGamesPlugin extends JavaPlugin {

    public static Map<String, SurvivalGame> survivalGameMap;
    public static BukkitSurvivalGamesPlugin plugin;
    private final CommandExecutor commandExecutor = new SurvivalGamesCommandExecutor();
    private final TabCompleter tabCompleter = new SurvivalGamesTabCompleter();

    public BukkitSurvivalGamesPlugin() {
        BukkitSurvivalGamesPlugin.plugin = this;
        BukkitSurvivalGamesPlugin.survivalGameMap = new HashMap<>();
    }

    public static Set<Player> getPlayers(Set<UUID> playerUUIDs) {
        return playerUUIDs.stream()
                .map(uuid -> Bukkit.getServer().getPlayer(uuid))
                .filter((player) -> player != null)
                .collect(Collectors.toSet());
    }

    public Map<String, SurvivalGame> getSurvivalGameMap() {
        return survivalGameMap;
    }

    @Override
    public void onEnable() {
        survivalGameMap.clear();
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEventListener(), this);
        Bukkit.getLogger().info("Sponge Survival Games Plugin Enabled");
        getCommand(CommandKeywords.SSG).setExecutor(commandExecutor);
        getCommand(CommandKeywords.SSG).setTabCompleter(tabCompleter);
    }

}

