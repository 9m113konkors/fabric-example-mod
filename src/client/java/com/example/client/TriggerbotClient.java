package com.example.client;

import com.example.TriggerbotMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class TriggerbotClient implements ClientModInitializer {

	// Settings/friends first so other static initializers never see null.
	private static final ModuleSettings SETTINGS = ModuleSettings.instance();
	private static final FriendsMenu FRIENDS_MENU = FriendsMenu.instance();

	private static final TriggerbotModule TRIGGERBOT_MODULE = new TriggerbotModule();
	private static final AimAssistModule AIM_ASSIST_MODULE = new AimAssistModule();
	private static final AutoCrystalModule AUTO_CRYSTAL_MODULE = new AutoCrystalModule();
	private static final AutoTotemModule AUTO_TOTEM_MODULE = new AutoTotemModule();
	private static final AutoMaceModule AUTO_MACE_MODULE = new AutoMaceModule();
	private static final AutoPearlCatchModule AUTO_PEARL_CATCH_MODULE = new AutoPearlCatchModule();
	private static final AutoWTapModule AUTO_W_TAP_MODULE = new AutoWTapModule();
	private static final SilentAuraModule SILENT_AURA_MODULE = new SilentAuraModule();
	private static final AnchorAuraModule ANCHOR_AURA_MODULE = new AnchorAuraModule();
	private static final LaunchUi LAUNCH_UI = new LaunchUi();

	@Override
	public void onInitializeClient() {
		SETTINGS.load();
		FRIENDS_MENU.load();

		if (SETTINGS.menuBind() < 0) {
			SETTINGS.setMenuBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
		}

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			try {
				triggerbotModule().tick(client);
				aimAssistModule().tick(client);
				autoCrystalModule().tick(client);
				autoTotemModule().tick(client);
				autoMaceModule().tick(client);
				autoPearlCatchModule().tick(client);
				autoWTapModule().tick(client);
				silentAuraModule().tick(client);
				anchorAuraModule().tick(client);
				launchUi().tick(client);
			} catch (Throwable t) {
				// Never let a module hard-crash the whole client.
				TriggerbotMod.LOGGER.error("Triggerbot tick error", t);
			}
		});

		TriggerbotMod.LOGGER.info(
			"Triggerbot client initialized. Press Right Shift (GLFW {}) in-game to open the module menu. Config: {}",
			SETTINGS.menuBind(),
			net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir()
		);
	}

	public static TriggerbotModule triggerbotModule() {
		return TRIGGERBOT_MODULE;
	}

	public static AimAssistModule aimAssistModule() {
		return AIM_ASSIST_MODULE;
	}

	public static AutoCrystalModule autoCrystalModule() {
		return AUTO_CRYSTAL_MODULE;
	}

	public static AutoTotemModule autoTotemModule() {
		return AUTO_TOTEM_MODULE;
	}

	public static AutoMaceModule autoMaceModule() {
		return AUTO_MACE_MODULE;
	}

	public static AutoPearlCatchModule autoPearlCatchModule() {
		return AUTO_PEARL_CATCH_MODULE;
	}

	public static AutoWTapModule autoWTapModule() {
		return AUTO_W_TAP_MODULE;
	}

	public static LaunchUi launchUi() {
		return LAUNCH_UI;
	}

	public static SilentAuraModule silentAuraModule() {
		return SILENT_AURA_MODULE;
	}

	public static AnchorAuraModule anchorAuraModule() {
		return ANCHOR_AURA_MODULE;
	}

	public static ModuleSettings settings() {
		return SETTINGS;
	}

	public static FriendsMenu friendsMenu() {
		return FRIENDS_MENU;
	}
}
