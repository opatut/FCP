package com.factioncraft.plugin.factions;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(! mPlugin.GetFactionManager().IsPlayerInFaction(event.getPlayer().getName(), "Humans")) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				// disallow every non-human player to use a hoe
				if (event.hasItem() &&
						event.getItem().getType() == Material.WOOD_HOE || 
						event.getItem().getType() == Material.STONE_HOE ||
						event.getItem().getType() == Material.IRON_HOE || 
						event.getItem().getType() == Material.GOLD_HOE ||
						event.getItem().getType() == Material.DIAMOND_HOE) {
					event.setCancelled(true);
					return;
				}
				
				// disallow every non-human player to use seeds
				if (event.hasItem() &&
						event.getItem().getType() == Material.SEEDS) {
					event.setCancelled(true);
					return;
				}
			}
			
		} else {
			// humans
			// spawn huge tree
			/*
			if (event.getItem() != null && event.getItem().getType() == Material.STICK && event.getClickedBlock() != null) {
				if(event.getClickedBlock().getType() == Material.GRASS && mPlugin.IsPlayerAdmin(event.getPlayer())) {
					GiantTreeGenerator.GenerateTree(event.getClickedBlock().getLocation());
				}
			} */
		}
	}
	/*
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
		} else {
			// humans
			
			if (event.getItem().getType() == Material.STICK && event.getBlockClicked() != null) {
				if(event.getBlockClicked().getType() == Material.GRASS && mPlugin.IsPlayerAdmin(event.getPlayer())) {
					// spawn huge tree
					GiantTreeGenerator.GenerateTree(event.getBlockClicked().getLocation());
				}
			}
		}
		super.onPlayerItem(event);
	}
	 */
	FactionCraftPlugin mPlugin;
}
