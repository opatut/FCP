package com.factioncraft.plugin.factions;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;

import com.factioncraft.plugin.FactionCraftPlugin;

public class MermenEntityListener extends EntityListener {
	public MermenEntityListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		// dont react on non-players
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player)event.getEntity();
		if(! mPlugin.GetFactionManager().IsPlayerInFaction(player.getName(), "Mermen"))
			return;
		
		// heal if drowning, instead of damage
		if(event.getCause() == DamageCause.DROWNING) {
			int h = player.getHealth();
			if(h < 20) h++;
			player.setHealth(h);
			event.setCancelled(true);
		} else {
			super.onEntityDamage(event);
		}
	}
	
	FactionCraftPlugin mPlugin;
}
