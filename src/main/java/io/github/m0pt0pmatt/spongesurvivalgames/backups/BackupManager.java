package io.github.m0pt0pmatt.spongesurvivalgames.backups;

public final class BackupManager {
	
	private static BackupManager manager = null;
	
	public static BackupManager getManager() {
		if (manager == null) {
			manager = new BackupManager();
		}
		
		return manager;
	}
		
	private BackupManager() {
		
	}
}
