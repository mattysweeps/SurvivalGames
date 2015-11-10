package io.github.m0pt0pmatt.spongesurvivalgames.exceptions;

public class StopException extends SurvivalGameException {
    @Override
    public String getDescription() {
        return "Error while stopping";
    }
}
