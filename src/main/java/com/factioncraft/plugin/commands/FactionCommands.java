package com.factioncraft.plugin.commands;

import org.bukkit.*;
import org.bukkit.command.*;

import com.factioncraft.plugin.FactionCraftPlugin;
import com.factioncraft.plugin.factions.Faction;

public class FactionCommands implements CommandExecutor {
    private final FactionCraftPlugin mPlugin;

    public FactionCommands(FactionCraftPlugin plugin) {
        mPlugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if(command.getAliases().contains("listfactions")) {
    		String m = ChatColor.LIGHT_PURPLE + "Factions available:";
    		for(String f: mPlugin.GetFactions().keySet()) {
    			m += " " + f;
    		}
    		sender.sendMessage(m);
    	}
        
        return true;
    }

}
