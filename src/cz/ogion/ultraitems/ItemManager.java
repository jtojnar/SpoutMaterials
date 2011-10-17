package cz.ogion.ultraitems;

import java.util.HashMap;
import java.util.zip.DataFormatException;

public class ItemManager {
	public HashMap<String, CustomItem> items = new HashMap<String, CustomItem>();
	private UltraItems plugin;

	public ItemManager(UltraItems instance) {
		plugin = instance;
	}
	public void addItem(ItemType type, String name, String title, String textureurl) throws DataFormatException {
		CustomItem item = new CustomItem(type, name, title, textureurl, plugin);
		items.put(name, item);
	}
	public void removeItem(String name) {
		// TODO: method stub
	}
	public CustomItem getItem(String name) {
		return items.get(name);
	}
}