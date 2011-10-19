package cz.ogion.ultraitems;

import java.util.HashMap;
import java.util.zip.DataFormatException;

public class ItemManager {
	public HashMap<String, CustomItem> items = new HashMap<String, CustomItem>();
	public HashMap<Integer, HashMap<Integer, String>> itemIdCache = new HashMap<Integer, HashMap<Integer,String>>();
	private UltraItems plugin;

	/**
	 * Create item manager
	 * @param instance the plugin instance used for spout cache
	 */
	public ItemManager(UltraItems instance) {
		plugin = instance;
	}
	/**
	 * Add item to the manager
	 * @param type type of item
	 * @param name unique name used for getting the item
	 * @param title title shown in inventory
	 * @param textureUrl URL of used texture
	 * @throws DataFormatException
	 */
	public void addItem(ItemType type, String name, String title, String textureUrl) throws DataFormatException {
		CustomItem item = new CustomItem(type, name, title, textureUrl, plugin);
		items.put(name, item);
		if (!itemIdCache.containsKey(item.getCustomItem().getRawId())) {
			itemIdCache.put(item.getCustomItem().getRawId(), new HashMap<Integer, String>());
		}
		itemIdCache.get(item.getCustomItem().getRawId()).put(item.getCustomItem().getRawData(), name);
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
	 * @return
	 */
	public CustomItem getItem(String name) {
		return items.get(name);
	}
	/**
	 * Get the custom item based on its id and data value
	 * @param id id of custom item
	 * @param data data value of custom item
	 * @return
	 */
	public CustomItem getItem(Integer id, Integer data) {
		return items.get(itemIdCache.get(id).get(data));
	}
}