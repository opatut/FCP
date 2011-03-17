package com.factioncraft.plugin.factions;

import org.bukkit.ChatColor;

import com.factioncraft.plugin.FactionCraftPlugin;

/**
 * Faction names are <b>always</b> plural (eg Mermen, Humans etc)
 * @author paul
 *
 */
public abstract class Faction {
	public Faction(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	public void OnEnable() {
		// PluginManager pm = mPlugin.getServer().getPluginManager();
	}
	
	public abstract String GetName();
	public abstract ChatColor GetChatColor();
	public abstract String GetPrefix();
	
	public FactionCraftPlugin mPlugin;
}
