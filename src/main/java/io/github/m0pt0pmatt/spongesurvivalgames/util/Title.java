package io.github.m0pt0pmatt.spongesurvivalgames.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Utility class used to display titles.
 * @author Skyler
 *
 */
public final class Title {
	
	private static final ChatColor defaultColor = ChatColor.DARK_GREEN;
	
	private static final String playerToken = "[PLAYER]";
	
	private static final String titleToken = "[TITLE]";
	
	private static final String subtitleToken = "[SUBTITLE]";
	
	private static final String titleColorToken = "[TITLECOLOR]";
	
	private static final String subtitleColorToken = "[SUBTITLECOLOR]";
	
	private static final String[] titleCommands = new String[]{
			"title " + playerToken + " times 10 0 10",
			"title dove_bren subtitle {text:\"" + subtitleToken + "\",color:\"" + subtitleColorToken + "\"}",
			"title dove_bren title {text:\"" + titleToken + "\",color:\"" + titleColorToken + "\"}"
	};
	
	public static void displayTitle(Player player, String titleText, String subtitleText) {
		displayTitle(player, titleText, subtitleText, defaultColor, defaultColor);
	}
	
	public static void displayTitle(Player player, String titleText, String subtitleText, ChatColor titleColor, ChatColor subtitleColor) {
		for (String command : titleCommands) {
			
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), 
					command.replace(playerToken, player.getName())
						.replace(titleToken, titleText)
						.replace(subtitleToken, subtitleText)
						.replace(titleColorToken, titleColor.name().toLowerCase())
						.replace(subtitleColorToken, subtitleColor.name().toLowerCase())
					);
			
		}
	}
	
}
