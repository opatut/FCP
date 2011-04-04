package com.factioncraft.plugin.factions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.factioncraft.plugin.FactionCraftPlugin;

public class DarkseekersEntityListener extends EntityListener {
	public DarkseekersEntityListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			if(mPlugin.GetFactionManager().IsPlayerInFaction(player.getName(), "Darkseekers")) {
				if(event.getCause() == DamageCause.FALL && player.getItemInHand().getType() == Material.FEATHER) {
					event.setCancelled(true);
					return;
				}
			}
		}
		super.onEntityDamage(event);
	}
	
	private FactionCraftPlugin mPlugin;
}
