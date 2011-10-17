package cz.ogion.ultraitems;
import java.util.zip.DataFormatException;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.item.GenericCustomItem;

public class CustomItem {
	private ItemType type;
	private String name;
	private String title;
	private String textureUrl;
	private Plugin plugin;
	private org.getspout.spoutapi.material.CustomItem ci;
	public CustomItem(ItemType type, String name, String title, String textureurl, Plugin plugin) throws DataFormatException {
		setName(name);
		this.type = type;
		switch (this.type) {
		case GENERIC_ITEM:
			ci = new GenericCustomItem(plugin, title, textureurl);
			break;
		}
	}
	public CustomItem setName(String name) throws DataFormatException {
		if (name == null) {
			throw new DataFormatException("You have to specify item name");
		}
		this.name = name;
		return this;
	}
	public CustomItem setTexture(String textureUrl) throws DataFormatException {
		if (textureUrl == null) {
			throw new DataFormatException("You have to specify item texture url");
		}
		SpoutManager.getFileManager().addToCache(plugin, textureUrl);
		this.textureUrl = textureUrl;
		return this;
	}
	public CustomItem setTitle(String title) throws DataFormatException {
		if (title == null) {
			throw new DataFormatException("You have to specify item title");
		}
		this.title = title;
		return this;
	}
	public org.getspout.spoutapi.material.CustomItem getCustomItem() {
		return ci;
	}
	public String getTextureUrl() {
		return textureUrl;
	}
	public String getTitle() {
		return title;
	}
	public String getName() {
		return name;
	}


	// TODO: maxstacksize

}
