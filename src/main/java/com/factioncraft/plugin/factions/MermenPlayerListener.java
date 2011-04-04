package com.factioncraft.plugin.factions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import com.factioncraft.plugin.FactionCraftPlugin;

public class MermenPlayerListener extends PlayerListener {
	public MermenPlayerListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (mPlugin.GetFactionManager().IsPlayerInFaction(p.getName(), "Mermen")) {
			// is merman
			if(!event.hasItem() || event.getItem().getAmount() <= 0) {
				// empty stack in hand
				Material mat = p.getWorld().getBlockAt(p.getLocation()).getType();
				if(mat == Material.STATIONARY_WATER || mat == Material.WATER) {
					// player is in water
					p.setVelocity(p.getLocation().getDirection().multiply(mPlugin.MERMEN_SWIM_SPEED));
				}
			}
		}
	}
	
	FactionCraftPlugin mPlugin;
}
