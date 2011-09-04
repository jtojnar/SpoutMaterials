package cz.ogion.ultraitems;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.config.ConfigurationNode;
import org.getspout.spoutapi.SpoutManager;

public class UltraItems extends JavaPlugin {
	public Map<String, ConfigurationNode> config;
	Logger log = Logger.getLogger("Minecraft");
	PluginDescriptionFile pdfile;
	PlayerListener playerListener;

	public void onDisable() {}

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pdfile = this.getDescription();
		playerListener = new PlayerListener(this);
		pm.registerEvent(Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Monitor, this);
		loadConfig();
		log.info(pdfile.getFullName()+" was enabled");
	}

	public void loadConfig() {
		getConfiguration().load();
		config = this.getConfiguration().getNodes("UltraItems");
		if (config != null) {
			for(ConfigurationNode item : config.values()) {
				// TODO: exception handling
				String url = item.getString("url", null);
				Integer itemid = item.getInt("item", 0);
				Short itemdata = ((Integer) item.getInt("data", 0)).shortValue();
				String title = item.getString("title", null);
				SpoutManager.getFileManager().addToCache(this, url);
				SpoutManager.getItemManager().setItemTexture(new MaterialData(itemid).getItemType(), itemdata, this, url);
				SpoutManager.getItemManager().setItemName(new MaterialData(itemid).getItemType(), itemdata, title);
				log.info("["+pdfile.getName()+"] Added new item:"+item.getAll().toString());
				// TODO: add to general
				// TODO: crafting recipes
			}
		} else {
			this.getConfiguration().setProperty("UltraItems", null);
			getConfiguration().save();
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// TODO: Test permissions support
		// TODO: console
		Player who = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("ultraitems")){
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					// TODO: reload command
					if(who.hasPermission("ultraitems.reload")) {
						loadConfig();
						who.sendMessage("Config reloaded.");
						return true;
					} else {
						who.sendMessage("You don't have permission for reloading config.");
					}
				} else if(args[0].equalsIgnoreCase("list")) {
					if(who.hasPermission("ultraitems.list")) {
						StringBuilder sb = new StringBuilder();
						for(String s : config.keySet()) {
							sb.append(s);
							sb.append(" ");
						}
						who.sendMessage("UltraItems: " + sb.toString());
						return true;
					} else {
						who.sendMessage("You don't have permission for list of ultra items.");
					}
				} else if(config.containsKey(args[0])) {
					if (who.hasPermission("ultraitems.give")){
						if (who.hasPermission("ultraitems.give.*") || who.hasPermission("ultraitems.give."+args[0])){
							try {
								ConfigurationNode item = config.get(args[0]);
								Integer itemid = item.getInt("item", 0);
								Short itemdata = ((Integer)item.getInt("data", 0)).shortValue();
								log.info("Giving " + args[0] + itemid+":"+itemdata);
								if(itemid != 0 && itemdata != 0) {
									ItemStack stack = new ItemStack(itemid, 1, itemdata);
									int slot = who.getInventory().firstEmpty();
									if(slot < 0) {
										who.getWorld().dropItem(who.getLocation(), stack);
									} else {
										who.getInventory().addItem(stack);
									}
									who.sendMessage("Here you are!");
								} else {
									who.sendMessage(args[0]+" has incorrectly set data! Please contact server admin.");
								}
								return true;
							} catch (Exception e) {
								who.sendMessage("Error:"+e.getMessage());
							}
						} else {
							who.sendMessage("You don't have permission to get " + args[0] + ".");
						}
					} else {
						who.sendMessage("You don't have permission to get ultra items.");
					}
				} else {
					who.sendMessage(args[0] + " isn't neither command or item!");
				}
			}
		}
		return false;
	}
}