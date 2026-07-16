package com.example.client;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * Reliable key polling against the Minecraft GLFW window.
 * Uses typed API (Window#handle) so Loom remaps correctly in production jars.
 */
public final class KeyInput {
	private KeyInput() {
	}

	public static boolean isKeyDown(Minecraft client, int glfwKey) {
		if (client == null || glfwKey < 0) {
			return false;
		}

		long handle = client.getWindow().handle();
		if (handle == 0L) {
			return false;
		}

		return GLFW.glfwGetKey(handle, glfwKey) == GLFW.GLFW_PRESS;
	}

	/**
	 * Edge-detect helper: returns true once when the key transitions from up to down.
	 */
	public static boolean wasPressed(Minecraft client, int glfwKey, boolean wasDown) {
		return isKeyDown(client, glfwKey) && !wasDown;
	}
}
