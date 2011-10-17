package cz.ogion.ultraitems;

import java.util.logging.Logger;
import java.util.zip.DataFormatException;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.Material;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.material.MaterialData;

public class Ingredient {
	Integer itemid = 0;
	Integer itemdata = 0;
	Integer amount = 0;
	Material material;
	UltraItems plugin;
	Logger log = Logger.getLogger("Minecraft");
	public Ingredient(String ingredient, UltraItems instance) throws Exception {
		plugin = instance;
		String[] item = null;
		try {
			item = ingredient.split("[:,;-]");
			if (item.length >= 1) {
				itemid = Integer.decode(item[0]);
				if (item.length >= 2) {
					itemdata = Integer.decode(item[1]);
					if (item.length >= 3){
						amount = Integer.decode(item[2]);
					}
				}
			}
		} catch (Exception e) {
			material = plugin.itemManager.getItem(item[0]).getCustomItem();
			if (material == null) {
				throw new DataFormatException("Your ingredient is in wrong format: " + ingredient);
			}
		}
		try {
			if (SpoutManager.getMaterialManager().isCustomItem(new ItemStack(itemid, itemdata))) {
				material = SpoutManager.getMaterialManager().getCustomItem(new ItemStack(itemid, itemdata));
			} else {
				material = MaterialData.getMaterial(itemid);
			}
		} catch (Exception e) {
			throw new DataFormatException("Couldn't estabilish material from " + ingredient);
		}
	}
	public Material getMaterial() throws Exception {
		if (material == null) {
			throw new DataFormatException("Id " + itemid + " of ingredient doesn't exist");
		}
		return material;
	}
	public Short getData() {
		return itemdata.shortValue();
	}
	public Byte getDataByte() {
		return itemdata.byteValue();
	}
	public org.bukkit.Material getOldMaterial() throws Exception {
		if (material == null) {
			throw new DataFormatException("Id " + itemid + " of ingredient doesn't exist");
		}
		return new org.bukkit.material.MaterialData(itemid).getItemType();
	}
}
