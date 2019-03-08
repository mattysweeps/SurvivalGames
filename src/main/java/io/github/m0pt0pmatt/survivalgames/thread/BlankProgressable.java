package io.github.m0pt0pmatt.survivalgames.thread;

public class BlankProgressable implements Progressable {

    private final Runnable runnable;

    public BlankProgressable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public String getProgress() {
        return null;
    }

    @Override
    public void run() {
        runnable.run();
    }
}
