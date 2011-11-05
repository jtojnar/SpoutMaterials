package cz.ogion.ultraitems;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class ItemManager {
	public HashMap<String, UICustomItem> items = new HashMap<String, UICustomItem>();
	public HashMap<Integer, HashMap<Integer, String>> itemIdCache = new HashMap<Integer, HashMap<Integer,String>>();

	/**
	 * Create item manager
	 * @param instance the plugin instance used for spout cache
	 */
	public ItemManager() {
	}
	/**
	 * Add item to the manager
	 * @param item item to be added
	 * @return item manager
	 * @throws Exception 
	 */
	public ItemManager addItem(UICustomItem item) throws Exception {
		items.put(item.getName(), item);
		if (!itemIdCache.containsKey(item.getCustomItem().getRawId())) {
			itemIdCache.put(item.getCustomItem().getRawId(), new HashMap<Integer, String>());
		}
		itemIdCache.get(item.getCustomItem().getRawId()).put(item.getCustomItem().getRawData(), item.getName());
		return this;
	}
	/**
	 * Remove item from manager
	 * @param name name of item
	 */
	public void removeItem(String name) {
		// TODO: method stub
	}
	/**
	 * Get the custom item based on its name
	 * @param name name of item
	 * @return custom item with specified name
	 */
	public UICustomItem getItem(String name) {
		return items.get(name);
	}
	/**
	 * Get the custom item based on its id and data value
	 * @param id id of custom item
	 * @param data data value of custom item
	 * @return custom item with specified name
	 */
	public UICustomItem getItem(Integer id, Integer data) {
		try {
			return items.get(itemIdCache.get(id).get(data));
		} catch(Exception e) {
			return null;
		}
	}
	/**
	 * Get the custom item based on stack of items
	 * @param item stack of items
	 * @return custom item or null if not existing
	 */
	public UICustomItem getItem(ItemStack item) {
		try {
			return items.get(itemIdCache.get(item.getTypeId()).get(((Short) item.getDurability()).intValue()));
		} catch(Exception e) {
			return null;
		}
	}
}