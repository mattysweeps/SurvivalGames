package io.github.m0pt0pmatt.spongesurvivalgames.backups;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;

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
			backups.delete();
		}
		
		if (!backups.exists()) {
			backups.mkdirs();
		}
		
		File outFile = new File(backups, 
				"Backup[" + game.getID() + "]_" + format.format(new Date()) + ".yml");
		
		Backup backup = new Backup(game);
		
		BackupDumper.dumpBackup(backup, outFile);
	}
	
	private SurvivalGame game;
	
	private int ticks;
	
	public BackupTaker(SurvivalGame game, int secondsTillBackup) {
		this.game = game;
		ticks = Math.round(secondsTillBackup * ticksPerSecond);
		
		this.runTaskTimer(BukkitSurvivalGamesPlugin.plugin, ticks, ticks);
	}
	
}
