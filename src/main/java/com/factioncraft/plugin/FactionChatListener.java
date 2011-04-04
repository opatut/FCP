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
        
        Faction player_faction = mPlugin.GetFactionManager().GetPlayerFaction(player);
        
        boolean faction_chat = false;
        if(event.getMessage().trim().startsWith(mPlugin.FACTION_CHAT_PREFIX) && player_faction != null) {
        	event.setMessage(event.getMessage().substring(mPlugin.FACTION_CHAT_PREFIX.length()));
        	faction_chat = true;
        }
        
        boolean is_admin = mPlugin.IsPlayerAdmin(player) && mPlugin.GetPlayerFlagsManager().GetFlag(player, "flagadmin", false);
        boolean is_op = mPlugin.IsPlayerOP(player) && mPlugin.GetPlayerFlagsManager().GetFlag(player, "flagop", false);
        
        // get first color
        ChatColor color_1 = ChatColor.GRAY;
        if(player_faction != null)
        	color_1 = player_faction.GetChatColor();
        
        // get second color
        ChatColor color_2 = ChatColor.WHITE;
        if(is_admin)	color_2 = ChatColor.DARK_RED;
        else if(is_op)	color_2 = ChatColor.GOLD;
        
        String m = color_1 + "[";
        
        if(player_faction != null)
        	m += player_faction.GetPrefix();

        if(mPlugin.IsPlayerPremium(player)) {
        	// write golden coin  = (R)
        	m += ChatColor.GOLD + "\u00AE" + color_1;
        }
        
        if(is_admin)	m += "!";
        else if(is_op)	m += "*";
        
        m += "] " + player.getName() + ": ";
        if(faction_chat) {
        	m += (player_faction.GetChatColor() != ChatColor.YELLOW ? ChatColor.YELLOW : ChatColor.BLUE) + "<Faction chat> ";
        }
        m += color_2 + event.getMessage();
        
        for(Player r: event.getRecipients()) {
        	if(!faction_chat || mPlugin.GetFactionManager().GetPlayerFaction(r) == player_faction)
        		r.sendMessage(m);
        }
        
        Logger.getLogger("Minecraft").info(player.getName() + " said: " 
        		+ (faction_chat ? "<Faction chat> ":"") + event.getMessage());
        event.setCancelled(true);
    }
	
	FactionCraftPlugin mPlugin;
}
