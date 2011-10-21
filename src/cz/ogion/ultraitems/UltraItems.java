package cz.ogion.ultraitems;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.inventory.SpoutShapelessRecipe;
import org.getspout.spoutapi.material.CustomItem;

public class UltraItems extends JavaPlugin {
	public ConfigurationSection config;
	Logger log = Logger.getLogger("Minecraft");
	private PluginDescriptionFile pdfile;
	PlayerListener playerListener;
	EntityListener entityListener;
	BlockListener blockListener;
	ItemManager itemManager;

	@Override
	public void onDisable() {}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pdfile = this.getDescription();
		playerListener = new PlayerListener(this);
		entityListener = new EntityListener(this);
		blockListener = new BlockListener(this);
		itemManager = new ItemManager(this);
		pm.registerEvent(Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.ENTITY_DAMAGE, this.entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_DAMAGE, this.blockListener, Event.Priority.Normal, this);
		new SpoutInventoryListener(this);
		loadConfig();
		log.info(pdfile.getFullName() + " was enabled");
	}

	// TODO: reset textures and titles
	@SuppressWarnings("unchecked")
	public void loadConfig() {
		config = this.getConfig().getConfigurationSection("UltraItems");
		// TODO: empty config
		if (config != null) {
			for(Entry<String, Object> item : config.getValues(false).entrySet()) {
				try {
					ConfigurationSection value = (ConfigurationSection) item.getValue();
					String name = item.getKey();
					String url = value.getString("url", null);
					String title = value.getString("title", null);
					CustomItem ci = itemManager.addItem(ItemType.GENERIC_ITEM, name, title, url).setConfig(value).getCustomItem();

					List<Map<String, Object>> recipes = value.getList("recipes");
					if (recipes != null) {
						for (Map<String, Object> recipe : recipes) {
							String type = (String) recipe.get("type");
							Integer amount = (Integer) recipe.get("amount");
							if (amount == null) {
								amount = 1;
							}
							if (type.equalsIgnoreCase("furnace")) {
								try {
									Ingredient ingredient = new Ingredient((String) recipe.get("ingredients"), this);
									FurnaceRecipe rcp = new FurnaceRecipe(SpoutManager.getMaterialManager().getCustomItemStack(ci, amount), new MaterialData(ingredient.getOldMaterial(), ingredient.getDataByte()));
									Bukkit.getServer().addRecipe(rcp);
									log.info("[" + pdfile.getName() + "] " + "Added furnace recipe for " + name);
								} catch (Exception e) {
									log.warning("[" + pdfile.getName() + "] " + e.getMessage());
								}
							} else if (type.equalsIgnoreCase("shaped")) {
								SpoutShapedRecipe rcp = new SpoutShapedRecipe(SpoutManager.getMaterialManager().getCustomItemStack(ci, amount)).shape("abc", "def", "ghi");
								String ingredients = (String) recipe.get("ingredients");
								doRecipe(rcp, ingredients);
								log.info("[" + pdfile.getName() + "] " + "Added shaped recipe for " + name);
							} else if (type.equalsIgnoreCase("shapeless")) {
								SpoutShapelessRecipe rcp = new SpoutShapelessRecipe(SpoutManager.getMaterialManager().getCustomItemStack(ci, amount));
								String ingredients = (String) recipe.get("ingredients");
								doRecipe(rcp, ingredients);
								log.info("[" + pdfile.getName() + "] " + "Added shapeless recipe for " + name);
							} else {
								log.warning("[" + pdfile.getName() + "] " + "You have to specify valid type of recipe (furnace, shaped, shapeless) for " + name);
							}
						}
					}
					log.info("[" + pdfile.getName() + "] " + "Added item "+name+" ("+ci.getRawId()+":"+ci.getRawData()+")");
					// TODO: add to general
				} catch (NoSuchMethodError e) {
					log.log(Level.SEVERE, "[" + pdfile.getName() + "] " + "NoSuchMethod Error. This is probably because your spout doesn't support required api, please upgrade to dev version. If you have dev version report the error bellow:");
					e.printStackTrace();
				} catch (DataFormatException e) {
					log.warning("[" + pdfile.getName() + "] " + e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			getConfig().createSection("UltraItems:");
			saveConfig();
		}
	}
	private void doRecipe(Recipe rcp, String ingredients) throws Exception {
		Integer curline = 0;
		Integer curcol = 0;
		ingredients = ingredients.replaceAll("\\s{2,}", " ");
		for (String line : ingredients.split("\\r?\\n")){
			if (curline < 3) {
				for (String ingredientitem : line.split(" ")){
					if (curcol < 3) {
						char a = (char) ('a' + curcol + curline * 3);
						Ingredient ingredient = new Ingredient(ingredientitem, this);
						curcol++;
						try{
							if (Integer.decode(ingredientitem)==0) {
								continue;
							}
						} catch (NumberFormatException e) {

						}
						if(rcp instanceof SpoutShapedRecipe) {
							((SpoutShapedRecipe) rcp).setIngredient(a, ingredient.getMaterial());
						} else {
							((SpoutShapelessRecipe) rcp).addIngredient(ingredient.getMaterial());
						}
					}
				}
				curcol = 0;
			}
			curline++;
		}
		SpoutManager.getMaterialManager().registerSpoutRecipe(rcp);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ultraitems")){
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("ultraitems.reload")) {
						loadConfig();
						sender.sendMessage(ChatColor.GREEN + "Config reloaded.");
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have permission for reloading config.");
					}
					return true;
				} else if(args[0].equalsIgnoreCase("list")) {
					if(sender.hasPermission("ultraitems.list")) {
						StringBuilder sb = new StringBuilder();
						for(String s : config.getKeys(false)) {
							sb.append(s);
							sb.append(" ");
						}
						sender.sendMessage("UltraItems: " + ChatColor.YELLOW + sb.toString());
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have permission for list of ultra items.");
					}
					return true;
				} else if(args[0].equalsIgnoreCase("replace")) {
					if (sender instanceof ConsoleCommandSender){
						sender.sendMessage(ChatColor.RED + "This command must be run in-game.");
					}  else {
						Player who = (Player) sender;
						for(Entry<String, Object> item : config.getValues(false).entrySet()) {
							try {
								String name = item.getKey();
								ConfigurationSection value = (ConfigurationSection) item.getValue();
								Integer itemid = value.getInt("itemid", 0);
								Integer data = value.getInt("data", 0);
								Integer amount = 0;
								if (itemid == 0 || data == 0) {
									throw new Exception("");
								}
								Map<Integer, ? extends ItemStack> iitems = who.getInventory().all(itemid);
								for (int i : iitems.keySet()) {
									ItemStack is = iitems.get(i);
									if (itemid == is.getTypeId() && ((Short) is.getDurability()).intValue() == data) {
										amount += is.getAmount();
										who.getInventory().setItem(i, null);
									}
								}
								while (amount > 0) {
									int a = 0;
									if (amount >= 64) {
										a = 64;
									} else {
										a = amount;
									}
									amount -= a;
									who.getInventory().addItem(SpoutManager.getMaterialManager().getCustomItemStack(itemManager.getItem(name).getCustomItem(), a));
								}
							} catch (Exception e) {
							}
						}
						sender.sendMessage("UltraItems: " + ChatColor.YELLOW + "Items from your inventory were replaced");
					}
					return true;
				} else if(config.contains(args[0])) {
					if (sender instanceof ConsoleCommandSender){
						sender.sendMessage(ChatColor.RED + "This command must be run in-game.");
					} else {
						Player who = (Player) sender;
						if (who.hasPermission("ultraitems.give")){
							if (who.hasPermission("ultraitems.give.*") || who.hasPermission("ultraitems.give."+args[0])){
								try {
									CustomItem ci = itemManager.getItem(args[0]).getCustomItem();
									ItemStack stack = SpoutManager.getMaterialManager().getCustomItemStack(ci, 1);
									int slot = who.getInventory().firstEmpty();
									if(slot < 0) {
										who.getWorld().dropItem(who.getLocation(), stack);
									} else {
										who.getInventory().addItem(stack);
									}
									sender.sendMessage(ChatColor.GREEN + "Here you are!");
								} catch (Exception e) {
									sender.sendMessage(ChatColor.RED + "Error:"+e.getMessage());
									e.printStackTrace();
								}
							} else {
								sender.sendMessage(ChatColor.RED + "You don't have permission to get " + args[0] + ".");
							}
						} else {
							who.sendMessage(ChatColor.RED + "You don't have permission to get ultra items.");
						}
					}
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + args[0] + " isn't neither command or item!");
				}
			}
		}
		return false;
	}
}