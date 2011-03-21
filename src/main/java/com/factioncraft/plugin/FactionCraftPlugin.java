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
        
        AdminsOpsCommands admins_ops_commands = new AdminsOpsCommands(this);
        getCommand(admins_ops_commands.ADMIN_FLAG).setExecutor(admins_ops_commands);
        getCommand(admins_ops_commands.OP_FLAG).setExecutor(admins_ops_commands);
        
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
    	PREMIUM_GROUP = c.getString("permissions.premium_group", PREMIUM_GROUP);
    	PLAYER_GROUP = c.getString("permissions.player_group", PLAYER_GROUP);
    	
    	MERMEN_SWIM_SPEED = (float)c.getDouble("perks.mermen.swim_speed", MERMEN_SWIM_SPEED);
    	MERMEN_AXE_DAMAGE_MULTIPLIER = (float)c.getDouble("perks.mermen.axe_multiplier", MERMEN_AXE_DAMAGE_MULTIPLIER);
    	
    	HUMANS_BOW_DAMAGE_MULTIPLIER = (float)c.getDouble("perks.humans.bow_multiplier", HUMANS_BOW_DAMAGE_MULTIPLIER);
    	
    	ORCS_SWORD_DAMAGE_MULTIPLIER = (float)c.getDouble("perks.orcs.sword_multiplier", ORCS_SWORD_DAMAGE_MULTIPLIER);
    	ORCS_HEAL_IN_FIRE_DELAY = c.getInt("perks.orcs.heal_in_fire_delay", ORCS_HEAL_IN_FIRE_DELAY);
    }
    
    public void SaveConfiguration() {
    	Configuration c = new Configuration(new File(getDataFolder(), "config.yml"));
    	c.load();
    	
    	c.setProperty("permissions.admins_group", ADMINS_GROUP);
    	c.setProperty("permissions.ops_group", OPS_GROUP);
    	c.setProperty("permissions.premium_group", PREMIUM_GROUP);
    	c.setProperty("permissions.player_group", PLAYER_GROUP);
    	
    	c.setProperty("perks.mermen.swim_speed", MERMEN_SWIM_SPEED);
    	c.setProperty("perks.mermen.axe_multiplier", MERMEN_AXE_DAMAGE_MULTIPLIER);
    	
    	c.setProperty("perks.humans.bow_multiplier", HUMANS_BOW_DAMAGE_MULTIPLIER);
    	
    	c.setProperty("perks.orcs.sword_multiplier", ORCS_SWORD_DAMAGE_MULTIPLIER);
    	c.setProperty("perks.orcs.heal_in_fire_delay", ORCS_HEAL_IN_FIRE_DELAY);
    	
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
    
    public boolean IsPlayerPremium(Player player) {
    	if(Permissions != null) {
    		// Permissions plugin enabled
    		return Permissions.inGroup(player.getWorld().getName(), player.getName(), PREMIUM_GROUP);
    	}
    	// if no Permissions plugin available, premium features are disabled
		return false;
    }
    
    public void TogglePlayerPremium(Player player) {
    	SetPlayerPremium(player, ! IsPlayerPremium(player));
    }
    
    public void SetPlayerPremium(Player player, boolean premium) {
    	String group = premium ? PREMIUM_GROUP : PLAYER_GROUP;
    	SetPermissionsPlayerGroup(player, group);
    }
    
    public void SetPermissionsPlayerPermission(Player player, String permissions, boolean value) {
    	// set or unset player's value
    }
    
    public void SetPermissionsPlayerGroup(Player player, String group) {
    	String w = player.getWorld().getName();
    	
    	Configuration c = new Configuration(new File(getDataFolder().getParentFile(), "Permissions" + File.pathSeparator + w + ".yml"));
    	c.load();
    	
    	c.setProperty("users."+player.getName()+".group", group);
    	
    	c.save();
    }
    

    private HashMap<String, Faction> mFactions;
    private FactionManager mFactionManager;
    private PlayerFlagsManager mPlayerFlagsManager;
    
    public static PermissionHandler Permissions;
    public String ADMINS_GROUP = "Admins"; 
    public String OPS_GROUP = "Moderators"; 
    public String PREMIUM_GROUP = "Donators";
    public String PLAYER_GROUP = "Players";
    
    public float MERMEN_SWIM_SPEED = 1.3F;
    public float MERMEN_AXE_DAMAGE_MULTIPLIER = 1.2F;
    
    public float HUMANS_BOW_DAMAGE_MULTIPLIER = 1.3F;
    
    public float ORCS_SWORD_DAMAGE_MULTIPLIER = 1.2F;
    public int ORCS_HEAL_IN_FIRE_DELAY = 8;
}
