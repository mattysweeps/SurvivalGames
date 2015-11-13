package io.github.m0pt0pmatt.spongesurvivalgames.backups;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupTaker extends BukkitRunnable {

    private static final int ticksPerSecond = 20;

    private static final DateFormat format = new SimpleDateFormat("MMMMd_hh-mm-ss");

    @Override
    public void run() {
        BukkitSurvivalGamesPlugin.plugin.getLogger().info("Scheduling a backup of [" + game.getID() + "]...");
        File backups = new File(BukkitSurvivalGamesPlugin.plugin.getDataFolder(), "Backups");

        if (!backups.isDirectory()) {
            BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Found file named 'Backup', but need name "
                    + "for backup folders!\nFile will be deleted!");
            if (!backups.delete()) {
                Bukkit.getLogger().warning("Unable to delete file: " + backups.getName());
            }
        }

        if (!backups.exists()) {
            if (!backups.mkdirs()) {
                Bukkit.getLogger().warning("Unable to create backups directory");
            }
        }

        File outFile = new File(backups,
                "Backup[" + game.getID() + "]_" + format.format(new Date()) + ".yml");

        Backup backup = new Backup(game);

        BackupDumper.dumpBackup(backup, outFile);
    }

    private final SurvivalGame game;

    public BackupTaker(SurvivalGame game) {
        this.game = game;
        int ticks = Math.round(SurvivalGame.backupTime * ticksPerSecond);
        this.runTaskTimer(BukkitSurvivalGamesPlugin.plugin, ticks, ticks);
    }

}
