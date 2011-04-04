package com.factioncraft.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.factioncraft.plugin.factions.Faction;

public class MainPlayerListener extends PlayerListener {
	public MainPlayerListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Location loc = mPlugin.GetFactionSpawnManager().GetTeleportLocation(event.getPlayer());
		if(loc != null) {
			event.setRespawnLocation(loc);
			Faction f = mPlugin.GetFactionManager().GetPlayerFaction(event.getPlayer());
			if(f != null) {
				event.getPlayer().sendMessage(ChatColor.GREEN + "You respawn at the faction spawn of the " + 
						f.GetChatColor() + f.GetName() + ChatColor.GREEN + ".");
			}
		}
		super.onPlayerRespawn(event);
	}
	
	@Override	
	public void onPlayerJoin(PlayerJoinEvent event) {
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
			String msg = new String(mPlugin.WELCOME_MESSAGE);
			msg = msg.replaceAll("\\%name\\%", player.getName());
			msg = msg.replaceAll("\\%faction\\%", faction.GetChatColor() + faction.GetName() + c);
			String[] split = msg.split("\\<br\\>");
			for(String s: split) {
				player.sendMessage(c + s);
			}
			event.setJoinMessage(ChatColor.YELLOW + player.getName() + faction.GetChatColor() + 
					" (" + faction.GetName() + ") " + ChatColor.YELLOW + "joined the game");
		}
	}
	
	FactionCraftPlugin mPlugin;
}
