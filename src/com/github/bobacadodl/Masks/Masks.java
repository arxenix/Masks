package com.github.bobacadodl.Masks;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.HashBiMap;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class Masks extends JavaPlugin{
	public HashBiMap<String,Short> maskshash = HashBiMap.<String,Short>create();
	private MasksCommandExecutor myExecutor;
	public DisguiseCraftAPI dcAPI;
	public MasksListener maskl;
	public List<String> confirmed = new ArrayList<String>();
	
	public void onEnable(){ 
		this.getLogger().log(Level.INFO,"Masks 0.1 by bobacadodl enabled!");
		
		//loading the masks hashmap
		if(getConfig().getInt("Config-Version")==1){
			
			if(getConfig().getShortList("maskshorts").size()>0){
				List<Short> maskshorts = getConfig().getShortList("maskshorts");
				List<String> maskplayers = getConfig().getStringList("maskplayers");
				int loadedmasks = 0;
				for(int i=0 ; i< maskshorts.size() ; i++){
					Short x=maskshorts.get(i);
					String y = maskplayers.get(i);
					maskshash.put(y, x);
					loadedmasks = loadedmasks+1;
				}
				if(loadedmasks>0) this.getLogger().log(Level.INFO, Integer.toString(loadedmasks)+ " Masks successfuly loaded!");
			}
			
			//updating all the maps!
			//if(maskshash.keySet().size()>0){
			//	List<String> maskednames = new ArrayList<String>(maskshash.keySet());
			//	for (int i = 0; i<maskednames.size(); i++){
			//		updateMap(maskshash.get(maskednames.get(i)), maskednames.get(i));
			//	}
			//}
			updateMaps(maskshash);
			
		}
		
		//loading config
		loadConfiguration();
		
		//registering command executor
		myExecutor = new MasksCommandExecutor(this,this);
		getCommand("masks").setExecutor(myExecutor);
		
		//registering events
		getServer().getPluginManager().registerEvents(new MasksListener(this), this);
		setupDisguiseCraft();
	}
	
	public void onDisable(){
		//saving the masks hashmap to disk
		this.getLogger().log(Level.INFO,"Masks 0.1 by bobacadodl disabled!");
		List<Short> maskshorts = new ArrayList<Short>(maskshash.values());
		List<String> maskplayers = new ArrayList<String>(maskshash.inverse().values());
		getConfig().set("maskshorts", maskshorts);
		getConfig().set("maskplayers", maskplayers);
		saveConfig();
	}
	
	public void loadConfiguration(){
		if(getConfig().getInt("Config-Version")!=1){
			getConfig().set("Item-to-kill", 1);
			getConfig().set("Enable-instant-kill", true);
			getConfig().set("Config-Version", 1);
		}
	    getConfig().options().copyDefaults(true);
	    saveConfig();
	}
	
	public void setupDisguiseCraft() {
		dcAPI = DisguiseCraft.getAPI();
	}
	
	public void updateMap(short id, String pname){
		MapView map = Bukkit.getServer().getMap(id);
		MasksMapRenderer maskr = new MasksMapRenderer (this, pname);
		maskr.initialize(map);
		for (int i =0; i<map.getRenderers().size();i++){
			map.removeRenderer(map.getRenderers().get(i));
		}
		map.addRenderer(maskr);
	}
	public void updateMaps(HashBiMap<String,Short> maps){
		List<String> pnames = new ArrayList<String>(maps.keySet());
		List<Short> maskids = new ArrayList<Short>(maps.inverse().keySet());
		int updatedmaps = 0;
		for(int i =0; i<pnames.size(); i++){
			String pname = pnames.get(i);
			Short id = maskids.get(i);
			MapView map = Bukkit.getServer().getMap(id);
			MasksMapRenderer maskr = new MasksMapRenderer (this, pname);
			maskr.initialize(map);
			for( int j =0 ; j<map.getRenderers().size(); j++){
				map.removeRenderer(map.getRenderers().get(j));
			}
			map.getRenderers().clear();
			map.addRenderer(maskr);
			updatedmaps = updatedmaps+1;
		}
		if(updatedmaps>0) this.getLogger().log(Level.INFO, Integer.toString(updatedmaps)+ " Maps updated!");
		
	}
	
	public short createMap(Player p){
		MapView map = Bukkit.getServer().createMap(p.getWorld());
		map.setCenterX(Integer.MAX_VALUE);
		map.setCenterZ(Integer.MAX_VALUE);
		MasksMapRenderer maskr = new MasksMapRenderer (this, p.getName());
		maskr.initialize(map);
		map.getRenderers().clear();
		map.addRenderer(maskr);
		return map.getId();
	}
	

}
