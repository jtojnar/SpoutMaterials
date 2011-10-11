package cz.ogion.ultraitems;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.util.config.ConfigurationNode;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.CustomItem;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	UltraItems plugin;
	Map<String, ConfigurationNode> config;

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
				for(Map.Entry<String, ConfigurationNode> item : config.entrySet()) {
					CustomItem ci = plugin.items.get(item.getKey());
					ConfigurationNode lclick = item.getValue().getNode("lclick");
					ConfigurationNode rclick = item.getValue().getNode("rclick");
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
								player.setHealth(player.getHealth() + lclick.getInt("health", 0));
							}
							if (lclick.getInt("hunger", 0) != 0) {
								player.setFoodLevel(player.getFoodLevel() + lclick.getInt("hunger", 0));
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
								player.setHealth(player.getHealth() + rclick.getInt("health", 0));
							}
							if (rclick.getInt("hunger", 0) != 0) {
								player.setFoodLevel(player.getFoodLevel() + rclick.getInt("hunger", 0));
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