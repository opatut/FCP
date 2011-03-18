package com.factioncraft.plugin.factions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

import com.factioncraft.plugin.FactionCraftPlugin;

public class Orcs extends Faction {

	public Orcs(FactionCraftPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void OnEnable() {
		PluginManager pm = mPlugin.getServer().getPluginManager();
		OrcsEntityListener oel = new OrcsEntityListener(mPlugin);
		pm.registerEvent(Event.Type.ENTITY_DAMAGED, oel, Priority.Normal, mPlugin);
	}
	
	@Override
	public void OnPlayerJoin(Player player) {
	}
	
	@Override
	public String GetName() {
		return "Orcs";
	}

	@Override
	public ChatColor GetChatColor() {
		return ChatColor.DARK_GREEN;
	}

	@Override
	public String GetPrefix() {
		return "O";
	}

}
