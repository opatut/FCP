package com.factioncraft.plugin.factions;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

import com.factioncraft.plugin.FactionCraftPlugin;

public class Darkseekers extends Faction {
	public Darkseekers(FactionCraftPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void OnEnable() {
		PluginManager pm = mPlugin.getServer().getPluginManager();
		DarkseekersPlayerListener dpl = new DarkseekersPlayerListener(mPlugin);
		pm.registerEvent(Event.Type.PLAYER_MOVE, dpl, Priority.Normal, mPlugin);
		pm.registerEvent(Event.Type.PLAYER_ANIMATION, dpl, Priority.Normal, mPlugin);
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, dpl, Priority.Normal, mPlugin);
		pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, dpl, Priority.Normal, mPlugin);		
		pm.registerEvent(Event.Type.PLAYER_INTERACT, dpl, Priority.Normal, mPlugin);
		
		DarkseekersEntityListener del = new DarkseekersEntityListener(mPlugin);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, del, Priority.Normal, mPlugin);
		
		super.OnEnable();
	}

	@Override
	public String GetName() {
		return "Darkseekers";
	}

	@Override
	public String[] GetAliases() {
		String[] r = {"Darkseekers", "Darkseeker", "Vampires", "Vampire", 
				"Mutants", "Mutant", "Skypeople", "Darkfolk", "Skyman"};
		return r;
	}

	@Override
	public ChatColor GetChatColor() {
		return ChatColor.DARK_GRAY;
	}

	@Override
	public String GetPrefix() {
		return "D";
	}

}
