package com.factioncraft.plugin.factions;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.factioncraft.plugin.FactionCraftPlugin;

public class OrcsEntityListener extends EntityListener {
	public OrcsEntityListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		// dont react on non-players
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player)event.getEntity();
		if(! mPlugin.GetFactionManager().IsPlayerInFaction(player.getName(), "Orcs"))
			return;
		
		// heal if burning or lava-ing, instead of damage
		if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.LAVA) {
			int h = player.getHealth();
			if(h < 20 && player.getWorld().getTime() % (HEAL_DELAY*20) == 0) h++;
			player.setHealth(h);
			event.setCancelled(true);
		} else if(event.getCause() == DamageCause.FIRE_TICK) {
			event.setCancelled(true);
		} else {
			super.onEntityDamage(event);
		}
	}
	
	FactionCraftPlugin mPlugin;
	public final int HEAL_DELAY = 5; // seconds between healing in fire / lava (1-2 hearts each time)
}
