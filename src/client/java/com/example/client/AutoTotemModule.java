package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;

public final class AutoTotemModule {

	private boolean enabled = true;
	private boolean toggleKeyWasDown;
	private long tickCounter;
	private long nextActionTick;

	public void tick(Minecraft client) {
		if (client == null) {
			return;
		}

		tickCounter++;
		pollToggleKey(client);

		if (!enabled || !TriggerbotClient.settings().autoTotemEnabled()) {
			return;
		}

		if (tickCounter < nextActionTick) {
			return;
		}

		if (client.player == null || client.level == null) {
			return;
		}

		if (client.screen != null && !TriggerbotClient.settings().autoTotemOpenInventory()) {
			return;
		}

		if (!needsTotem(client)) {
			return;
		}

		ItemStack totem = findTotem(client);
		if (totem == null || totem.isEmpty()) {
			return;
		}

		performAction(client, totem);
		scheduleNextAction();
	}

	private boolean needsTotem(Minecraft client) {
		float health = client.player.getHealth();
		float absorption = client.player.getAbsorptionAmount();
		ItemStack offhand = client.player.getOffhandItem();
		boolean hasTotem = !offhand.isEmpty() && offhand.is(Items.TOTEM_OF_UNDYING);
		return health + absorption <= 18.0F || !hasTotem;
	}

	private ItemStack findTotem(Minecraft client) {
		ItemStack offhand = client.player.getOffhandItem();
		if (!offhand.isEmpty() && offhand.is(Items.TOTEM_OF_UNDYING)) {
			return offhand;
		}

		Inventory inventory = client.player.getInventory();
		for (ItemStack stack : inventory.getNonEquipmentItems()) {
			if (!stack.isEmpty() && stack.is(Items.TOTEM_OF_UNDYING)) {
				return stack;
			}
		}

		return null;
	}

	private void performAction(Minecraft client, ItemStack totem) {
		String actions = TriggerbotClient.settings().autoTotemActions();
		if (TriggerbotClient.settings().autoTotemLegitMouseMovements()) {
			moveMouseSlightly(client);
		}

		if (TriggerbotClient.settings().autoTotemHoverTotem()) {
			TriggerbotMod.LOGGER.info("Auto totem hovering target item before action");
		}

		if (TriggerbotClient.settings().autoTotemOpenInventory()) {
			TriggerbotMod.LOGGER.info("Auto totem using open inventory action path");
		}

		if ("DOUBLE_HAND".equalsIgnoreCase(actions)) {
			TriggerbotMod.LOGGER.info("Auto totem double hand action triggered");
		} else if ("OPEN_INVENTORY".equalsIgnoreCase(actions)) {
			TriggerbotMod.LOGGER.info("Auto totem open inventory action triggered");
		} else {
			TriggerbotMod.LOGGER.info("Auto totem action {} triggered", actions);
		}

		if (TriggerbotClient.settings().autoTotemDoubleHand()) {
			TriggerbotMod.LOGGER.info("Auto totem double hand follow-up triggered");
		}

		// Inventory mutation hooks require screen handlers; log presence of totem for now.
		TriggerbotMod.LOGGER.debug("Auto totem found stack: {}", totem.getHoverName().getString());
	}

	private void moveMouseSlightly(Minecraft client) {
		long handle = client.getWindow().handle();
		double x = ThreadLocalRandom.current().nextDouble(0.0D, 1.0D);
		double y = ThreadLocalRandom.current().nextDouble(0.0D, 1.0D);
		GLFW.glfwSetCursorPos(handle, x, y);
	}

	private void scheduleNextAction() {
		int baseDelay = Math.max(0, TriggerbotClient.settings().autoTotemSpeed());
		int randomDelay = 0;
		if (TriggerbotClient.settings().autoTotemRandomized()) {
			int min = Math.max(0, TriggerbotClient.settings().autoTotemRandomizedMin());
			int max = Math.max(min, TriggerbotClient.settings().autoTotemRandomizedMax());
			randomDelay = ThreadLocalRandom.current().nextInt(min, max + 1);
		}

		if (TriggerbotClient.settings().autoTotemDynamicDelay()) {
			nextActionTick = tickCounter + baseDelay + randomDelay;
		} else {
			nextActionTick = tickCounter + baseDelay;
		}
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().autoTotemBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setAutoTotemEnabled(enabled);
			TriggerbotMod.LOGGER.info("Auto totem toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
