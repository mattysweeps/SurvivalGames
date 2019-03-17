package io.github.m0pt0pmatt.survivalgames.task;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.scoreboard.ScoreboardRepository;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.TextMessageException;

public class AdjustScoreboardForStartTask implements Task {

    private static final AdjustScoreboardForStartTask INSTANCE = new AdjustScoreboardForStartTask();

    private AdjustScoreboardForStartTask() {

    }

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {
        ScoreboardRepository.get(survivalGame)
                .flatMap(s -> s.getObjective("ssg-" + survivalGame.getName()))
                .ifPresent(o -> o.setDisplayName(Text.of("Remaining Players")));
    }

    public static AdjustScoreboardForStartTask getInstance() {
        return INSTANCE;
    }
}
