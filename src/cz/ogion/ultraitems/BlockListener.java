package cz.ogion.ultraitems;

import org.bukkit.event.block.BlockDamageEvent;

public class BlockListener extends org.bukkit.event.block.BlockListener {
	UltraItems plugin;

	public BlockListener(UltraItems instance) {
		// TODO Auto-generated constructor stub
		plugin = instance;
	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		
	}
}
