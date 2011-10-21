package cz.ogion.ultraitems;

import java.util.zip.DataFormatException;

import org.getspout.spoutapi.material.Material;
import org.getspout.spoutapi.material.MaterialData;

public class Ingredient {
	private Integer itemid = 0;
	private Integer itemdata = 0;
	private Integer amount = 1;
	private Material material;
	private UltraItems plugin;
	private String ingredient;
	public Ingredient(String ingredient, UltraItems instance) throws Exception {
		this.ingredient = ingredient;
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
			material = MaterialData.getMaterial(itemid, getData());
		} catch (Exception e) {
			if (plugin.itemManager.getItem(ingredient) != null) {
				material = plugin.itemManager.getItem(ingredient).getCustomItem();
			}
		}
			plugin.log.info("k"+material);
		if (material == null) {
			throw new DataFormatException("Ingredient \"" + ingredient + "\" doesn't exist");
		}
	}
	public Material getMaterial() throws Exception {
		if (material == null) {
			throw new DataFormatException("Ingredient \"" + ingredient + "\" doesn't exist");
		}
		return material;
	}
	public Integer getAmount() {
		return amount;
	}
	public Short getData() {
		return itemdata.shortValue();
	}
	public Byte getDataByte() {
		return itemdata.byteValue();
	}
	public org.bukkit.Material getOldMaterial() throws Exception {
		if (material == null) {
			throw new DataFormatException("Ingredient \"" + ingredient + "\" doesn't exist");
		}
		return new org.bukkit.material.MaterialData(itemid).getItemType();
	}
}
