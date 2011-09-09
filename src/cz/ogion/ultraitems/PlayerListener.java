package cz.ogion.ultraitems;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.ConfigurationNode;

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
				Integer eventitemdata = ((Short) event.getItem().getDurability()).intValue();
				for(ConfigurationNode item : config.values()) {
					Integer itemid = item.getInt("item", 0);
					Integer itemdata = item.getInt("data", 0);
					ConfigurationNode lclick = item.getNode("lclick");
					ConfigurationNode rclick = item.getNode("rclick");
					if(itemid != 0 && itemdata != 0 && itemid.equals(eventitemid) && itemdata.equals(eventitemdata)) {
						if((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && lclick != null && lclick.getString("action", null) != null) {
							player.chat(lclick.getString("action"));
							if (lclick.getBoolean("consume", false)) {
								ItemStack is = player.getItemInHand();
								is.setAmount(is.getAmount() - 1);
								player.setItemInHand(is);
							}
						} else if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && rclick != null && rclick.getString("action", null) != null) {
							player.chat(rclick.getString("action"));
							event.setCancelled(true);
							if (rclick.getBoolean("consume", false)) {
								ItemStack is = player.getItemInHand();
								is.setAmount(is.getAmount() - 1);
								player.setItemInHand(is);
							}
						} else {
						}
					}
				}
			}
		}
	}
}