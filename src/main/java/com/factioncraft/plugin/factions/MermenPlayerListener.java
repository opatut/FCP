package com.factioncraft.plugin.factions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerListener;

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
		}
		super.onPlayerAnimation(event);

	}
	
	FactionCraftPlugin mPlugin;
}
