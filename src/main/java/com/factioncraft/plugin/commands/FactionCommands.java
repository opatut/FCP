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
    	// only players can use these commands (in-game)
    	if(!(sender instanceof Player))
    		return false;
    	Player player = (Player)sender;
    	
    	if(label.equalsIgnoreCase("factions")) {
    		String m = ChatColor.GREEN.toString() + mPlugin.GetFactions().size() + " Factions available:";
    		for(Faction f: mPlugin.GetFactions().values()) {
    			m += " " + f.GetChatColor().toString() + f.GetName();
    		}
    		player.sendMessage(m);
    		Faction f = mPlugin.GetFactionManager().GetPlayerFaction(player);
    		if(f != null) {
	    		player.sendMessage(ChatColor.GREEN + "> You are member of the " + f.GetChatColor() + 
	    				f.GetName() + ChatColor.GREEN + " faction.");
    		} else {
    			player.sendMessage(ChatColor.RED + "> You are not currently member of any faction.");
    		}
    		return true;
    	} else if(label.equalsIgnoreCase("joinfaction")) {
    		if(args.length >= 1) {
    			if(mPlugin.GetFactions().containsKey(args[0])) {
    				Faction f = mPlugin.GetFactions().get(args[0]);
		    		mPlugin.GetFactionManager().SetPlayerFaction(player, f);
		    		player.sendMessage(ChatColor.GREEN + "You joined the " + f.GetChatColor() + f.GetName()
		    				+ ChatColor.GREEN + " faction.");
    			} else {
    				player.sendMessage(ChatColor.RED + "The faction " + ChatColor.YELLOW 
    						+ args[0] + ChatColor.RED + " does not exist.");
    			}
	    		return true;
    		}
    	} else if(label.equalsIgnoreCase("quitfaction")) {
    		if(mPlugin.IsPlayerOP(player)) {
    			String p = player.getName();
    			if(args.length >= 1) {
    				p = args[0];
    			}
    			Player target_player = mPlugin.getServer().getPlayer(p);
    			if(target_player != null) {
    				if(mPlugin.GetFactionManager().GetPlayerFaction(target_player) != null) {
	    				mPlugin.GetFactionManager().SetPlayerFaction(target_player, null);
	    				if(! player.getName().equals(target_player.getName())) {
	    					// the op reset someone's elses faction
	    					player.sendMessage(ChatColor.GREEN + "Success: " + p + " is now without faction.");
	    				}
	    				target_player.sendMessage(ChatColor.GREEN + "Your faction been reset by " + 
	    						ChatColor.WHITE + player.getName() + "."); 
	    				target_player.sendMessage(ChatColor.GREEN + "You are being logged out to apply the changes...");
	    				target_player.kickPlayer("Faction reset by "+player.getName()+". Please login again to apply the changes.");
    				} else {
    					player.sendMessage(ChatColor.GREEN + "Pointless... " + p + " was in no faction! :D");
    				}
    			} else {
    				player.sendMessage(ChatColor.RED + "Could not find player: " + p);
    			}
    		} else {
    			player.sendMessage(ChatColor.RED + "Only OPs can let you quit your faction. Please contact them.");
    		}
    		return true;
    	}
    	return false;
    }

}
