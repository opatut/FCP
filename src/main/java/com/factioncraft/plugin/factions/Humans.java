package com.factioncraft.plugin.factions;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;

import com.factioncraft.plugin.FactionCraftPlugin;

public class Humans extends Faction {
	public Humans(FactionCraftPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void OnEnable() {
		PluginManager pm = mPlugin.getServer().getPluginManager();
		
		HumansPlayerListener hpl = new HumansPlayerListener(mPlugin);
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, hpl, Priority.Normal, mPlugin);
		pm.registerEvent(Event.Type.PLAYER_ITEM, hpl, Priority.Normal, mPlugin);
		
		HumansEntityListener hel = new HumansEntityListener(mPlugin);
		pm.registerEvent(Event.Type.ENTITY_DAMAGED, hel, Priority.Normal, mPlugin);
	}

	@Override
	public String GetName() {
		return "Humans";
	}

	@Override
	public ChatColor GetChatColor() {
		return ChatColor.YELLOW;
	}

	@Override
	public String GetPrefix() {
		return "H";
	}

}
