package com.factioncraft.plugin.factions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.factioncraft.plugin.FactionCraftPlugin;

public class MermenPlayerListener extends PlayerListener {
	public MermenPlayerListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
			Player p = event.getPlayer();
			
			// its a merman
			if (mPlugin.GetFactionManager().IsPlayerInFaction(p.getName(), "Mermen")) {
				// the player has nothing in his hand
				if (p.getItemInHand().getAmount() <= 0) {
					// empty stack in hand
					Material mat = p.getWorld().getBlockAt(p.getLocation()).getType();
					if(mat == Material.STATIONARY_WATER || mat == Material.WATER) {
						p.setVelocity(p.getLocation().getDirection().multiply(mPlugin.MERMEN_SWIM_SPEED));
					}
				}
			}
			
			if(p.getItemInHand().getType() == Material.FEATHER) {
				Vector dist = new Vector(0, 0.5, 0).add(p.getLocation().getDirection().multiply(1.0));
				p.setVelocity(dist);
			}
			
		}
		super.onPlayerAnimation(event);

	}
	
	FactionCraftPlugin mPlugin;
}
