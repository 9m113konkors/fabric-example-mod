package com.example.client;

import com.example.TriggerbotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

public final class AimAssistModule {

	private static final float MAX_ROTATION_STEP = 6.0F;

	private boolean enabled = true;
	private boolean toggleKeyWasDown;

	public void tick(Minecraft client) {
		if (client == null) {
			return;
		}

		pollToggleKey(client);

		if (!enabled || !TriggerbotClient.settings().aimAssistEnabled()) {
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

		CombatTargeting.lookAtSmooth(client.player, target, MAX_ROTATION_STEP);
	}

	private void pollToggleKey(Minecraft client) {
		int keyBind = TriggerbotClient.settings().aimAssistBind();
		boolean keyDown = KeyInput.isKeyDown(client, keyBind);
		if (KeyInput.wasPressed(client, keyBind, toggleKeyWasDown)) {
			enabled = !enabled;
			TriggerbotClient.settings().setAimAssistEnabled(enabled);
			TriggerbotMod.LOGGER.info("Aim assist toggled {}", enabled ? "on" : "off");
		}
		toggleKeyWasDown = keyDown;
	}
}
