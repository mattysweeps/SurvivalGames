package io.github.m0pt0pmatt.survivalgames.scoreboard;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.scoreboard.Scoreboard;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ScoreboardRepository {

    private ScoreboardRepository() {

    }

    private static final Map<SurvivalGame, Scoreboard> MAP = new ConcurrentHashMap<>();

    public static void put(SurvivalGame survivalGame, Scoreboard scoreboard) {
        checkNotNull(survivalGame, "survivalGame");

        MAP.put(survivalGame, scoreboard);
    }

    public static Optional<Scoreboard> get(SurvivalGame survivalGame) {
        return Optional.ofNullable(MAP.get(survivalGame));
    }

    public static Optional<Scoreboard> remove(SurvivalGame survivalGame) {
        return Optional.ofNullable(MAP.remove(survivalGame));
    }
}
