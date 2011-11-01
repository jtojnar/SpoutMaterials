package cz.ogion.ultraitems;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.getspout.spoutapi.SpoutManager;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	UltraItems plugin;
	ConfigurationSection config;
	Logger log = Logger.getLogger("Minecraft");

	public PlayerListener(UltraItems instance) {
		plugin = instance;
	}
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.hasItem()){
			Action action = event.getAction();
			Player player = event.getPlayer();
			if (event.getItem() != null) {
				CustomItem ci = plugin.itemManager.getItem(event.getItem());
				if (ci != null) {
					ItemAction lclick = ci.getAction(ItemActionType.LCLICK);
					ItemAction rclick = ci.getAction(ItemActionType.RCLICK);
					if((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && lclick != null) {
						if (lclick.getAction() != null) {
							String permissionbypass = lclick.getPermissionBypass();
							if(permissionbypass != null){
								PermissionAttachment attachment = player.addAttachment(plugin);
								attachment.setPermission(permissionbypass, true);
								player.chat(lclick.getAction());
								attachment.unsetPermission(permissionbypass);
								player.removeAttachment(attachment);
							} else {
								player.chat(lclick.getAction());
							}
						}
						// TODO: delay
						if (lclick.getConsume()) {
							ItemStack is = player.getItemInHand();
							is.setAmount(is.getAmount() - 1);
							//	player.setItemInHand(is);
						}
						if (lclick.getHealth() != 0) {
							EntityRegainHealthEvent regainevent = new EntityRegainHealthEvent(player, lclick.getHealth(), RegainReason.CUSTOM);
							Integer newhealth = player.getHealth() + regainevent.getAmount();
							if (newhealth < 0) {
								newhealth = 0;
							} else if (newhealth > 20) {
								newhealth = 20;
							}
							regainevent.setAmount(newhealth - player.getHealth());
							Bukkit.getServer().getPluginManager().callEvent(regainevent);
							if (!regainevent.isCancelled()) {
								player.setHealth(newhealth);
							}
						}
						if (lclick.getHunger() != 0) {
							FoodLevelChangeEvent flchangeevent = new FoodLevelChangeEvent(player, lclick.getHunger());
							Integer newhunger = player.getFoodLevel() + flchangeevent.getFoodLevel();
							if (newhunger < 0) {
								newhunger = 0;
							} else if (newhunger> 20) {
								newhunger = 20;
							}
							flchangeevent.setFoodLevel(newhunger - player.getFoodLevel());
							Bukkit.getServer().getPluginManager().callEvent(flchangeevent);
							if (!flchangeevent.isCancelled()) {
								player.setFoodLevel(newhunger);
							}
						}
						if (lclick.getSound() != null){
							SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, lclick.getSound(), false, player.getLocation());
						}
						event.setCancelled(true);
						event.setUseInteractedBlock(Result.DENY);
						event.setUseItemInHand(Result.DENY);
					} else if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && rclick != null) {
						if (rclick.getAction() != null) {
							String permissionbypass = rclick.getPermissionBypass();
							if(permissionbypass != null){
								PermissionAttachment attachment = player.addAttachment(plugin);
								attachment.setPermission(permissionbypass, true);
								player.chat(rclick.getAction());
								attachment.unsetPermission(permissionbypass);
								player.removeAttachment(attachment);
							} else {
								player.chat(rclick.getAction());
							}
						}
						if (rclick.getConsume()) {
							ItemStack is = player.getItemInHand();
							is.setAmount(is.getAmount() - 1);
							player.setItemInHand(is);
							// TODO: returning another item (drinking coffee)
						}
						if (rclick.getHealth() != 0) {
							EntityRegainHealthEvent regainevent = new EntityRegainHealthEvent(player, rclick.getHealth(), RegainReason.CUSTOM);
							Integer newhealth = player.getHealth() + regainevent.getAmount();
							if (newhealth < 0) {
								newhealth = 0;
							} else if (newhealth > 20) {
								newhealth = 20;
							}
							regainevent.setAmount(newhealth - player.getHealth());
							Bukkit.getServer().getPluginManager().callEvent(regainevent);
							if (!regainevent.isCancelled()) {
								player.setHealth(newhealth);
							}
						}
						if (rclick.getHunger() != 0) {
							FoodLevelChangeEvent flchangeevent = new FoodLevelChangeEvent(player, rclick.getHunger());
							Integer newhunger = player.getFoodLevel() + flchangeevent.getFoodLevel();
							if (newhunger < 0) {
								newhunger = 0;
							} else if (newhunger> 20) {
								newhunger = 20;
							}
							flchangeevent.setFoodLevel(newhunger - player.getFoodLevel());
							Bukkit.getServer().getPluginManager().callEvent(flchangeevent);
							if (!flchangeevent.isCancelled()) {
								player.setFoodLevel(newhunger);
							}
						}
						if (rclick.getSound() != null){
							SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, rclick.getSound(), false, player.getLocation());
						}
						event.setCancelled(true);
						event.setUseInteractedBlock(Result.DENY);
						event.setUseItemInHand(Result.DENY);
					} else {
					}
				}
			}
		}
	}
}