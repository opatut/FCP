package com.factioncraft.plugin.factions;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.factioncraft.plugin.FactionCraftPlugin;


public class DarkseekersPlayerListener extends PlayerListener {
	public DarkseekersPlayerListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
		mFlapsDone = new HashMap<String, Integer>();
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(mPlugin.GetFactionManager().IsPlayerInFaction(event.getPlayer().getName(), "Darkseekers")) {
				if(event.hasItem() && event.getItem().getType() == Material.TORCH) {
					event.getItem().setType(Material.REDSTONE_TORCH_ON);
					// event.getPlayer().getItemInHand().setType(Material.REDSTONE_TORCH_ON);
				}
			}
		}
		
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
			Player p = event.getPlayer();
			Material p_mat = p.getLocation().getBlock().getType();

			if(event.hasItem() && event.getItem().getType() == Material.FEATHER &&
					mPlugin.GetFactionManager().IsPlayerInFaction(p.getName(), "Darkseekers") &&
					p_mat != Material.WATER && p_mat != Material.STATIONARY_WATER &&
					p_mat != Material.LAVA && p_mat  != Material.STATIONARY_LAVA) {
				if(IsPlayerOnGround(p))
					SetPlayerFlapsDone(p, 0);
				
				int d = mFlapsDone.containsKey(p.getName()) ? mFlapsDone.get(p.getName()) : 0;
				
				d += 1;
				if(d <= mPlugin.DARKSEEKERS_MAX_FLAPS) {
					SetPlayerFlapsDone(p, d);
					if(d == mPlugin.DARKSEEKERS_MAX_FLAPS - 3)
						p.sendMessage(ChatColor.GRAY + "3 flaps left!");
					
					Vector dist = new Vector(0, 0.5, 0).add(p.getLocation().getDirection().multiply(mPlugin.DARKSEEKERS_FLIGHT_SPEED));
					p.setVelocity(dist);
				} else {
					p.sendMessage(ChatColor.GRAY + "You are too tired to fly further. Return to ground!");
				}
			}
		}
		// TODO Auto-generated method stub
		super.onPlayerInteract(event);
	}
	
	public void SetPlayerFlapsDone(Player player, int flaps) {
		mFlapsDone.put(player.getName(), flaps);
		float h = flaps * 1F / mPlugin.DARKSEEKERS_MAX_FLAPS;
		for(ItemStack stack: player.getInventory().all(Material.FEATHER).values()) {
			stack.setDurability((short)(h * 32));
		}
	}
	
	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(mPlugin.GetFactionManager().IsPlayerInFaction(event.getPlayer().getName(), "Darkseekers")) {
			if(event.getItem().getItemStack().getType() == Material.TORCH) {
				event.getItem().getItemStack().setType(Material.REDSTONE_TORCH_ON);
			}
		}
		super.onPlayerPickupItem(event);
	}
	
	@Override
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(mPlugin.GetFactionManager().IsPlayerInFaction(event.getPlayer().getName(), "Darkseekers")) {
			if(event.getItemDrop().getItemStack().getType() == Material.REDSTONE_TORCH_ON) {
				event.getItemDrop().getItemStack().setType(Material.TORCH);
			}
		}
		super.onPlayerDropItem(event);
	}
	
	public boolean IsPlayerOnGround(Player player) {
		Location pl = player.getLocation();
		Location bl = new Location(pl.getWorld(), pl.getBlockX(), pl.getBlockY() - 1, pl.getBlockZ());
		return (bl.getBlock() != null && bl.getBlock().getType() != Material.AIR 
				&& bl.getBlockY() < 128);
		}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.getPlayer() == null)
			return; 
		if(mPlugin.GetFactionManager().IsPlayerInFaction(event.getPlayer().getName(), "Darkseekers")) {
			Location to = event.getTo();
			if(IsPlayerOnGround(event.getPlayer())) {
				// on ground
				SetPlayerFlapsDone(event.getPlayer(), 0);
			}
			if(to.getBlock() != null && to.getBlock().getLightLevel() == 15) {
				// in sunlight
				event.getPlayer().setFireTicks(20);
			}
		}
	}
	
	FactionCraftPlugin mPlugin;
	
	private HashMap<String, Integer> mFlapsDone;
}
