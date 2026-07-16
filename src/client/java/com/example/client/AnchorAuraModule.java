package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class AnchorAuraModule {

	private static final double MAX_RANGE = 5.5D;

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

		if (!enabled || !TriggerbotClient.settings().anchorAuraEnabled()) {
			return;
		}

		if (tickCounter < pauseUntilTick || tickCounter < nextActionTick) {
			return;
		}

		if (TriggerbotClient.settings().anchorAuraAvoidAutoCrystal() && TriggerbotClient.settings().autoCrystalEnabled()) {
			return;
		}

		if (TriggerbotClient.settings().anchorAuraAvoidSilentAura() && TriggerbotClient.settings().silentAuraEnabled()) {
			return;
		}

		if (!CombatTargeting.isInGame(client)) {
			return;
		}

		Entity target = findTarget(client);
		if (target == null) {
			return;
		}

		if (TriggerbotClient.settings().anchorAuraSilentAim() && TriggerbotClient.settings().anchorAuraExtremelyOp()) {
			CombatTargeting.lookAt(client.player, target);
		}

		if (TriggerbotClient.settings().anchorAuraAutoSwap()) {
			TriggerbotMod.LOGGER.info("Anchor aura auto swap engaged");
		}

		CombatTargeting.attack(client, target);
		applyPostAttackDelay();
		if (TriggerbotClient.settings().anchorAuraAutomaticSafeAnchor()) {
			pauseUntilTick = tickCounter + 5L;
		}
	}

	private Entity findTarget(Minecraft client) {
		List<Entity> entities = CombatTargeting.collectNearbyEntities(client.level, client.player, MAX_RANGE);
		Entity bestAnchorLike = null;
		double bestAnchorDistance = Double.MAX_VALUE;
		Entity bestLivingTarget = null;
		double bestLivingDistance = Double.MAX_VALUE;

		for (Entity entity : entities) {
			if (entity == client.player || !entity.isAlive() || FriendsMenu.instance().isFriend(entity)) {
				continue;
			}

			if (TriggerbotClient.settings().anchorAuraOnlyOwnAnchors() && !isLikelyOwnAnchor(entity, client)) {
				continue;
			}

			double distance = CombatTargeting.distanceSquared(client.player, entity);
			boolean anchorLike = entity instanceof EndCrystal
				|| entity.getType().toShortString().toLowerCase().contains("anchor");

			if (anchorLike && distance < bestAnchorDistance) {
				bestAnchorDistance = distance;
				bestAnchorLike = entity;
			}

			if (entity instanceof LivingEntity && distance < bestLivingDistance) {
				bestLivingDistance = distance;
				bestLivingTarget = entity;
			}
		}

		return bestAnchorLike != null ? bestAnchorLike : bestLivingTarget;
	}

	private boolean isLikelyOwnAnchor(Entity entity, Minecraft client) {
		String entityName = CombatTargeting.entityName(entity).toLowerCase();
		String playerName = CombatTargeting.entityName(client.player).toLowerCase();
		return entityName.contains(playerName) || entityName.contains("anchor") || entity instanceof EndCrystal;
	}

	private void applyPostAttackDelay() {
		int baseDelay = Math.max(0, TriggerbotClient.settings().anchorAuraDelay());
		int randomizedDelay = 0;
		if (TriggerbotClient.settings().anchorAuraRandomized()) {
			int min = Math.max(0, TriggerbotClient.settings().anchorAuraRandomizedMin());
			int max = Math.max(min, TriggerbotClient.settings().anchorAuraRandomizedMax());
			randomizedDelay = ThreadLocalRandom.current().nextInt(min, max + 1);
		}

		nextActionTick = tickCounter + baseDelay + randomizedDelay;
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().anchorAuraBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setAnchorAuraEnabled(enabled);
			TriggerbotMod.LOGGER.info("Anchor aura toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
