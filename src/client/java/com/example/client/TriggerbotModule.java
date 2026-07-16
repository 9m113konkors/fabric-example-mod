package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

public final class TriggerbotModule {

	private boolean enabled = true;
	private boolean toggleKeyWasDown;

	public void tick(Minecraft client) {
		if (client == null) {
			return;
		}

		pollToggleKey(client);

		if (!enabled || !TriggerbotClient.settings().triggerbotEnabled()) {
			return;
		}

		if (!CombatTargeting.isInGame(client)) {
			return;
		}

		if (!CombatTargeting.isAttackReady(client.player)) {
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

		CombatTargeting.attack(client, target);
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().triggerbotBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setTriggerbotEnabled(enabled);
			TriggerbotMod.LOGGER.info("Triggerbot toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
