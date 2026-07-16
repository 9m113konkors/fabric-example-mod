package com.example.client;

/** Modules shown in the in-game menu. */
public enum ModuleId {
	TRIGGERBOT("Triggerbot"),
	AIM_ASSIST("Aim Assist"),
	AUTO_CRYSTAL("Auto Crystal"),
	SILENT_AURA("Silent Aura"),
	ANCHOR_AURA("Anchor Aura"),
	AUTO_TOTEM("Auto Totem"),
	AUTO_MACE("Auto Mace"),
	AUTO_PEARL_CATCH("Auto Pearl Catch"),
	AUTO_W_TAP("Auto W-Tap"),
	MENU("Menu / Binds");

	private final String displayName;

	ModuleId(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return displayName;
	}

	public boolean isEnabled(ModuleSettings settings) {
		return switch (this) {
			case TRIGGERBOT -> settings.triggerbotEnabled();
			case AIM_ASSIST -> settings.aimAssistEnabled();
			case AUTO_CRYSTAL -> settings.autoCrystalEnabled();
			case SILENT_AURA -> settings.silentAuraEnabled();
			case ANCHOR_AURA -> settings.anchorAuraEnabled();
			case AUTO_TOTEM -> settings.autoTotemEnabled();
			case AUTO_MACE -> settings.autoMaceEnabled();
			case AUTO_PEARL_CATCH -> settings.autoPearlCatchEnabled();
			case AUTO_W_TAP -> settings.autoWTapEnabled();
			case MENU -> true;
		};
	}

	public void setEnabled(ModuleSettings settings, boolean enabled) {
		switch (this) {
			case TRIGGERBOT -> settings.setTriggerbotEnabled(enabled);
			case AIM_ASSIST -> settings.setAimAssistEnabled(enabled);
			case AUTO_CRYSTAL -> settings.setAutoCrystalEnabled(enabled);
			case SILENT_AURA -> settings.setSilentAuraEnabled(enabled);
			case ANCHOR_AURA -> settings.setAnchorAuraEnabled(enabled);
			case AUTO_TOTEM -> settings.setAutoTotemEnabled(enabled);
			case AUTO_MACE -> settings.setAutoMaceEnabled(enabled);
			case AUTO_PEARL_CATCH -> settings.setAutoPearlCatchEnabled(enabled);
			case AUTO_W_TAP -> settings.setAutoWTapEnabled(enabled);
			case MENU -> {
			}
		}
	}
}
