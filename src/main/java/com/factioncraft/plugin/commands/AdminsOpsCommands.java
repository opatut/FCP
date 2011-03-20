package com.factioncraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.factioncraft.plugin.FactionCraftPlugin;
import com.factioncraft.plugin.PlayerFlagsManager;

public class AdminsOpsCommands implements CommandExecutor {
	public AdminsOpsCommands(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		PlayerFlagsManager fm = mPlugin.GetPlayerFlagsManager();
		
		if(label.equalsIgnoreCase("flagadmin")) {
			if(mPlugin.IsPlayerAdmin(player)) {
				if(args.length < 1) {
					// toggle
					fm.ToggleFlag(player, "flagadmin");
					player.sendMessage(ChatColor.GREEN + "Your admin flag has been turned "
						+ (fm.GetFlag(player, "flagadmin", false) ? "on" : "off") + ".");
				} else {
					if(args[0].equalsIgnoreCase("on")) {
						// Set on
						fm.SetFlag(player, "flagadmin", true);
						player.sendMessage(ChatColor.GREEN + "Your admin flag has been turned on.");
					} else if (args[0].equalsIgnoreCase("off")) {
						// Set off
						fm.SetFlag(player, "flagadmin", false);
						player.sendMessage(ChatColor.GREEN + "Your admin flag has been turned off.");
					} else {
						// Wrong usage
						player.sendMessage(ChatColor.RED + "Usage: /flagadmin [on|off]");
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "Well, you'd propably like to flag yourself as admin... but you can't.");
			}
			return true;
		}
		
		if(label.equalsIgnoreCase(ADMIN_FLAG)) {
			if(mPlugin.IsPlayerAdmin(player)) {
				if(args.length < 1) {
					// toggle
					fm.ToggleFlag(player, ADMIN_FLAG);
					player.sendMessage(ChatColor.GREEN + "Your admin flag has been turned "
						+ (fm.GetFlag(player, ADMIN_FLAG, false) ? "on" : "off") + ".");
				} else {
					if(args[0].equalsIgnoreCase(YES_ANSWER)) {
						// Set on
						fm.SetFlag(player, ADMIN_FLAG, true);
						player.sendMessage(ChatColor.GREEN + "Your admin flag has been turned on.");
					} else if (args[0].equalsIgnoreCase(NO_ANSWER)) {
						// Set off
						fm.SetFlag(player, ADMIN_FLAG, false);
						player.sendMessage(ChatColor.GREEN + "Your admin flag has been turned off.");
					} else {
						// Wrong usage
						player.sendMessage(ChatColor.RED + "Usage: /" + ADMIN_FLAG + " [" 
								+ YES_ANSWER + "|" + NO_ANSWER + "]");
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "Well, you'd propably like to flag yourself as admin... but you can't.");
			}
			return true;
		} else if(label.equalsIgnoreCase(OP_FLAG)) {
			if(mPlugin.IsPlayerOP(player)) {
				if(args.length < 1) {
					// toggle
					fm.ToggleFlag(player, OP_FLAG);
					player.sendMessage(ChatColor.GREEN + "Your OP flag has been turned "
						+ (fm.GetFlag(player, OP_FLAG, false) ? "on" : "off") + ".");
				} else {
					if(args[0].equalsIgnoreCase(YES_ANSWER)) {
						// Set on
						fm.SetFlag(player, OP_FLAG, true);
						player.sendMessage(ChatColor.GREEN + "Your OP flag has been turned on.");
					} else if (args[0].equalsIgnoreCase(NO_ANSWER)) {
						// Set off
						fm.SetFlag(player, OP_FLAG, false);
						player.sendMessage(ChatColor.GREEN + "Your OP flag has been turned off.");
					} else {
						// Wrong usage
						player.sendMessage(ChatColor.RED + "Usage: /" + OP_FLAG + "[" 
								+ YES_ANSWER + "|" + NO_ANSWER + "]");
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "Well, you'd propably like to flag yourself as OP... but you can't.");
			}
			return true;
		}
		
		return false;
	}
	
	public FactionCraftPlugin mPlugin;
	
	public final String YES_ANSWER = "on";
	public final String NO_ANSWER = "off";
	
	public final String ADMIN_FLAG = "flagadmin";
	public final String OP_FLAG = "flagop";
}
