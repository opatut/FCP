package com.factioncraft.plugin.factions;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
		
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			if(mPlugin.GetFactionManager().IsPlayerInFaction(player.getName(), "Mermen")) {
				// heal if drowning, instead of damage
				if(event.getCause() == DamageCause.DROWNING) {
					int h = player.getHealth();
					if(h < 20) h++;
					player.setHealth(h);
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
				if(mPlugin.GetFactionManager().IsPlayerInFaction(attacker.getName(), "Mermen")) {
					Material m = attacker.getItemInHand().getType();
					if(m == Material.WOOD_AXE || m == Material.STONE_AXE ||
							m == Material.IRON_AXE || m == Material.GOLD_AXE ||
							m == Material.DIAMOND_AXE) {
						int dmg_old = event.getDamage();
						float dmg_new = dmg_old * mPlugin.MERMEN_AXE_DAMAGE_MULTIPLIER;
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
