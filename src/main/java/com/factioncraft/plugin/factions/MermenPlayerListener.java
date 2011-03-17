package com.factioncraft.plugin.factions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerListener;

public class MermenPlayerListener extends PlayerListener {
	public MermenPlayerListener() {
		
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
			Player p = event.getPlayer();
			// swinging hand
			if (p.getItemInHand().getAmount() == 0) {
				// empty stack in hand
				if(p.getLocation().getBlock().getType().getId() == Material.WATER.getId()) {
					// and he is swimming ;)
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					
					
					Location l = new Location(p.getWorld(), x, y, z);
					//p.teleportTo(l);

					p.sendMessage("Du schwimmst.");
					p.setDisplayName(ChatColor.GREEN + "Swimming " + p.getName());
				}
			}
		}
		super.onPlayerAnimation(event);

	}
}
