package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public final class SilentAuraModule {

	private static final double MAX_RANGE = 4.75D;

	private boolean enabled = true;
	private boolean toggleKeyWasDown;

	public void tick(Minecraft client) {
		if (client == null) {
			return;
		}

		pollToggleKey(client);

		if (!enabled || !TriggerbotClient.settings().silentAuraEnabled()) {
			return;
		}

		if (TriggerbotClient.settings().silentAuraAvoidAutoCrystal() && TriggerbotClient.settings().autoCrystalEnabled()) {
			return;
		}

		if (TriggerbotClient.settings().silentAuraAvoidAnchorAura() && TriggerbotClient.settings().anchorAuraEnabled()) {
			return;
		}

		if (!CombatTargeting.isInGame(client)) {
			return;
		}

		Entity target = findTarget(client);
		if (target == null) {
			return;
		}

		CombatTargeting.attack(client, target);
	}

	private Entity findTarget(Minecraft client) {
		List<Entity> entities = CombatTargeting.collectNearbyEntities(client.level, client.player, MAX_RANGE);
		Entity nearestTarget = null;
		double nearestDistance = Double.MAX_VALUE;
		double maxRangeSquared = MAX_RANGE * MAX_RANGE;

		for (Entity entity : entities) {
			if (entity == client.player || !entity.isAlive() || FriendsMenu.instance().isFriend(entity)) {
				continue;
			}

			if (CombatTargeting.isCrystal(entity) || !(entity instanceof LivingEntity)) {
				continue;
			}

			double distance = CombatTargeting.distanceSquared(client.player, entity);
			if (distance <= maxRangeSquared && distance < nearestDistance) {
				nearestDistance = distance;
				nearestTarget = entity;
			}
		}

		return nearestTarget;
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().silentAuraBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setSilentAuraEnabled(enabled);
			TriggerbotMod.LOGGER.info("Silent aura toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
