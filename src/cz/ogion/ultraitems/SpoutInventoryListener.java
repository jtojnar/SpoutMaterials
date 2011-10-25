package cz.ogion.ultraitems;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.getspout.spoutapi.event.inventory.InventoryClickEvent;
import org.getspout.spoutapi.event.inventory.InventoryCraftEvent;
import org.getspout.spoutapi.event.inventory.InventoryListener;

public class SpoutInventoryListener extends InventoryListener {
	UltraItems plugin;

	public SpoutInventoryListener(UltraItems instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, this, Event.Priority.Monitor, plugin);
	}
	@Override
	public void onInventoryCraft(InventoryCraftEvent event) {
		if(plugin.itemManager.getItem(event.getResult()) != null && !event.getPlayer().hasPermission("ultraitems.craft."+plugin.itemManager.getItem(event.getResult()).getName()) && !event.getPlayer().hasPermission("ultraitems.craft.*")) {
			event.getPlayer().sendMessage(ChatColor.RED + "You can't craft this!");
			event.setCancelled(true);
		}
	}
	@Override
	public void onInventoryClick(InventoryClickEvent event) {
//		ItemStack clicked = event.getItem();
//		ItemStack holding = event.getCursor();
//			// TODO: enhance maxstacksize
//			for (Object it : config.getValues(false).values()) {
//				ConfigurationSection item = (ConfigurationSection) it;
//				Integer itemid = item.getInt("item", 0);
//				Integer maxstacksize = item.getInt("maxstacksize", 0);
//				if (clicked != null && holding != null && itemid != 0 && holding.getType() == clicked.getType() && clicked.getType().getId() == itemid) {
//					if (clicked.getDurability() != holding.getDurability()) {
//						event.setCancelled(true);
//					} else if ((clicked.getAmount() + holding.getAmount()) >= maxstacksize) {
//						clicked.setAmount(maxstacksize);
//						holding.setAmount(clicked.getAmount() + holding.getAmount() - maxstacksize);
//					}
//				}
//		}
	}
	// TODO: crafting sound
	// TODO: burning time
}