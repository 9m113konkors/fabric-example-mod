package com.example.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Typed combat helpers. Direct Minecraft API calls so Fabric Loom remaps
 * field/method names correctly in both dev and production jars.
 * (String reflection against Mojmap names fails at runtime after remap.)
 */
public final class CombatTargeting {
	private CombatTargeting() {
	}

	public static boolean isInGame(Minecraft client) {
		return client != null
			&& client.player != null
			&& client.level != null
			&& client.gameMode != null
			&& client.screen == null;
	}

	public static List<Entity> collectNearbyEntities(ClientLevel level, LocalPlayer player, double range) {
		AABB searchBox = player.getBoundingBox().inflate(range);
		List<Entity> entities = level.getEntities(player, searchBox, entity -> true);
		return new ArrayList<>(entities);
	}

	public static List<Entity> collectNearbyEntities(ClientLevel level, LocalPlayer player, double range, Predicate<Entity> filter) {
		AABB searchBox = player.getBoundingBox().inflate(range);
		List<Entity> entities = level.getEntities(player, searchBox, filter == null ? entity -> true : filter);
		return new ArrayList<>(entities);
	}

	public static EntityHitResult asEntityHit(HitResult hitResult) {
		if (hitResult instanceof EntityHitResult entityHitResult) {
			return entityHitResult;
		}
		return null;
	}

	public static boolean isAttackReady(Player player) {
		return player.getAttackStrengthScale(0.0F) >= 1.0F;
	}

	public static void attack(Minecraft client, Entity target) {
		if (client == null || client.gameMode == null || client.player == null || target == null) {
			return;
		}
		client.gameMode.attack(client.player, target);
	}

	public static void lookAt(Entity from, Entity target) {
		if (from == null || target == null) {
			return;
		}

		Vec3 eye = from.getEyePosition();
		double targetX = target.getX();
		double targetY = target.getY() + target.getBbHeight() * 0.5D;
		double targetZ = target.getZ();

		double deltaX = targetX - eye.x;
		double deltaY = targetY - eye.y;
		double deltaZ = targetZ - eye.z;
		double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
		if (horizontalDistance < 0.0001D) {
			return;
		}

		float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
		float pitch = (float) (-Math.toDegrees(Math.atan2(deltaY, horizontalDistance)));
		from.setYRot(yaw);
		from.setXRot(pitch);
		if (from instanceof LivingEntity living) {
			living.setYHeadRot(yaw);
		}
	}

	public static void lookAtSmooth(Entity from, Entity target, float maxStep) {
		if (from == null || target == null) {
			return;
		}

		Vec3 eye = from.getEyePosition();
		double targetX = target.getX();
		double targetY = target.getY() + target.getBbHeight() * 0.5D;
		double targetZ = target.getZ();

		double deltaX = targetX - eye.x;
		double deltaY = targetY - eye.y;
		double deltaZ = targetZ - eye.z;
		double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
		if (horizontalDistance < 0.0001D) {
			return;
		}

		float desiredYaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
		float desiredPitch = (float) (-Math.toDegrees(Math.atan2(deltaY, horizontalDistance)));
		float newYaw = approachAngle(from.getYRot(), desiredYaw, maxStep);
		float newPitch = approachAngle(from.getXRot(), desiredPitch, maxStep);

		from.setYRot(newYaw);
		from.setXRot(newPitch);
		if (from instanceof LivingEntity living) {
			living.setYHeadRot(newYaw);
		}
	}

	public static double distanceSquared(Entity first, Entity second) {
		Vec3 eye = first.getEyePosition();
		double secondX = second.getX();
		double secondY = second.getY() + second.getBbHeight() * 0.5D;
		double secondZ = second.getZ();

		double deltaX = secondX - eye.x;
		double deltaY = secondY - eye.y;
		double deltaZ = secondZ - eye.z;
		return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
	}

	public static boolean isWithinRange(Entity first, Entity second, double maxRangeSquared) {
		return distanceSquared(first, second) <= maxRangeSquared;
	}

	public static boolean isCrystal(Entity entity) {
		return entity instanceof EndCrystal;
	}

	public static String entityName(Entity entity) {
		if (entity == null) {
			return "";
		}

		String scoreboardName = entity.getScoreboardName();
		if (scoreboardName != null && !scoreboardName.isEmpty()) {
			return scoreboardName;
		}

		return entity.getName().getString();
	}

	private static float approachAngle(float current, float target, float maxStep) {
		float delta = wrapDegrees(target - current);
		if (delta > maxStep) {
			delta = maxStep;
		}
		if (delta < -maxStep) {
			delta = -maxStep;
		}
		return current + delta;
	}

	private static float wrapDegrees(float degrees) {
		float wrapped = degrees % 360.0F;
		if (wrapped >= 180.0F) {
			wrapped -= 360.0F;
		}
		if (wrapped < -180.0F) {
			wrapped += 360.0F;
		}
		return wrapped;
	}
}
