package com.factioncraft.plugin.factions;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

import com.factioncraft.plugin.FactionCraftPlugin;

public class HumansEntityListener extends EntityListener {
	public HumansEntityListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if(event instanceof EntityDamageByProjectileEvent) {
			EntityDamageByProjectileEvent e = (EntityDamageByProjectileEvent)event;
			if(e.getDamager() instanceof Player) {
				Player attacker = (Player)e.getDamager();
				if(mPlugin.GetFactionManager().IsPlayerInFaction(attacker.getName(), "Humans")) {
					// Human attacked
					if(e.getProjectile() instanceof Arrow) {
						// damaged by a human's arrow
						int dmg_old = e.getDamage();
						float dmg_new = dmg_old * mPlugin.HUMANS_BOW_DAMAGE_MULTIPLIER;
						int dmg_new_i = (int)Math.ceil(dmg_new);
						event.setDamage(dmg_new_i);
					}
				}
			}
		}
		
		super.onEntityDamage(event);
	}
	
	public FactionCraftPlugin mPlugin;
}
