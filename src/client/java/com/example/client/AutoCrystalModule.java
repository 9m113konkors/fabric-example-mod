package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class AutoCrystalModule {

	private static final double MAX_RANGE = 4.5D;
	private static final double MAX_RANGE_SQUARED = MAX_RANGE * MAX_RANGE;

	private boolean enabled = true;
	private boolean toggleKeyWasDown;
	private long tickCounter;
	private long nextActionTick;
	private long pauseUntilTick;

	public void tick(Minecraft client) {
		if (client == null) {
			return;
		}

		tickCounter++;
		pollToggleKey(client);

		if (!enabled || !TriggerbotClient.settings().autoCrystalEnabled()) {
			return;
		}

		if (tickCounter < pauseUntilTick || tickCounter < nextActionTick) {
			return;
		}

		if (TriggerbotClient.settings().autoCrystalAvoidSilentAura() && TriggerbotClient.settings().silentAuraEnabled()) {
			return;
		}

		if (TriggerbotClient.settings().autoCrystalAvoidAnchorAura() && TriggerbotClient.settings().anchorAuraEnabled()) {
			return;
		}

		if (!CombatTargeting.isInGame(client)) {
			return;
		}

		EntityHitResult entityHit = CombatTargeting.asEntityHit(client.hitResult);
		if (entityHit == null) {
			Entity nearbyCrystal = findNearbyCrystal(client);
			if (nearbyCrystal == null) {
				return;
			}

			if (TriggerbotClient.settings().autoCrystalDamageTick() && !CombatTargeting.isAttackReady(client.player)) {
				return;
			}

			maybeLookAtTarget(client, nearbyCrystal);
			CombatTargeting.attack(client, nearbyCrystal);
			if (TriggerbotClient.settings().autoCrystalDoubleTap()) {
				CombatTargeting.attack(client, nearbyCrystal);
			}
			applyPostAttackDelay();
			if (TriggerbotClient.settings().autoCrystalPauseOnKill()) {
				pauseUntilTick = tickCounter + 10L;
			}
			TriggerbotMod.LOGGER.info("Auto crystal hit a nearby crystal");
			return;
		}

		Entity target = entityHit.getEntity();
		if (target == null || !target.isAlive() || FriendsMenu.instance().isFriend(target)) {
			return;
		}

		if (!CombatTargeting.isCrystal(target)) {
			return;
		}

		if (!CombatTargeting.isWithinRange(client.player, target, MAX_RANGE_SQUARED)) {
			return;
		}

		if (TriggerbotClient.settings().autoCrystalDamageTick() && !CombatTargeting.isAttackReady(client.player)) {
			return;
		}

		if (!TriggerbotClient.settings().autoCrystalSilent()) {
			maybeLookAtTarget(client, target);
		}

		CombatTargeting.attack(client, target);
		if (TriggerbotClient.settings().autoCrystalDoubleTap()) {
			CombatTargeting.attack(client, target);
		}
		if (TriggerbotClient.settings().autoCrystalHeadBob()) {
			applyHeadBob(client);
		}
		applyPostAttackDelay();
		if (TriggerbotClient.settings().autoCrystalPauseOnKill()) {
			pauseUntilTick = tickCounter + 10L;
		}
		TriggerbotMod.LOGGER.info("Auto crystal popped a crystal in range");
	}

	private void applyPostAttackDelay() {
		int baseDelay = Math.max(0, TriggerbotClient.settings().autoCrystalDelay());
		int randomizedDelay = 0;
		if (TriggerbotClient.settings().autoCrystalRandomized()) {
			int min = Math.max(0, TriggerbotClient.settings().autoCrystalRandomizedMin());
			int max = Math.max(min, TriggerbotClient.settings().autoCrystalRandomizedMax());
			randomizedDelay = ThreadLocalRandom.current().nextInt(min, max + 1);
		}
		nextActionTick = tickCounter + baseDelay + randomizedDelay;
	}

	private void maybeLookAtTarget(Minecraft client, Entity target) {
		CombatTargeting.lookAt(client.player, target);
	}

	private void applyHeadBob(Minecraft client) {
		double bob = Math.sin(tickCounter * 0.35D) * 0.5D;
		client.player.setXRot(client.player.getXRot() + (float) bob);
	}

	private Entity findNearbyCrystal(Minecraft client) {
		List<Entity> entities = CombatTargeting.collectNearbyEntities(
			client.level,
			client.player,
			MAX_RANGE,
			entity -> entity instanceof EndCrystal && entity.isAlive()
		);

		Entity nearestCrystal = null;
		double nearestDistance = Double.MAX_VALUE;
		for (Entity crystal : entities) {
			double distance = CombatTargeting.distanceSquared(client.player, crystal);
			if (distance <= MAX_RANGE_SQUARED && distance < nearestDistance) {
				nearestDistance = distance;
				nearestCrystal = crystal;
			}
		}

		return nearestCrystal;
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().autoCrystalBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setAutoCrystalEnabled(enabled);
			TriggerbotMod.LOGGER.info("Auto crystal toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
