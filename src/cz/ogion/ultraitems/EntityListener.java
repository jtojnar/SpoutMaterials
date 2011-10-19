package cz.ogion.ultraitems;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListener extends org.bukkit.event.entity.EntityListener {
	UltraItems plugin;
	ConfigurationSection config;

	public EntityListener(UltraItems instance) {
		plugin = instance;
	}
	@Override
	public void onEntityDamage(EntityDamageEvent ev) {
		if(ev instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ev;
			Entity damager = event.getDamager();
			if (damager instanceof Player) {
				Player player = (Player) damager;
				if (player.getItemInHand() != null) {
					CustomItem ci = plugin.itemManager.getItem(player.getItemInHand());
					if(ci != null) {
						plugin.log.info("z"+ci);
						Integer entitydamage = ci.getEntityDamage();
						event.setDamage(entitydamage);
					}
				}
			}
		}
	}
}