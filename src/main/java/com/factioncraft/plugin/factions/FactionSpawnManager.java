package com.factioncraft.plugin.factions;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import com.factioncraft.plugin.FactionCraftPlugin;

public class FactionSpawnManager {
	public FactionSpawnManager(FactionCraftPlugin plugin) {
		mPlugin = plugin;
		mSpawns = new HashMap<String, Location>();
	}
	
	public void Load() {
		Configuration c = new Configuration(new File(mPlugin.getDataFolder(), "spawns.yml"));
		c.load();
		List<String> worlds = c.getKeys(null);
		if(worlds != null) {
			for(String world: worlds) {
				List<String> factions = c.getKeys(world);
				if(factions != null) {
					for(String faction: factions) {
						double x 	= c.getDouble(world + "." + faction + ".x", 0);
						double y 	= c.getDouble(world + "." + faction + ".y", 0);
						double z 	= c.getDouble(world + "." + faction + ".z", 0);
						float yaw 	= (float) c.getDouble(world + "." + faction + ".yaw", 0);
						float pitch	= (float) c.getDouble(world + "." + faction + ".pitch", 0);

						World w = mPlugin.getServer().getWorld(world);
						Location loc = new Location(w, x, y, z, yaw, pitch);
						SetSpawn(w, faction, loc);
					}
				}
			}
		}
	}
	
	public void Save() {
		Configuration c = new Configuration(new File(mPlugin.getDataFolder(), "spawns.yml"));
		for(String key: mSpawns.keySet()) {
			Location loc = mSpawns.get(key);
			if(loc != null) {
				c.setProperty(key + ".x", loc.getX());
				c.setProperty(key + ".y", loc.getY());
				c.setProperty(key + ".z", loc.getZ());
				c.setProperty(key + ".yaw", loc.getYaw());
				c.setProperty(key + ".pitch", loc.getPitch());
			}
		}
		c.save();
	}
	
	public void SetSpawn(World world, String faction, Location location) {
		if(world != null && faction != null && faction.length() > 0) {
			mSpawns.put(world.getName() + "." + faction, location);
		}
	}
	
	public Location GetSpawn(World world, String faction) {
		if(world == null || faction == null)
			return null;
		
		String key = world.getName() + "." + faction;
		if(mSpawns.containsKey(key)) 
			return mSpawns.get(key);
		else
			return null;
	}
	
	public Location GetSpawnInAir(World world, String faction) {
		Location loc = GetSpawn(world, faction);
		if(loc == null) 
			return null;
		Location l2 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		for(int y = l2.getBlockY(); y < 127; ++y) {
			if(world.getBlockAt(l2.getBlockX(), y, l2.getBlockZ()).getType() == Material.AIR &&
					world.getBlockAt(l2.getBlockX(), y + 1, l2.getBlockZ()).getType() == Material.AIR) {
				l2.setY(y);
				break;
			}
		}
		return l2;
	}

	public Location GetTeleportLocation(Player player) {
		if (player == null)
			return null;
		return GetTeleportLocation(player, mPlugin.GetFactionManager().GetPlayerFaction(player));
	}
	
	public Location GetTeleportLocation(Player player, Faction faction) {
		if(player == null || faction == null)
			return null;
		return GetSpawnInAir(player.getWorld(), faction.GetName());
	}
	
	public boolean Teleport(Player player, boolean to_default) {
		Location loc = GetTeleportLocation(player);
		if(loc != null)
			player.teleport(loc);
		else if(to_default)
			player.teleport(player.getWorld().getSpawnLocation());
		else
			return false;
		return true;
	}

	public boolean Teleport(Player player, Faction faction, boolean to_default) {
		Location loc = GetTeleportLocation(player, faction);
		if(loc != null)
			player.teleport(loc);
		else if(to_default)
			player.teleport(player.getWorld().getSpawnLocation());
		else
			return false;
		return true;
	}
	
	HashMap<String, Location> mSpawns;
	
	FactionCraftPlugin mPlugin;

}
