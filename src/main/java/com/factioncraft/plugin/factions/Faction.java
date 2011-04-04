package com.factioncraft.plugin.factions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
	
	public boolean MatchesAliasesIgnoreCase(String name) {
		for(String alias: GetAliases()) {
			if(name.equalsIgnoreCase(alias))
				return true;
		}
		return false;
	}
	
	/**
	 * Called when a player joins the faction or a player of the faction joins the game.
	 * @param player The player who joins.
	 */
	public void OnPlayerJoin(Player player) {}
	
	public abstract String GetName();
	public abstract String[] GetAliases();
	public abstract ChatColor GetChatColor();
	public abstract String GetPrefix();
	
	public FactionCraftPlugin mPlugin;
}
