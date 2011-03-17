package com.factioncraft.plugin;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.factioncraft.plugin.commands.*;
import com.factioncraft.plugin.factions.*;

public class FactionCraftPlugin extends JavaPlugin {
	public FactionCraftPlugin() {
		mFactions = new HashMap<String, Faction>();
		mFactionManager = new FactionManager(this);
	}
	
    public void onDisable() {
    	mFactionManager.Save();
        //PluginManager pm = getServer().getPluginManager();
    }

    public void onEnable() { 	
		mFactionManager.Load();
       
        // Load and enable factions
        LoadFactions();
        EnableFactions();

        FactionCommands faction_commands = new FactionCommands(this);
        getCommand("factions").setExecutor(faction_commands);
        getCommand("joinfaction").setExecutor(faction_commands);
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_CHAT, new FactionChatListener(this), Priority.Highest, this);
        
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    public void LoadFactions() {
    	mFactions.put("Mermen", new Mermen(this));
    	mFactions.put("Humans", new Humans(this));
    	//mFactions.put("Orcs", new Orcs(this));
    }
    
    public void EnableFactions() {
    	for(Faction f: mFactions.values()) {
    		f.OnEnable();
    	}
    }
    
    public HashMap<String, Faction> GetFactions() {
    	return mFactions;
    }
    
    public FactionManager GetFactionManager() {
    	return mFactionManager;
    }

    private HashMap<String, Faction> mFactions;
    private FactionManager mFactionManager;
}
