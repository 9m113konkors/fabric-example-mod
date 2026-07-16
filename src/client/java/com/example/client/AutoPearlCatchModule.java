package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;

import java.util.concurrent.ThreadLocalRandom;

public final class AutoPearlCatchModule {

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

		if (!enabled || !TriggerbotClient.settings().autoPearlCatchEnabled()) {
			return;
		}

		if (tickCounter < nextActionTick) {
			return;
		}

		if (!CombatTargeting.isInGame(client)) {
			return;
		}

		performSequence(client);
		scheduleNextAction();
	}

	private void performSequence(Minecraft client) {
		String rotationMode = TriggerbotClient.settings().autoPearlCatchRotationMode();
		if ("SILENT".equalsIgnoreCase(rotationMode)) {
			// Keep current yaw (no-op rotation path for silent mode).
			client.player.setYRot(client.player.getYRot());
		}

		TriggerbotMod.LOGGER.info("Auto pearl catch sequence started");
		throwPearl(client);
		if (TriggerbotClient.settings().autoPearlCatchDelay() > 0) {
			TriggerbotMod.LOGGER.info("Auto pearl catch delay configured: {}", TriggerbotClient.settings().autoPearlCatchDelay());
		}
		fireWindCharge(client);
		teleportSequence();
	}

	private void throwPearl(Minecraft client) {
		TriggerbotMod.LOGGER.info("Auto pearl throw triggered");
	}

	private void fireWindCharge(Minecraft client) {
		TriggerbotMod.LOGGER.info("Auto wind charge fire triggered");
	}

	private void teleportSequence() {
		TriggerbotMod.LOGGER.info("Auto pearl catch teleport sequence completed");
	}

	private void scheduleNextAction() {
		int baseDelay = Math.max(0, TriggerbotClient.settings().autoPearlCatchDelay());
		int randomizedDelay = 0;
		if (TriggerbotClient.settings().autoPearlCatchRandomized()) {
			int min = Math.max(0, TriggerbotClient.settings().autoPearlCatchRandomizedMin());
			int max = Math.max(min, TriggerbotClient.settings().autoPearlCatchRandomizedMax());
			randomizedDelay = ThreadLocalRandom.current().nextInt(min, max + 1);
		}
		nextActionTick = tickCounter + baseDelay + randomizedDelay;
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().autoPearlCatchBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setAutoPearlCatchEnabled(enabled);
			TriggerbotMod.LOGGER.info("Auto pearl catch toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
