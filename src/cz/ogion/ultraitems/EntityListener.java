package cz.ogion.ultraitems;

import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.CustomItem;

public class EntityListener extends org.bukkit.event.entity.EntityListener {
	UltraItems plugin;
	ConfigurationSection config;

	public EntityListener(UltraItems instance) {
		plugin = instance;
	}
	public void onEntityDamage(EntityDamageEvent ev) {
		if(ev instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ev;
			Entity damager = event.getDamager();
			if (damager instanceof Player) {
				config = plugin.config;
				Player player = (Player) damager;
				if (config != null && player.getItemInHand() != null) {
					Integer eventitemid = player.getItemInHand().getTypeId();
					for(Entry<String, Object> item : config.getValues(false).entrySet()) {
						ConfigurationSection value = (ConfigurationSection) item.getValue();
						CustomItem ci = plugin.items.get(item.getKey());
						if(eventitemid != 0 && SpoutManager.getMaterialManager().isCustomItem(player.getItemInHand()) && SpoutManager.getMaterialManager().getCustomItem(player.getItemInHand()) == ci) {
							Integer entitydamage = value.getInt("damage.entity", event.getDamage());
							event.setDamage(entitydamage);
						}
					}
				}
			}
		}
	}
}