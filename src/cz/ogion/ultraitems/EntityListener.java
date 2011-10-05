package cz.ogion.ultraitems;

import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.config.ConfigurationNode;

public class EntityListener extends org.bukkit.event.entity.EntityListener {
	UltraItems plugin;
	Map<String, ConfigurationNode> config;

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
					Integer eventitemdata = ((Short) player.getItemInHand().getDurability()).intValue();
					for(ConfigurationNode item : config.values()) {
						Integer itemid = item.getInt("item", 0);
						Integer itemdata = item.getInt("data", 0);
						if(itemid != 0 && itemid.equals(eventitemid) && itemdata.equals(eventitemdata)) {
							Integer entitydamage = item.getInt("damage.entity", event.getDamage());
							event.setDamage(entitydamage);
						}
					}
				}
			}
		}
	}
}