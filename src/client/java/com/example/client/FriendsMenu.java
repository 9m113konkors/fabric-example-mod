package com.example.client;

import com.example.TriggerbotMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.Entity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class FriendsMenu {
	private static final Path FRIENDS_PATH = FabricLoader.getInstance().getConfigDir().resolve("triggerbot-friends.txt");
	private static final FriendsMenu INSTANCE = new FriendsMenu();

	private final Map<String, String> friendsByNormalizedName = new LinkedHashMap<>();
	private boolean loaded;

	private FriendsMenu() {
	}

	public static FriendsMenu instance() {
		return INSTANCE;
	}

	public synchronized void load() {
		if (loaded) {
			return;
		}

		friendsByNormalizedName.clear();
		if (Files.exists(FRIENDS_PATH)) {
			try {
				for (String line : Files.readAllLines(FRIENDS_PATH, StandardCharsets.UTF_8)) {
					String trimmed = line.trim();
					if (!trimmed.isEmpty()) {
						friendsByNormalizedName.put(normalize(trimmed), trimmed);
					}
				}
			} catch (IOException ignored) {
			}
		}

		loaded = true;
		save();
	}

	public synchronized void save() {
		try {
			Path parent = FRIENDS_PATH.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}

			Files.write(FRIENDS_PATH, new ArrayList<>(friendsByNormalizedName.values()), StandardCharsets.UTF_8);
		} catch (IOException ignored) {
		}
	}

	public synchronized void addFriend(String username) {
		load();
		String cleanedName = clean(username);
		if (cleanedName.isEmpty()) {
			return;
		}

		friendsByNormalizedName.put(normalize(cleanedName), cleanedName);
		save();
	}

	public synchronized void removeFriend(String username) {
		load();
		friendsByNormalizedName.remove(normalize(username));
		save();
	}

	public synchronized boolean isFriend(String username) {
		load();
		return friendsByNormalizedName.containsKey(normalize(username));
	}

	public synchronized boolean isFriend(Entity entity) {
		if (entity == null) {
			return false;
		}

		return isFriend(CombatTargeting.entityName(entity));
	}

	public synchronized List<String> friends() {
		load();
		return new ArrayList<>(friendsByNormalizedName.values());
	}

	public synchronized void showMenu() {
		load();
		TriggerbotMod.LOGGER.info("Friends menu: {}", String.join(", ", friendsByNormalizedName.values()));
	}

	private static String clean(String value) {
		return value == null ? "" : value.trim();
	}

	private static String normalize(String value) {
		return clean(value).toLowerCase(Locale.ROOT);
	}
}