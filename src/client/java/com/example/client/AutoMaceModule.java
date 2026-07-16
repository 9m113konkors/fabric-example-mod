package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class AutoMaceModule {

	private static final double MAX_RANGE = 4.5D;

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

		if (!enabled || !TriggerbotClient.settings().autoMaceEnabled()) {
			return;
		}

		if (tickCounter < nextActionTick) {
			return;
		}

		if (!CombatTargeting.isInGame(client)) {
			return;
		}

		Entity target = findTarget(client);
		if (target == null) {
			return;
		}

		if (TriggerbotClient.settings().autoMaceBreachCheck() && !hasBreachPath(client, target)) {
			return;
		}

		if (TriggerbotClient.settings().autoMaceSilentAim()) {
			CombatTargeting.lookAt(client.player, target);
		}

		if (TriggerbotClient.settings().autoMaceTriggerbotTiming() && !CombatTargeting.isAttackReady(client.player)) {
			return;
		}

		if (TriggerbotClient.settings().autoMaceStunSlam()) {
			TriggerbotMod.LOGGER.info("Auto mace stun slam sequence engaged");
		}

		CombatTargeting.attack(client, target);
		applyPostAttackDelay();
	}

	private Entity findTarget(Minecraft client) {
		List<Entity> entities = CombatTargeting.collectNearbyEntities(client.level, client.player, MAX_RANGE);
		Entity bestTarget = null;
		double bestDistance = Double.MAX_VALUE;

		for (Entity entity : entities) {
			if (entity == client.player || !entity.isAlive() || FriendsMenu.instance().isFriend(entity)) {
				continue;
			}

			if (!(entity instanceof LivingEntity) || CombatTargeting.isCrystal(entity)) {
				continue;
			}

			double distance = CombatTargeting.distanceSquared(client.player, entity);
			if (distance < bestDistance) {
				bestDistance = distance;
				bestTarget = entity;
			}
		}

		return bestTarget;
	}

	private boolean hasBreachPath(Minecraft client, Entity target) {
		return client.player.position() != null && target.position() != null;
	}

	private void applyPostAttackDelay() {
		int baseDelay = Math.max(0, TriggerbotClient.settings().autoMaceDelay());
		int randomizedDelay = 0;
		if (TriggerbotClient.settings().autoMaceRandomized()) {
			int min = Math.max(0, TriggerbotClient.settings().autoMaceRandomizedMin());
			int max = Math.max(min, TriggerbotClient.settings().autoMaceRandomizedMax());
			randomizedDelay = ThreadLocalRandom.current().nextInt(min, max + 1);
		}
		nextActionTick = tickCounter + baseDelay + randomizedDelay;
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().autoMaceBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setAutoMaceEnabled(enabled);
			TriggerbotMod.LOGGER.info("Auto mace toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
