package com.factioncraft.plugin.factions;

import org.bukkit.ChatColor;

import com.factioncraft.plugin.FactionCraftPlugin;

public class Humans extends Faction {
	public Humans(FactionCraftPlugin plugin) {
		super(plugin);
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
