package io.github.m0pt0pmatt.spongesurvivalgames.exceptions;

public class NoPlayerException extends SurvivalGameException {

    private String desc;

    public NoPlayerException(String description) {
        this.desc = description;
    }

    @Override
    public String getDescription() {
        return desc;
    }

}
