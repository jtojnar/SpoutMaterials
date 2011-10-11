package cz.ogion.ultraitems;

import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.ConfigurationNode;
import org.getspout.spoutapi.event.inventory.InventoryClickEvent;
import org.getspout.spoutapi.event.inventory.InventoryListener;

public class SpoutInventoryListener extends InventoryListener {
	UltraItems plugin;
	Map<String, ConfigurationNode> config;

	public SpoutInventoryListener(UltraItems instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, this, Event.Priority.Monitor, plugin);
	}
	public void onInventoryClick(InventoryClickEvent event) {
		config = plugin.config;
		ItemStack clicked = event.getItem();
		ItemStack holding = event.getCursor();
		if(config != null) {
			// TODO: enhance maxstacksize
			for (ConfigurationNode item : config.values()) {
				Integer itemid = item.getInt("item", 0);
				Integer maxstacksize = item.getInt("maxstacksize", 0);
				if (clicked != null && holding != null && itemid != 0 && holding.getType() == clicked.getType() && clicked.getType().getId() == itemid) {
					if (clicked.getDurability() != holding.getDurability()) {
						event.setCancelled(true);
					} else if ((clicked.getAmount() + holding.getAmount()) >= maxstacksize) {
						clicked.setAmount(maxstacksize);
						holding.setAmount(clicked.getAmount() + holding.getAmount() - maxstacksize);
					}
				}
			}
		}
	}
	// TODO: craftiong sound
	// TODO: burning time
}