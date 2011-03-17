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
        boolean is_admin = player.getName().equals("ryanemm10");
        String faction = "Mermen";
        if(player.getName().equals("opatut")) {
        	faction = "Humans";
        }
        Faction player_faction = mPlugin.GetFactions().get(faction);
        String m = player_faction.GetChatColor() + "[" + player_faction.GetPrefix();
        if(is_admin)
        	m += "!";
        else if(player.isOp()) 
        	m += "*";
        m += "] " + player.getDisplayName() + ": ";
        if(is_admin) 
        	m += ChatColor.RED;
        else if(player.isOp())
        	m += ChatColor.GOLD;
        else
        	m += ChatColor.WHITE;
        
        m += event.getMessage();
        
        for(Player r: event.getRecipients()) {
        	r.sendMessage(m);
        }
        Logger.getLogger("Minecraft").info("- " + player.getDisplayName() + ": " + event.getMessage());
        event.setCancelled(true);
    }
	
	FactionCraftPlugin mPlugin;
}
