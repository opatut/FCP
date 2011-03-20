package com.factioncraft.plugin;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

import com.factioncraft.plugin.factions.Faction;

public class FactionChatListener extends PlayerListener {
	public FactionChatListener(FactionCraftPlugin plugin) {
		mPlugin = plugin;
	}
	
	public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        
        // pretend player to be merman
        boolean is_admin = mPlugin.IsPlayerAdmin(player) && mPlugin.GetPlayerFlagsManager().GetFlag(player, "flagadmin", false);
        boolean is_op = mPlugin.IsPlayerOP(player) && mPlugin.GetPlayerFlagsManager().GetFlag(player, "flagop", false);
        
        Faction player_faction = mPlugin.GetFactionManager().GetPlayerFaction(player);
        String m = "";
        
        m += player_faction.GetChatColor() + "[" + player_faction.GetPrefix();

        if(mPlugin.IsPlayerPermium(player))
        	// write golden coin  = (R)
        	m += ChatColor.GOLD + "\u00AE" + player_faction.GetChatColor();
        
        if(is_admin)	m += "!";
        else if(is_op)	m += "*";
        
        m += "] " + player.getDisplayName() + ": ";
        
        if(is_admin)	m += ChatColor.DARK_RED;
        else if(is_op)	m += ChatColor.GOLD;
        else			m += ChatColor.WHITE;
        
        m += event.getMessage();
        
        for(Player r: event.getRecipients()) {
        	r.sendMessage(m);
        }
        Logger.getLogger("Minecraft").info(player.getDisplayName() + " said: " + event.getMessage());
        event.setCancelled(true);
    }
	
	FactionCraftPlugin mPlugin;
}
