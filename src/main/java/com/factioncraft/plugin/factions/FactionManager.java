package com.factioncraft.plugin.factions;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import com.factioncraft.plugin.FactionCraftPlugin;

public class FactionManager {
	public FactionManager(FactionCraftPlugin plugin) {
		mPlugin = plugin;
		mPlayerFactions = new HashMap<String, Faction>();
	}
	
	public void Load() {
		File f = new File(mPlugin.getDataFolder(), PLAYER_FACTIONS_FILE);
		Configuration c = new Configuration(f);
		c.load();
		List<String> players = c.getKeys(null);
		if(players != null) {
			for(String player: players) {
				String faction = c.getString(player);
				if(mPlugin.GetFactions().keySet().contains(faction)) {
					// set player faction
					mPlayerFactions.put(player, mPlugin.GetFactions().get(faction));
				}
			}
		}
	}
	
	public void Save() {
		Configuration c = new Configuration(new File(mPlugin.getDataFolder(), PLAYER_FACTIONS_FILE));
		for(String player: mPlayerFactions.keySet()) {
			c.setProperty(player, mPlayerFactions.get(player).GetName());
		}
		c.save();
		Logger.getLogger("Minecraft").info("Saved Factions. (" + mPlayerFactions.size() + ")");
	}
	
	public Faction GetPlayerFaction(Player player) {
		return GetPlayerFaction(player.getName());
	}
	public Faction GetPlayerFaction(String player) {
		if(mPlayerFactions.containsKey(player)) {
			return mPlayerFactions.get(player);
		}
		return null;
	}
	
	public boolean IsPlayerInFaction(Player player, Faction faction) {
		return IsPlayerInFaction(player.getName(), faction.GetName());
	}
	public boolean IsPlayerInFaction(String player, String faction) {
		return mPlayerFactions.containsKey(player) && 
			mPlayerFactions.get(player).GetName().equals(faction);
	}
	
	public void SetPlayerFaction(Player player, Faction faction) {
		mPlayerFactions.put(player.getName(), faction);
	}
	
	private FactionCraftPlugin mPlugin;
	private HashMap<String, Faction> mPlayerFactions;
	
	public static final String PLAYER_FACTIONS_FILE = "playerfactions.yml";
}
