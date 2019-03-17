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

package io.github.m0pt0pmatt.survivalgames.game;

import com.google.common.collect.ImmutableSet;
import io.github.m0pt0pmatt.survivalgames.Util;
import io.github.m0pt0pmatt.survivalgames.data.GameConfig;
import io.github.m0pt0pmatt.survivalgames.scoreboard.ScoreboardRepository;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/** Represents a Survival Game. */
public class SurvivalGame {

    SurvivalGameState state;
    SurvivalGameRunningState runningState;

    private final String name;
    private final GameConfig config;
    private final Set<UUID> playerUUIDs;
    private final Set<UUID> spectatorUUIDs;
    private final Set<CommandBlock> commandBlocks;
    private final Set<UUID> activeMobSpawners;
    private final Set<UUID> activeEventIntervals;
    private final Map<UUID, PlayerRestorer> playerSnapshots;
    private final MessageChannel messageChannel;

    public SurvivalGame(String name, GameConfig config) {
        this.name = checkNotNull(name);
        state = SurvivalGameState.STOPPED;
        runningState = SurvivalGameRunningState.STOPPED;
        this.config = checkNotNull(config);
        playerUUIDs = new HashSet<>();
        spectatorUUIDs = new HashSet<>();
        commandBlocks = new HashSet<>();
        activeMobSpawners = new HashSet<>();
        activeEventIntervals = new HashSet<>();
        playerSnapshots = new HashMap<>();
        messageChannel = MessageChannel.combined(MessageChannel.TO_CONSOLE, () -> Stream.of(playerUUIDs, spectatorUUIDs)
                .flatMap(Collection::stream)
                .map(uuid -> Sponge.getServer().getPlayer(uuid))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
    }

    public SurvivalGame(String name) {
        this(name, new GameConfig());
    }

    public String getName() {
        return name;
    }

    public SurvivalGameState getState() {
        return state;
    }

    public SurvivalGameRunningState getRunningState() {
        return runningState;
    }

    public GameConfig getConfig() {
        return config;
    }

    public void forEachPlayer(Consumer<? super UUID> action) {
        playerUUIDs.forEach(action);
    }

    public void addPlayer(Player player) {
        playerUUIDs.add(player.getUniqueId());
        sendMessage(Text.of(player.getName(), " joined the game"));
        ScoreboardRepository.get(this).ifPresent(s -> {
            s.getObjective("ssg-" + getName()).ifPresent(o -> o.getOrCreateScore(Text.of(player.getName())).setScore(0));
            player.setScoreboard(s);
        });
    }

    public void clearPlayerUUIDs() {
        playerUUIDs.clear();
    }

    public void removePlayer(UUID uuid) {
        playerUUIDs.remove(uuid);
    }

    public int getPlayerCount() {
        return playerUUIDs.size();
    }

    public boolean containsPlayer(UUID uuid) {
        return playerUUIDs.contains(uuid);
    }

    public void forEachSpectator(Consumer<? super UUID> action) {
        spectatorUUIDs.forEach(action);
    }

    public void addSpectator(Player player) {
        spectatorUUIDs.add(player.getUniqueId());
        messageChannel.send(Text.of(player.getName(), " now spectating the game"));
    }

    public boolean containsSpectator(UUID uuid) {
        return spectatorUUIDs.contains(uuid);
    }

    public void removeSpectator(UUID uuid) {
        spectatorUUIDs.remove(uuid);
    }

    public void clearSpectatorUUIDs() {
        spectatorUUIDs.clear();
    }

    public Set<CommandBlock> getCommandBlocks() {
        return commandBlocks;
    }

    public Set<UUID> getActiveMobSpawners() {
        return activeMobSpawners;
    }

    public Set<UUID> getActiveEventIntervals() {
        return activeEventIntervals;
    }

    public Map<UUID, PlayerRestorer> getPlayerSnapshots() {
        return playerSnapshots;
    }

    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    public void sendMessage(Text text) {
        messageChannel.send(Text.of(Util.gamePrefix(this), TextColors.GRAY, text));
    }

    public Text printPlayers() {
        return Text.joinWith(
                Text.of('\n'),
                playerUUIDs
                        .stream()
                        .map(id -> Sponge.getServer().getPlayer(id))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(Player::getName)
                        .map(Text::of)
                        .collect(Collectors.toList()));
    }
}
