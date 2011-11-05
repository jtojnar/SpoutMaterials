package cz.ogion.ultraitems;

import org.bukkit.event.block.BlockDamageEvent;

public class BlockListener extends org.bukkit.event.block.BlockListener {
	UltraItems plugin;

	public BlockListener(UltraItems instance) {
		plugin = instance;
	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		UICustomItem item = plugin.itemManager.getItem(event.getPlayer().getItemInHand());
		if (item != null && item.getInstantBreak()) {
			event.setInstaBreak(true);
		}
	}
}
