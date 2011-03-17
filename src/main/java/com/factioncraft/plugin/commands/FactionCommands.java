package com.factioncraft.plugin.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import com.factioncraft.plugin.FactionCraftPlugin;
import com.factioncraft.plugin.factions.Faction;

public class FactionCommands implements CommandExecutor {
    private final FactionCraftPlugin mPlugin;

    public FactionCommands(FactionCraftPlugin plugin) {
        mPlugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if(label.equalsIgnoreCase("factions")) {
    		String m = ChatColor.GREEN.toString() + mPlugin.GetFactions().size() + " Factions available:";
    		for(Faction f: mPlugin.GetFactions().values()) {
    			m += " " + f.GetChatColor().toString() + f.GetName();
    		}
    		sender.sendMessage(m);
    		Faction f = mPlugin.GetFactionManager().GetPlayerFaction((Player)sender);
    		if(f != null) {
	    		sender.sendMessage(ChatColor.GREEN + "> You are member of the " + f.GetChatColor() + 
	    				f.GetName() + ChatColor.GREEN + " faction.");
    		} else {
    			sender.sendMessage(ChatColor.RED + "> You are not currently member of any faction.");
    		}
    		return true;
    	} else if(label.equalsIgnoreCase("joinfaction")) {
    		if(args.length >= 1) {
    			if(mPlugin.GetFactions().containsKey(args[0])) {
    				Faction f = mPlugin.GetFactions().get(args[0]);
		    		mPlugin.GetFactionManager().SetPlayerFaction((Player)sender, f);
		    		sender.sendMessage(ChatColor.GREEN + "You joined the " + f.GetChatColor() + f.GetName()
		    				+ ChatColor.GREEN + " faction.");
    			} else {
    				sender.sendMessage(ChatColor.RED + "The faction " + ChatColor.YELLOW 
    						+ args[0] + ChatColor.RED + " does not exist.");
    			}
	    		return true;
    		}
    	}
    	return false;
    }

}
