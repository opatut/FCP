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
    		if(mPlugin.GetFactionManager().GetPlayerFaction(player) != null) {
    			if(mPlugin.IsPlayerOP(player)) {
    				player.sendMessage(ChatColor.RED + "You have to "+ ChatColor.WHITE +"/quitfaction" + ChatColor.RED + " first.");
    			} else {
    				player.sendMessage(ChatColor.RED + "You have to quit your faction first. Ask an OP/Admin to reset your faction for you.");
    			}
    			return true;
    		} else if(args.length >= 1) {
    			Faction f = mPlugin.GetFactionByName(args[0]);
    			if(f != null) {
    				if(mPlugin.mPermissions.has(player, "factioncraft.join."+f.GetName().toLowerCase())) {
			    		mPlugin.GetFactionManager().SetPlayerFaction(player, f);
			    		player.sendMessage(ChatColor.GREEN + "You joined the " + f.GetChatColor() + f.GetName()
			    				+ ChatColor.GREEN + " faction.");
    				} else {
    					player.sendMessage(ChatColor.RED + "You are not allowed to join the " + f.GetChatColor() 
    							+ f.GetName() + ChatColor.RED + ".");
    					
    				}
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
    	} else if (label.equalsIgnoreCase("factionspawn")) {
    		if(args.length == 0) {
    			// teleport to own's faction's spawn
    			if(mPlugin.mPermissions.has(player, "factioncraft.factionspawn.own")) {
    				mPlugin.GetFactionSpawnManager().Teleport(player, true);
    			} else {
    				player.sendMessage(ChatColor.RED + "You are not allowed to teleport yourself to your spawn.");
    			}
    			return true;
    		} else if(args.length == 1) {
    			String f = args[0];
    			Faction faction = mPlugin.GetFactionByName(f);
    			Faction player_faction = mPlugin.GetFactionManager().GetPlayerFaction(player);
    			if(faction != null) {
	    			if(mPlugin.mPermissions.has(player, "factioncraft.factionspawn.custom") ||
	    					player_faction == faction) {
		    			if(!mPlugin.GetFactionSpawnManager().Teleport(player, faction, false))
		    				player.sendMessage(ChatColor.RED + "The faction's spawn location is not set.");
	    			} else {
	    				player.sendMessage(ChatColor.RED + "You are not allowed to teleport yourself to the desired spawn.");
	    			}
    			} else {
    				player.sendMessage(ChatColor.RED + "The faction " + ChatColor.WHITE + f + ChatColor.RED + " could not be found.");
    			}
    			return true;
    		} else if(args.length == 2) {
    			String cmd = args[1].toLowerCase();
    			if(cmd.equals("set") || cmd.equals("unset")) {
    				if(mPlugin.mPermissions.has(player, "factioncraft.factionspawn.edit")) {
		    			String f = args[0];
		    			Faction faction = mPlugin.GetFactionByName(f);
		    			if(faction != null) {
		    				if(cmd.equals("set")) {
		    					mPlugin.GetFactionSpawnManager().SetSpawn(player.getWorld(), faction.GetName(), player.getLocation());
		    					player.sendMessage(ChatColor.GREEN + "The faction spawn for " + faction.GetChatColor() + faction.GetName() 
		    							+ ChatColor.GREEN + " has been set.");
		    				} else if(cmd.equals("unset")) {
		    					mPlugin.GetFactionSpawnManager().SetSpawn(player.getWorld(), faction.GetName(), null);
		    					player.sendMessage(ChatColor.GREEN + "The faction spawn for " + faction.GetChatColor() + faction.GetName() 
		    							+ ChatColor.GREEN + " has been unset.");
		    				}
		    			} else {
		    				player.sendMessage(ChatColor.RED + "The faction " + ChatColor.WHITE + f + ChatColor.RED + " could not be found.");
		    			}
    				} else {
    					player.sendMessage(ChatColor.RED + "You are not allowed to edit faction spawns.");
    				}
	    			return true;
    			}
    		}
    	} else if(label.equalsIgnoreCase("playerfaction")) {
    		if(args.length <= 1) {
    			String p = player.getName();
    			if(args.length == 1) {
    				p = args[0];
    			}
    			Faction f = mPlugin.GetFactionManager().GetPlayerFaction(p);
    			if(f == null) {
    				player.sendMessage(ChatColor.RED + (p.equals(player.getName()) ? "You have" : "The player has") 
    						+ " not joined any faction yet.");
    			} else {
    				player.sendMessage(ChatColor.GREEN + (p.equals(player.getName()) ? "Your are" : "The player is") 
    						+ " member of the " + f.GetChatColor() + f.GetName() + ChatColor.GREEN + " faction.");
    			}
    			return true;
    		}
    	}
    	return false;
    }

}
