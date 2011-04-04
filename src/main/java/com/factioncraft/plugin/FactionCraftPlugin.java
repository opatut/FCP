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
		mFactionSpawnManager = new FactionSpawnManager(this);
	}
	
    public void onDisable() {
    	mFactionSpawnManager.Save();
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
		mFactionSpawnManager.Load();
		
        FactionCommands faction_commands = new FactionCommands(this);
        getCommand("factions").setExecutor(faction_commands);
        getCommand("joinfaction").setExecutor(faction_commands);
        getCommand("quitfaction").setExecutor(faction_commands);
        getCommand("factionspawn").setExecutor(faction_commands);
        getCommand("playerfaction").setExecutor(faction_commands);
        
        AdminsOpsCommands admins_ops_commands = new AdminsOpsCommands(this);
        getCommand(admins_ops_commands.ADMIN_FLAG).setExecutor(admins_ops_commands);
        getCommand(admins_ops_commands.OP_FLAG).setExecutor(admins_ops_commands);
        
        PluginManager pm = getServer().getPluginManager();
        MainPlayerListener mpl = new MainPlayerListener(this);
        FactionChatListener fcl = new FactionChatListener(this);
        pm.registerEvent(Event.Type.PLAYER_CHAT, fcl, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, mpl , Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, mpl , Priority.High, this);
        
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    private void SetupPermissions() {
        Plugin permissions_plugin = getServer().getPluginManager().getPlugin("Permissions");

        if (mPermissions == null) {
            if (permissions_plugin != null) {
                mPermissions = ((Permissions)permissions_plugin).getHandler();
            } else {
                Logger.getLogger("Minecraft").info("Permission system not detected, defaulting to OP");
            }
        }
    }
    
    public void LoadConfiguration() {
    	Configuration c = new Configuration(new File(getDataFolder(), "config.yml"));
    	c.load();
    	
    	WELCOME_MESSAGE = c.getString("general.welcome_message", WELCOME_MESSAGE);
    	FACTION_CHAT_PREFIX = c.getString("general.faction_chat_prefix", FACTION_CHAT_PREFIX);
    	
    	ADMINS_GROUP = c.getString("permissions.admins_group", ADMINS_GROUP);
    	OPS_GROUP = c.getString("permissions.ops_group", OPS_GROUP);
    	PREMIUM_PERMISSION = c.getString("permissions.premium", PREMIUM_PERMISSION);
    	
    	MERMEN_SWIM_SPEED = (float)c.getDouble("perks.mermen.swim_speed", MERMEN_SWIM_SPEED);
    	MERMEN_AXE_DAMAGE_MULTIPLIER = (float)c.getDouble("perks.mermen.axe_multiplier", MERMEN_AXE_DAMAGE_MULTIPLIER);
    	
    	HUMANS_BOW_DAMAGE_MULTIPLIER = (float)c.getDouble("perks.humans.bow_multiplier", HUMANS_BOW_DAMAGE_MULTIPLIER);
    	
    	ORCS_SWORD_DAMAGE_MULTIPLIER = (float)c.getDouble("perks.orcs.sword_multiplier", ORCS_SWORD_DAMAGE_MULTIPLIER);
    	ORCS_HEAL_IN_FIRE_DELAY = c.getInt("perks.orcs.heal_in_fire_delay", ORCS_HEAL_IN_FIRE_DELAY);
    	
    	DARKSEEKERS_MAX_FLAPS = c.getInt("perks.darkseekers.max_flaps", DARKSEEKERS_MAX_FLAPS);
    	DARKSEEKERS_FLIGHT_SPEED = (float) c.getDouble("perks.darkseekers.flight_speed", DARKSEEKERS_FLIGHT_SPEED);
    }
    
    public void SaveConfiguration() {
    	Configuration c = new Configuration(new File(getDataFolder(), "config.yml"));
    	c.load();
    	
    	c.setProperty("general.welcome_message", WELCOME_MESSAGE);
    	c.setProperty("general.faction_chat_prefix", FACTION_CHAT_PREFIX);
    	
    	c.setProperty("permissions.admins_group", ADMINS_GROUP);
    	c.setProperty("permissions.ops_group", OPS_GROUP);
    	c.setProperty("permissions.premium", PREMIUM_PERMISSION);
    	
    	c.setProperty("perks.mermen.swim_speed", MERMEN_SWIM_SPEED);
    	c.setProperty("perks.mermen.axe_multiplier", MERMEN_AXE_DAMAGE_MULTIPLIER);
    	
    	c.setProperty("perks.humans.bow_multiplier", HUMANS_BOW_DAMAGE_MULTIPLIER);
    	
    	c.setProperty("perks.orcs.sword_multiplier", ORCS_SWORD_DAMAGE_MULTIPLIER);
    	c.setProperty("perks.orcs.heal_in_fire_delay", ORCS_HEAL_IN_FIRE_DELAY);
    	
    	c.setProperty("perks.darkseekers.max_flaps", DARKSEEKERS_MAX_FLAPS);
    	c.setProperty("perks.darkseekers.flight_speed", DARKSEEKERS_FLIGHT_SPEED);
    	
    	c.save();
    }
    
    public void LoadFactions() {
    	LoadFaction(new Mermen(this));
    	LoadFaction(new Humans(this));
    	LoadFaction(new Orcs(this));
    	LoadFaction(new Darkseekers(this));
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
    
    public FactionSpawnManager GetFactionSpawnManager() {
    	return mFactionSpawnManager;
    }
    
    public boolean IsPlayerAdmin(Player player) {
    	if(mPermissions != null) {
    		// Permissions plugin enabled
    		return mPermissions.inGroup(player.getWorld().getName(), player.getName(), ADMINS_GROUP);
    	} else {
    		// return whether user is OP (default minecraft feature)
    		return player.isOp();
    	}
    }
    
    public boolean IsPlayerOP(Player player) {
    	// all admis are also OP
    	if(IsPlayerAdmin(player))
    		return true;
    	
    	if(mPermissions != null) {
    		// Permissions plugin enabled
    		// (also returns true if user is admins, as the admins group should inherit from ops)
    		return mPermissions.inGroup(player.getWorld().getName(), player.getName(), OPS_GROUP);
    	} else {
    		// return whether user is OP (default minecraft feature)
    		return player.isOp();
    	}
    }
    
    public boolean IsPlayerPremium(Player player) {
    	if(mPermissions != null) {
    		// Permissions plugin enabled
    		return mPermissions.has(player, PREMIUM_PERMISSION);
    	}
    	// if no Permissions plugin available, premium features are disabled
		return false;
    }
    
    public Faction GetFactionByName(String name) {
    	// try directly by key
    	if(mFactions.containsKey(name)) {
    		return mFactions.get(name);
    	}
    	// try by key, ignore case
		for(String faction: mFactions.keySet()) {
			if(faction.equalsIgnoreCase(name)) {
				return mFactions.get(faction);
			}
		}
		// try by aliases
		for(String faction: mFactions.keySet()) {
			Faction f = mFactions.get(faction);
			if(f.MatchesAliasesIgnoreCase(name)) {
				return f;
			}
		}
		// no luck :D
    	return null;
	}

    private HashMap<String, Faction> mFactions;
    private FactionManager mFactionManager;
    private PlayerFlagsManager mPlayerFlagsManager;
    private FactionSpawnManager mFactionSpawnManager;
    
    public PermissionHandler mPermissions;
    public String ADMINS_GROUP = "Admins"; 
    public String OPS_GROUP = "Moderators"; 
    public String PREMIUM_PERMISSION = "factioncraft.premium";
    
    public float MERMEN_SWIM_SPEED = 1.3F;
    public float MERMEN_AXE_DAMAGE_MULTIPLIER = 1.2F;
    public float HUMANS_BOW_DAMAGE_MULTIPLIER = 1.3F;
    public float ORCS_SWORD_DAMAGE_MULTIPLIER = 1.2F;
    public int ORCS_HEAL_IN_FIRE_DELAY = 8;
    public float DARKSEEKERS_FLIGHT_SPEED = 0.6F;
    public int DARKSEEKERS_MAX_FLAPS = 10;
    
    public String WELCOME_MESSAGE = "Welcome back, %name%!<br>Your friends, the %faction% are with you.";
    public String FACTION_CHAT_PREFIX = "_";
}
