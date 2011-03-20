package com.factioncraft.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class PlayerFlagsManager {
	public PlayerFlagsManager(FactionCraftPlugin plugin) {
		mPlugin = plugin;
		mFlags = new HashMap<String, Boolean>();
	}
	
	public void Load() {
		File f = new File(mPlugin.getDataFolder(), PLAYER_FLAGS_FILE);
		Configuration c = new Configuration(f);
		c.load();
		List<String> players = c.getKeys(null);
		if(players != null) {
			for(String player: players) {
				
				List<String> flags = c.getKeys(player);
				for(String flag: flags) {
					mFlags.put(player + "." + flag, c.getBoolean(player + "." + flag, false));
				}
				
			}
		}
	}
	
	public void Save() {
		Configuration c = new Configuration(new File(mPlugin.getDataFolder(), PLAYER_FLAGS_FILE));
		for(String player_flag: mFlags.keySet()) {
			c.setProperty(player_flag, mFlags.get(player_flag));
		}
		c.save();
		Logger.getLogger("Minecraft").info("Saved " + mFlags.size() + " player flags.");
	}
	
	public void SetFlag(Player player, String flag, boolean value) {
		mFlags.put(player.getName() + "." + flag, value);
	}
	
	public void ToggleFlag(Player player, String flag) {
		SetFlag(player, flag, ! GetFlag(player, flag, false));
	}
	
	public boolean GetFlag(Player player, String flag, boolean def) {
		if(mFlags.containsKey(player.getName() + "." + flag))
			return mFlags.get(player.getName() + "." + flag);
		return def;
	}
	
	HashMap<String, Boolean> mFlags;
	
	FactionCraftPlugin mPlugin;
	public final String PLAYER_FLAGS_FILE = "playerflags.yml";
}
