package cz.ogion.ultraitems;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.CustomItem;
import org.getspout.spoutapi.material.item.GenericCustomItem;

public class UICustomItem {
	private ItemType type;
	private String name, title, textureUrl;
	Logger log = Logger.getLogger("Minecraft");
	private Integer entityDamage;
	private boolean instantBreak;
	private CustomItem ci;
	private HashMap<ItemActionType, ItemAction> actions = new HashMap<ItemActionType, ItemAction>();

	public UICustomItem(ItemType type, String name, String title, String textureurl, Plugin plugin) throws Exception {
		setName(name);
		setType(type);
		setTitle(title);
		setTexture(textureurl);
		switch (this.type) {
		case GENERIC_ITEM:
			ci = new GenericCustomItem(plugin, title, textureurl);
			break;
		default:
			throw new Exception("Invalid type specified");
		}
	}
	public UICustomItem setType(ItemType type) {
		this.type = type;
		return this;
		
	}
	public UICustomItem setName(String name) throws DataFormatException {
		if (name == null) {
			throw new DataFormatException("You have to specify item name");
		}
		this.name = name;
		return this;
	}
	public UICustomItem setTexture(String textureUrl) throws DataFormatException {
		if (textureUrl == null) {
			throw new DataFormatException("You have to specify item texture url");
		}
		this.textureUrl = textureUrl;
		return this;
	}
	public UICustomItem setTitle(String title) throws DataFormatException {
		if (title == null) {
			throw new DataFormatException("You have to specify item title");
		}
		this.title = title;
		return this;
	}
	public CustomItem getCustomItem() {
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
	public UICustomItem setConfig(ConfigurationSection config) {
		ConfigurationSection lclick = config.getConfigurationSection("lclick");
		ConfigurationSection rclick = config.getConfigurationSection("rclick");
//		List<?> events = config.getList("events");
//		for (Object event : events) {
//			// TODO: multiple events init
//		}
		entityDamage = config.getInt("damage.entity"); 
		instantBreak = config.getBoolean("instantbreak", false);
		if (lclick != null) {
			ItemAction itemAction = new ItemAction(ItemActionType.LCLICK);
			String action = lclick.getString("action", null);
			String bypass = lclick.getString("permissionbypass", null);
			Boolean consume = lclick.getBoolean("consume", false);
			Integer health = lclick.getInt("health", 0);
			Integer hunger = lclick.getInt("hunger", 0);
			String sound = lclick.getString("sound", null);
			if (action != null) {
				itemAction.setAction(action);
			}
			if (bypass != null) {
				itemAction.setPermissionBypass(bypass);
			}
			if (consume) {
				itemAction.setConsume(consume);
			}
			if (health != 0) {
				itemAction.setHealth(health);
			}
			if (hunger != 0) {
				itemAction.setHunger(hunger);
			}
			if (sound != null) {
				itemAction.setSound(sound);
			}
			addAction(itemAction);
		}
		if (rclick != null) {
			ItemAction itemAction = new ItemAction(ItemActionType.RCLICK);
			String action = rclick.getString("action", null);
			String bypass = rclick.getString("permissionbypass", null);
			Boolean consume = rclick.getBoolean("consume", false);
			Integer health = rclick.getInt("health", 0);
			Integer hunger = rclick.getInt("hunger", 0);
			String sound = rclick.getString("sound", null);
			if (action != null) {
				itemAction.setAction(action);
			}
			if (bypass != null) {
				itemAction.setPermissionBypass(bypass);
			}
			if (consume) {
				itemAction.setConsume(consume);
			}
			if (health != 0) {
				itemAction.setHealth(health);
			}
			if (hunger != 0) {
				itemAction.setHunger(hunger);
			}
			if (sound != null) {
				itemAction.setSound(sound);
			}
			addAction(itemAction);
		}
		return this;
	}
	public UICustomItem addAction(ItemAction action) {
		actions.put(action.getType(), action);
		return this;
	}
	public ItemAction getAction(ItemActionType type) {
		return actions.get(type);
	}
	public Integer getEntityDamage() {
		return entityDamage;
	}
	public boolean getInstantBreak() {
		return instantBreak;
	}

	// TODO: maxstacksize

}
