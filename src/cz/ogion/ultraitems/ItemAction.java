package cz.ogion.ultraitems;

public class ItemAction {
	private String bypass = null;
	private ItemActionType type;
	private String action = null;
	private Boolean consume = false;
	private Integer health = 0;
	private Integer hunger = 0;
	private String sound = null;

	public ItemAction(ItemActionType type) {
		setType(type);
	}
	public ItemAction setType(ItemActionType type) {
		this.type = type;
		return this;
	}
	public ItemActionType getType() {
		return this.type;
	}
	public ItemAction setPermissionBypass(String bypass) {
		this.bypass = bypass;
		return this;
	}
	public String getPermissionBypass() {
		return this.bypass;
	}
	public ItemAction setAction(String action) {
		this.action = action;
		return this;
		
	}
	public String getAction() {
		return this.action;
	}
	public ItemAction setConsume(Boolean consume) {
		this.consume = consume;
		return this;
		
	}
	public Boolean getConsume() {
		return this.consume;
	}
	public ItemAction setHealth(Integer health) {
		this.health = health;
		return this;
		
	}
	public Integer getHealth() {
		return this.health;
	}
	public ItemAction setHunger(Integer hunger) {
		this.hunger = hunger;
		return this;
	}
	public Integer getHunger() {
		return this.hunger;
	}
	public ItemAction setSound(String sound) {
		this.sound = sound;
		return this;
	}
	public String getSound() {
		return this.sound;
	}
}
