package com.factioncraft.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.factioncraft.plugin.commands.*;
import com.factioncraft.plugin.factions.*;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class FactionCraftPlugin extends JavaPlugin {
	public FactionCraftPlugin() {
		mFactions = new HashMap<String, Faction>();
		mFactionManager = new FactionManager(this);
		mPlayerFlagsManager = new PlayerFlagsManager(this);
	}
	
    public void onDisable() {
    	mPlayerFlagsManager.Save();
    	mFactionManager.Save();
    	SaveConfiguration();
        //PluginManager pm = getServer().getPluginManager();
    }

    public void onEnable() { 	
		// Link with permissions plugin
        SetupPermissions();
        
        // Load configuration
        LoadConfiguration();
        
        // Load and enable factions
        LoadFactions();
        EnableFactions();
        
        // Load player's factions now as the factions are already available
		mFactionManager.Load();
		mPlayerFlagsManager.Load();
		
        FactionCommands faction_commands = new FactionCommands(this);
        getCommand("factions").setExecutor(faction_commands);
        getCommand("joinfaction").setExecutor(faction_commands);
        getCommand("quitfaction").setExecutor(faction_commands);
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_CHAT, new FactionChatListener(this), Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, new MainPlayerListener(this), Priority.Normal, this);
        
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    private void SetupPermissions() {
        Plugin permissions_plugin = getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (permissions_plugin != null) {
                Permissions = ((Permissions)permissions_plugin).getHandler();
            } else {
                Logger.getLogger("Minecraft").info("Permission system not detected, defaulting to OP");
            }
        }
    }
    
    public void LoadConfiguration() {
    	Configuration c = new Configuration(new File(getDataFolder(), "config.yml"));
    	c.load();
    	ADMINS_GROUP = c.getString("permissions.admins_group", ADMINS_GROUP);
    	OPS_GROUP = c.getString("permissions.ops_group", OPS_GROUP);
    	PREMIUM_PERMISSION = c.getString("permissions.premium", PREMIUM_PERMISSION);
    	MERMEN_SWIM_SPEED = (float)c.getDouble("perks.mermen.swim_speed", MERMEN_SWIM_SPEED);
    }
    
    public void SaveConfiguration() {
    	Configuration c = new Configuration(new File(getDataFolder(), "config.yml"));
    	c.load();
    	c.setProperty("permissions.admins_group", ADMINS_GROUP);
    	c.setProperty("permissions.ops_group", OPS_GROUP);
    	c.setProperty("permissions.premium", PREMIUM_PERMISSION);
    	c.setProperty("perks.mermen.swim_speed", MERMEN_SWIM_SPEED);
    	c.save();
    }
    
    public void LoadFactions() {
    	LoadFaction(new Mermen(this));
    	LoadFaction(new Humans(this));
    	LoadFaction(new Orcs(this));
    }
    
    public void LoadFaction(Faction faction) {
    	mFactions.put(faction.GetName(), faction);
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
    
    public PlayerFlagsManager GetPlayerFlagsManager() {
    	return mPlayerFlagsManager;
    }
    
    public boolean IsPlayerAdmin(Player player) {
    	if(Permissions != null) {
    		// Permissions plugin enabled
    		return Permissions.inGroup(player.getWorld().getName(), player.getName(), ADMINS_GROUP);
    	} else {
    		// return whether user is OP (default minecraft feature)
    		return player.isOp();
    	}
    }
    
    public boolean IsPlayerOP(Player player) {
    	// all admis are also OP
    	if(IsPlayerAdmin(player))
    		return true;
    	
    	if(Permissions != null) {
    		// Permissions plugin enabled
    		// (also returns true if user is admins, as the admins group should inherit from ops)
    		return Permissions.inGroup(player.getWorld().getName(), player.getName(), OPS_GROUP);
    	} else {
    		// return whether user is OP (default minecraft feature)
    		return player.isOp();
    	}
    }
    
    public boolean IsPlayerPermium(Player player) {
    	if(Permissions != null) {
    		// Permissions plugin enabled
    		return Permissions.has(player, PREMIUM_PERMISSION);
    	}
    	// if no Permissions plugin available, premium features are disabled
		return false;
    }
    
    

    private HashMap<String, Faction> mFactions;
    private FactionManager mFactionManager;
    private PlayerFlagsManager mPlayerFlagsManager;
    
    public static PermissionHandler Permissions;
    public String ADMINS_GROUP = "Admins"; 
    public String OPS_GROUP = "Moderators"; 
    public String PREMIUM_PERMISSION = "factioncraft.premium";
    public float MERMEN_SWIM_SPEED = 1.3F;
}
