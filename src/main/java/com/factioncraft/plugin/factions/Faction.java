package com.factioncraft.plugin.factions;

import com.factioncraft.plugin.FactionCraftPlugin;

/**
 * Faction names are <b>always</b> plural (eg Mermen, Humans etc)
 * @author paul
 *
 */
public class Faction {
	public Faction(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	public void OnEnable() {
		// PluginManager pm = mPlugin.getServer().getPluginManager();
	}
	
	public FactionCraftPlugin mPlugin;
}
