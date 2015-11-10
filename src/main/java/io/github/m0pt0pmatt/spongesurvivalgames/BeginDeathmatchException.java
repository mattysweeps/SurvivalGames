package io.github.m0pt0pmatt.spongesurvivalgames;


import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.SurvivalGameException;

public class BeginDeathmatchException extends SurvivalGameException {
    @Override
    public String getDescription() {
        return "Error while beginning the deathmatch";
    }
}
