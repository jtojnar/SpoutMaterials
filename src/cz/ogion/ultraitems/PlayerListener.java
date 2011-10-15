package cz.ogion.ultraitems;

import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.CustomItem;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	UltraItems plugin;
	ConfigurationSection config;
	Logger log = Logger.getLogger("Minecraft");

	public PlayerListener(UltraItems instance) {
		plugin = instance;
	}
	public void onPlayerInteract(PlayerInteractEvent event) {
		config = plugin.config;
		if(event.hasItem()){
			if (config != null) {
				Action action = event.getAction();
				Player player = event.getPlayer();
				Integer eventitemid = event.getItem().getTypeId();
				for(Entry<String, Object> item : config.getValues(false).entrySet()) {
					ConfigurationSection value = (ConfigurationSection) item.getValue();
					CustomItem ci = plugin.items.get(item.getKey());
					ConfigurationSection lclick = value.getConfigurationSection("lclick");
					ConfigurationSection rclick = value.getConfigurationSection("rclick");
					if(eventitemid != 0 && SpoutManager.getMaterialManager().isCustomItem(event.getItem()) && SpoutManager.getMaterialManager().getCustomItem(event.getItem()) == ci) {
						if((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && lclick != null) {
							if (lclick.getString("action", null) != null) {
								String permissionbypass = lclick.getString("permissionbypass", null);
								if(permissionbypass != null){
									PermissionAttachment attachment = player.addAttachment(plugin);
									attachment.setPermission(permissionbypass, true);
									player.chat(lclick.getString("action"));
									attachment.unsetPermission(permissionbypass);
									player.removeAttachment(attachment);
								} else {
									player.chat(lclick.getString("action"));
								}
							}
							// TODO: delay
							if (lclick.getBoolean("consume", false)) {
								ItemStack is = player.getItemInHand();
								is.setAmount(is.getAmount() - 1);
							//	player.setItemInHand(is);
							}
							if (lclick.getInt("health", 0) != 0) {
								EntityRegainHealthEvent regainevent = new EntityRegainHealthEvent(player, lclick.getInt("health", 0), RegainReason.CUSTOM);
								Integer newhealth = player.getHealth() + regainevent.getAmount();
								if (newhealth < 0) {
									newhealth = 0;
								} else if (newhealth > 20) {
									newhealth = 20;
								}
								regainevent.setAmount(newhealth - player.getHealth());
								Bukkit.getServer().getPluginManager().callEvent(regainevent);
								if (!regainevent.isCancelled()) {
									player.setHealth(newhealth);
								}
							}
							if (lclick.getInt("hunger", 0) != 0) {
								FoodLevelChangeEvent flchangeevent = new FoodLevelChangeEvent(player, lclick.getInt("hunger", 0));
								Integer newhunger = player.getFoodLevel() + flchangeevent.getFoodLevel();
								if (newhunger < 0) {
									newhunger = 0;
								} else if (newhunger> 20) {
									newhunger = 20;
								}
								flchangeevent.setFoodLevel(newhunger - player.getFoodLevel());
								Bukkit.getServer().getPluginManager().callEvent(flchangeevent);
								if (!flchangeevent.isCancelled()) {
									player.setFoodLevel(newhunger);
								}
							}
							if (lclick.getString("sound", null) != null){
								SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, lclick.getString("sound"), false, player.getLocation());
							}
							event.setCancelled(true);
							event.setUseInteractedBlock(Result.DENY);
							event.setUseItemInHand(Result.DENY);
						} else if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && rclick != null) {
							if (rclick.getString("action", null) != null) {
								String permissionbypass = rclick.getString("permissionbypass", null);
								if(permissionbypass != null){
									PermissionAttachment attachment = player.addAttachment(plugin);
									attachment.setPermission(permissionbypass, true);
									player.chat(rclick.getString("action"));
									attachment.unsetPermission(permissionbypass);
									player.removeAttachment(attachment);
								} else {
									player.chat(rclick.getString("action"));
								}
							}
							if (rclick.getBoolean("consume", false)) {
								ItemStack is = player.getItemInHand();
								is.setAmount(is.getAmount() - 1);
								player.setItemInHand(is);
							}
							if (rclick.getInt("health", 0) != 0) {
								EntityRegainHealthEvent regainevent = new EntityRegainHealthEvent(player, rclick.getInt("health", 0), RegainReason.CUSTOM);
								Integer newhealth = player.getHealth() + regainevent.getAmount();
								if (newhealth < 0) {
									newhealth = 0;
								} else if (newhealth > 20) {
									newhealth = 20;
								}
								regainevent.setAmount(newhealth - player.getHealth());
								Bukkit.getServer().getPluginManager().callEvent(regainevent);
								if (!regainevent.isCancelled()) {
									player.setHealth(newhealth);
								}
							}
							if (rclick.getInt("hunger", 0) != 0) {
								FoodLevelChangeEvent flchangeevent = new FoodLevelChangeEvent(player, rclick.getInt("hunger", 0));
								Integer newhunger = player.getFoodLevel() + flchangeevent.getFoodLevel();
								if (newhunger < 0) {
									newhunger = 0;
								} else if (newhunger> 20) {
									newhunger = 20;
								}
								flchangeevent.setFoodLevel(newhunger - player.getFoodLevel());
								Bukkit.getServer().getPluginManager().callEvent(flchangeevent);
								if (!flchangeevent.isCancelled()) {
									player.setFoodLevel(newhunger);
								}
							}
							if (rclick.getString("sound", null) != null){
								SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, rclick.getString("sound"), false, player.getLocation());
							}
							event.setCancelled(true);
							event.setUseInteractedBlock(Result.DENY);
							event.setUseItemInHand(Result.DENY);
						} else {
						}
					}
				}
			}
		}
	}
}