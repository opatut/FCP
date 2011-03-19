package com.factioncraft.plugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

import com.factioncraft.plugin.factions.Faction;

public class MainPlayerListener extends PlayerListener {
	public MainPlayerListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	public void onPlayerJoin(PlayerEvent event) {
		Player player = event.getPlayer();
		
		Faction faction = mPlugin.GetFactionManager().GetPlayerFaction(player);
		String c = ChatColor.LIGHT_PURPLE.toString();
		if(faction == null) {
			// notify the player that he is currently in no faction and
			// how to join one
			player.sendMessage(c + "==================== FACTIONS =======================");
			player.sendMessage(c + "Welcome! This server features different player factions.");
			player.sendMessage(c + "You should start right away and list the available factions");
			player.sendMessage(c + "with the " + ChatColor.WHITE + "/factions" + c + " command. Then you should propably join");
			player.sendMessage(c + "one of the factions using " + ChatColor.WHITE + "/joinfaction" + c + ". Have fun with the");
			player.sendMessage(c + "faction specific perks!");
			player.sendMessage(c + "====================================================");
		} else {
			faction.OnPlayerJoin(player);
			player.sendMessage(c + "Welcome back, " + player.getName() + "!");
			player.sendMessage("Your friends, the " + faction.GetChatColor() + faction.GetName() + c + " are with you.");
		}
	}
	
	FactionCraftPlugin mPlugin;
}
