package com.factioncraft.plugin.factions;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class GiantTreeGenerator {
	public static void GenerateTree(Location root_position) {
		int rx = root_position.getBlockX();
		int ry = root_position.getBlockY();
		int rz = root_position.getBlockZ();
		
		mWorld = root_position.getWorld();
		mWoodType = 1;
		
		int min_radius = 0;
		int max_radius = 2;
		
		int height = 40;
		if(ry + height > 128)
			height = 128 - ry;
		
		for(int yd = 0; yd < height; ++yd) {
			float radius = (1F * (max_radius - min_radius) * (1F*(height-yd)/height)) + min_radius;
			radius *= RandomFloatBetween(0.9F, 1.2F);
			WoodCircle(rx, ry + yd, rz, radius);
		}
		
	}
	
	public static void WoodCircle(int x, int y, int z, float r) {
		for(float phi = 0; phi < 2 * Math.PI; phi += 1F / r) {
			float dx = (float)Math.sin(phi) * r * RandomFloatBetween(0.95F, 1.05F);
			float dz = (float)Math.cos(phi) * r * RandomFloatBetween(0.95F, 1.05F);
			SetWood(x + (int)Math.round(dx), y, z + (int)Math.round(dz));
		}
	}
	
	public static float RandomFloatBetween(float l, float r) {
		return mRandom.nextFloat() * (r-l) + l; 
	}
	
	public static void SetWood(int x, int y, int z) {
		Block b = mWorld.getBlockAt(x, y, z);
		if(b.getType() == Material.AIR) {
			b.setType(Material.LOG);
			b.setData(mWoodType);
		}
	}

	private static Random mRandom = new Random();
	private static World mWorld;
	private static byte mWoodType;
}
