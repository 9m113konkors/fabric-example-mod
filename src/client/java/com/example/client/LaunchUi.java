package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * Menu controller. Uses a real Minecraft Screen (not Swing) so it works under
 * Prism Launcher where AWT is headless and JFrame throws HeadlessException.
 */
public final class LaunchUi {
	private boolean menuKeyWasDown;

	public void show() {
		Minecraft client = Minecraft.getInstance();
		if (client == null) {
			return;
		}
		client.setScreen(new ModuleMenuScreen());
	}

	public void hide() {
		Minecraft client = Minecraft.getInstance();
		if (client != null && client.screen instanceof ModuleMenuScreen) {
			client.setScreen(null);
		}
	}

	public void toggle() {
		Minecraft client = Minecraft.getInstance();
		if (client == null) {
			return;
		}

		if (client.screen instanceof ModuleMenuScreen) {
			client.setScreen(null);
		} else if (client.screen == null) {
			// Only open when no other screen (chat/inventory) is open.
			client.setScreen(new ModuleMenuScreen());
			TriggerbotMod.LOGGER.info("Opened triggerbot in-game menu (Right Shift)");
		}
	}

	public void tick(Minecraft client) {
		if (client == null) {
			return;
		}

		ModuleSettings settings = ModuleSettings.instance();
		int keyBind = settings.menuBind();
		if (keyBind < 0) {
			keyBind = GLFW.GLFW_KEY_RIGHT_SHIFT;
		}

		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (keyDown && !menuKeyWasDown) {
			// Must open screens on the render/client thread (this tick already is).
			toggle();
		}
		menuKeyWasDown = keyDown;
	}
}
