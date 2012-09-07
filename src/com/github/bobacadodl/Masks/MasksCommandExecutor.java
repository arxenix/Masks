package com.github.bobacadodl.Masks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MasksCommandExecutor implements CommandExecutor{
	private Masks masks;
	public MasksCommandExecutor(Masks plugin, Masks instance) {
		this.masks = plugin;
		{masks = instance;}
	}
 
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("masks")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length==0){
					p.sendMessage("¤l¤4===Masks Help===");
					p.sendMessage(ChatColor.BLUE+"/masks get [playername]");
					p.sendMessage(ChatColor.AQUA+"-Gives you the mask of the defined player.");
					p.sendMessage(ChatColor.BLUE+"/masks cancel");
					p.sendMessage(ChatColor.AQUA+"-Cancel disguise confirmation mode!");
					return true;
				}
				
				if(args.length>0){
					
					if(args[0].equalsIgnoreCase("get")){
						if(p.hasPermission("masks.get")==false) p.sendMessage(ChatColor.RED+"You do not have permission to do this!");
						if(args.length!=2){
							p.sendMessage(ChatColor.RED+"Invalid Arguments! Correct usage is: /masks get [playername]");
							return false;
						}
						Player playertoget = Bukkit.getServer().getPlayer(args[1]);
						if(masks.maskshash.containsKey(playertoget.getName())==false){
							p.sendMessage(ChatColor.RED+"That player has not logged onto the server before!");
							return false;
						}
						Short mapid = masks.maskshash.get(playertoget.getName());
						ItemStack playermask = new ItemStack(Material.MAP, 1 , mapid);
						p.getInventory().addItem(playermask);
						return true;
					}
					
					if(args[0].equalsIgnoreCase("cancel")){
						if(masks.confirmed.contains(p.getName())){
							masks.confirmed.remove(p.getName());
							p.sendMessage(ChatColor.GREEN+"Exited confirmation mode!");
							return true;
						}
						p.sendMessage(ChatColor.RED+"You are not in confirmation mode!");
					}
				}
			}
		}
		return false;
	}
}
