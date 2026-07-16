package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

import java.util.concurrent.ThreadLocalRandom;

public final class AutoWTapModule {
	private enum Phase {
		IDLE,
		RELEASING,
		HOLDING
	}

	private boolean enabled = true;
	private boolean toggleKeyWasDown;
	private long tickCounter;
	private long nextActionTick;
	private Phase phase = Phase.IDLE;
	private boolean originalForwardState;
	private KeyMapping forwardKeyMapping;

	public void tick(Minecraft client) {
		if (client == null) {
			return;
		}

		tickCounter++;
		pollToggleKey(client);
		updateActiveTap();

		if (!enabled || !TriggerbotClient.settings().autoWTapEnabled()) {
			return;
		}

		if (phase != Phase.IDLE) {
			return;
		}

		if (tickCounter < nextActionTick) {
			return;
		}

		if (!CombatTargeting.isInGame(client)) {
			return;
		}

		EntityHitResult entityHit = CombatTargeting.asEntityHit(client.hitResult);
		if (entityHit == null) {
			return;
		}

		Entity target = entityHit.getEntity();
		if (target == null || !target.isAlive() || FriendsMenu.instance().isFriend(target)) {
			return;
		}

		if (TriggerbotClient.settings().autoWTapChance() < 100) {
			int chance = Math.max(0, Math.min(100, TriggerbotClient.settings().autoWTapChance()));
			if (ThreadLocalRandom.current().nextInt(100) >= chance) {
				scheduleNextAttempt();
				return;
			}
		}

		beginTap(client);
	}

	private void beginTap(Minecraft client) {
		KeyMapping keyMapping = client.options.keyUp;
		if (keyMapping == null) {
			scheduleNextAttempt();
			return;
		}

		forwardKeyMapping = keyMapping;
		originalForwardState = keyMapping.isDown();
		keyMapping.setDown(false);
		phase = Phase.RELEASING;
		nextActionTick = tickCounter + Math.max(0, TriggerbotClient.settings().autoWTapReactionDelay()) + randomOffset();
	}

	private void updateActiveTap() {
		if (phase == Phase.IDLE) {
			return;
		}

		if (forwardKeyMapping == null) {
			phase = Phase.IDLE;
			scheduleNextAttempt();
			return;
		}

		if (phase == Phase.RELEASING && tickCounter >= nextActionTick) {
			forwardKeyMapping.setDown(true);
			phase = Phase.HOLDING;
			nextActionTick = tickCounter + Math.max(0, TriggerbotClient.settings().autoWTapHoldLength());
			return;
		}

		if (phase == Phase.HOLDING && tickCounter >= nextActionTick) {
			forwardKeyMapping.setDown(originalForwardState);
			forwardKeyMapping = null;
			phase = Phase.IDLE;
			scheduleNextAttempt();
		}
	}

	private void scheduleNextAttempt() {
		int baseDelay = Math.max(0, TriggerbotClient.settings().autoWTapReactionDelay());
		nextActionTick = tickCounter + baseDelay + randomOffset();
	}

	private int randomOffset() {
		if (!TriggerbotClient.settings().autoWTapRandomized()) {
			return 0;
		}

		int min = Math.max(0, TriggerbotClient.settings().autoWTapRandomizedMin());
		int max = Math.max(min, TriggerbotClient.settings().autoWTapRandomizedMax());
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().autoWTapBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setAutoWTapEnabled(enabled);
			TriggerbotMod.LOGGER.info("Auto W tap toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
