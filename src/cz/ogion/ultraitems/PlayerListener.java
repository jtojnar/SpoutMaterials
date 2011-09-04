package cz.ogion.ultraitems;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.config.ConfigurationNode;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	UltraItems plugin;
	Map<String, ConfigurationNode> config;

	public PlayerListener(UltraItems instance) {
		plugin = instance;
	}
	public void onPlayerInteract(PlayerInteractEvent event) {
		config = plugin.config;
		Action action = event.getAction();
		Player player = event.getPlayer();
		Integer eventitemid = event.getItem().getTypeId();
		Integer eventitemdata = ((Short) event.getItem().getDurability()).intValue();
		if (config != null) {
			for(ConfigurationNode item : config.values()) {
				Integer itemid = item.getInt("item", 0);
				Integer itemdata = item.getInt("data", 0);
				String lclick = item.getString("lclick", null);
				String rclick = item.getString("rclick", null);
				if(itemid != 0 && itemdata != 0 && itemid.equals(eventitemid) && itemdata.equals(eventitemdata)) {
					// TODO: consume
					if((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && lclick != null) {
						player.chat(lclick);
					} else if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && rclick != null) {
						player.chat(rclick);
						event.setCancelled(true);
					} else {
					}
				}
			}
		}
	}
}