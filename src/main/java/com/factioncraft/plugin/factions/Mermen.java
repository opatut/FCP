package com.factioncraft.plugin.factions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

import com.factioncraft.plugin.FactionCraftPlugin;

public class Mermen extends Faction {

	public Mermen(FactionCraftPlugin plugin) {
		super(plugin);
	}
	
	public void OnEnable() {
		PluginManager pm = mPlugin.getServer().getPluginManager();
		MermenPlayerListener mpl = new MermenPlayerListener(mPlugin);
		MermenEntityListener mel = new MermenEntityListener(mPlugin);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, mpl , Priority.Normal, mPlugin);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, mel, Priority.Normal, mPlugin);
	}
	
	@Override
	public void OnPlayerJoin(Player player) {
		player.setMaximumAir(0);
	}

	@Override
	public String GetName() {
		return "Mermen";
	}
	
	@Override
	public String[] GetAliases() {
		String[] r = {"Mermen", "Merman", "Mermaid", "Merfolk", "Swimmer"};
		return r;
	}

	@Override
	public ChatColor GetChatColor() {
		return ChatColor.AQUA;
	}
	
	@Override
	public String GetPrefix() {
		return "M";
	}

	
}
