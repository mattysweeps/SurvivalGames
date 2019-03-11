package io.github.m0pt0pmatt.survivalgames.schedule;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.data.Schedule;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ScheduleRepository {

    private static final Map<SurvivalGame, ScheduleRunner> MAP = new ConcurrentHashMap<>();

    private ScheduleRepository() {

    }

    public static void schedule(SurvivalGame survivalGame, Schedule schedule) {

        // Remove old schedule
        Optional.ofNullable(MAP.get(survivalGame)).ifPresent(g -> unschedule(survivalGame));

        ScheduleRunner scheduleRunner = new ScheduleRunner(
                survivalGame,
                schedule
        );

        SurvivalGamesPlugin.ASYNC_EXECUTOR.submit(scheduleRunner);

        MAP.put(survivalGame, scheduleRunner);
    }

    public static void unschedule(SurvivalGame survivalGame) {
        Optional.ofNullable(MAP.remove(survivalGame)).ifPresent(ScheduleRunner::cancel);
    }
}
