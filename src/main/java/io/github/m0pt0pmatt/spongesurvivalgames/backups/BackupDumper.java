package io.github.m0pt0pmatt.spongesurvivalgames.backups;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;

public class BackupDumper extends BukkitRunnable {

	@Override
	public void run() {
		BukkitSurvivalGamesPlugin.plugin.getLogger().info("Starting backup dump...");
		try {
			config.save(outFile);
		} catch (IOException e) {
			BukkitSurvivalGamesPlugin.plugin.getLogger().warning(ChatColor.RED 
					+ "Error encountered when saving backup!");
		}
		BukkitSurvivalGamesPlugin.plugin.getLogger().info("finished backup dump!");
	}
	
	private YamlConfiguration config;
	
	private File outFile;
	
	private BackupDumper(YamlConfiguration config, File outFile) {
		this.config = config;
		this.outFile = outFile;
		
		this.runTaskAsynchronously(BukkitSurvivalGamesPlugin.plugin);
	}
	
	/**
	 * Schedules the backup to be written to file.<br />
	 * This does not run on the main thread, and instead runs async.
	 * @param backup
	 * @param outFile
	 */
	public static void dumpBackup(Backup backup, File outFile) {
		new BackupDumper(backup.asConfig(), outFile);
	}

}
