package com.factioncraft.plugin.factions;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
		if(event.getEntity() instanceof Player) {		
			Player player = (Player)event.getEntity();
			if(mPlugin.GetFactionManager().IsPlayerInFaction(player.getName(), "Orcs")) {
				// heal if burning or lava-ing, instead of damage
				if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.LAVA) {
					int h = player.getHealth();
					if(h < 20 && player.getWorld().getTime() % (mPlugin.ORCS_HEAL_IN_FIRE_DELAY*20) == 0) h++;
					player.setHealth(h);
					event.setCancelled(true);
					return;
				} else if(event.getCause() == DamageCause.FIRE_TICK) {
					event.setCancelled(true);
					return;
				}
			}
		}
		
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			Entity damager = e.getDamager();
			if(damager instanceof Player) {
				Player attacker = (Player)damager;
				if(mPlugin.GetFactionManager().IsPlayerInFaction(attacker.getName(), "Orcs")) {
					Material m = attacker.getItemInHand().getType();
					if(m == Material.WOOD_SWORD || m == Material.STONE_SWORD ||
							m == Material.IRON_SWORD || m == Material.GOLD_SWORD ||
							m == Material.DIAMOND_SWORD) {
						int dmg_old = event.getDamage();
						float dmg_new = dmg_old * mPlugin.ORCS_SWORD_DAMAGE_MULTIPLIER;
						int dmg_new_i = (int) Math.ceil(dmg_new);
						// mPlugin.getServer().broadcastMessage(event.getDamage() + " > " + dmg_new + " > " + dmg_new_i);
						event.setDamage(dmg_new_i);
					}	
				}
			}
		}
		
		super.onEntityDamage(event);
	}
	
	FactionCraftPlugin mPlugin; 
}
