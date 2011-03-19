package com.factioncraft.plugin.factions;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.factioncraft.plugin.FactionCraftPlugin;

public class HumansPlayerListener extends PlayerListener {
	public HumansPlayerListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(! mPlugin.GetFactionManager().IsPlayerInFaction(event.getPlayer().getName(), "Humans")) {
			// disallow every non-human player from picking up saplings			
			if(event.getItem().getItemStack().getType() == Material.SAPLING) {
				event.setCancelled(true);
				return;
			}
			
			// disallow every non-human player from picking up seeds			
			if(event.getItem().getItemStack().getType() == Material.SEEDS) {
				event.setCancelled(true);
				return;
			}
			
			if(event.getItem().getItemStack().getType() == Material.BREAD) {
				event.setCancelled(true);
				return;
			}
		}
		
		super.onPlayerPickupItem(event);
		// event.setCancelled(true);
	}
	
	public void onPlayerItem(PlayerItemEvent event) {
		if(! mPlugin.GetFactionManager().IsPlayerInFaction(event.getPlayer().getName(), "Humans")) {
			// disallow every non-human player to use a hoe
			if (event.getItem().getType() == Material.WOOD_HOE || 
					event.getItem().getType() == Material.STONE_HOE ||
					event.getItem().getType() == Material.IRON_HOE || 
					event.getItem().getType() == Material.GOLD_HOE ||
					event.getItem().getType() == Material.DIAMOND_HOE) {
				event.setCancelled(true);
				return;
			}
			
			// disallow every non-human player to use seeds
			if (event.getItem().getType() == Material.SEEDS) {
				event.setCancelled(true);
				return;
			}
		}
		super.onPlayerItem(event);
	}
	
	FactionCraftPlugin mPlugin;
}
