package com.factioncraft.plugin.factions;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

import com.factioncraft.plugin.FactionCraftPlugin;

public class Mermen extends Faction {

	public Mermen(FactionCraftPlugin plugin) {
		super(plugin);
	}
	
	public void OnEnable() {
		PluginManager pm = mPlugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_MOVE, new MermenPlayerListener(), Priority.Normal, mPlugin);
	}

	
}
