package com.example.client;

import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ModuleSettings {
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("triggerbot-mod.properties");
	private static final ModuleSettings INSTANCE = new ModuleSettings();

	private final Properties properties = new Properties();
	private boolean loaded;

	private int triggerbotBind = -1;
	private boolean triggerbotEnabled = true;
	private int aimAssistBind = -1;
	private boolean aimAssistEnabled = true;
	private int autoCrystalBind = -1;
	private boolean autoCrystalEnabled = true;
	private int autoCrystalDelay = 2;
	private boolean autoCrystalRandomized = false;
	private int autoCrystalRandomizedMin = 0;
	private int autoCrystalRandomizedMax = 2;
	private String autoCrystalCrystalMode = "BOTH";
	private boolean autoCrystalDoubleTap = false;
	private boolean autoCrystalDamageTick = true;
	private boolean autoCrystalPauseOnKill = true;
	private boolean autoCrystalHeadBob = false;
	private boolean autoCrystalSilent = true;
	private boolean autoCrystalAvoidSilentAura = true;
	private boolean autoCrystalAvoidAnchorAura = true;
	private int silentAuraBind = -1;
	private boolean silentAuraEnabled = true;
	private boolean silentAuraAvoidAutoCrystal = true;
	private boolean silentAuraAvoidAnchorAura = true;
	private int anchorAuraBind = -1;
	private boolean anchorAuraEnabled = true;
	private int anchorAuraDelay = 2;
	private boolean anchorAuraRandomized = false;
	private int anchorAuraRandomizedMin = 0;
	private int anchorAuraRandomizedMax = 2;
	private boolean anchorAuraSilentAim = true;
	private boolean anchorAuraExtremelyOp = false;
	private boolean anchorAuraAutoSwap = true;
	private boolean anchorAuraOnlyOwnAnchors = false;
	private boolean anchorAuraAutomaticSafeAnchor = true;
	private boolean anchorAuraAvoidAutoCrystal = true;
	private boolean anchorAuraAvoidSilentAura = true;
	private int autoTotemBind = -1;
	private boolean autoTotemEnabled = true;
	private int autoTotemSpeed = 4;
	private boolean autoTotemRandomized = false;
	private int autoTotemRandomizedMin = 0;
	private int autoTotemRandomizedMax = 2;
	private String autoTotemActions = "DOUBLE_HAND";
	private boolean autoTotemDoubleHand = true;
	private boolean autoTotemOpenInventory = false;
	private boolean autoTotemHoverTotem = true;
	private boolean autoTotemLegitMouseMovements = false;
	private boolean autoTotemDynamicDelay = true;
	private int autoMaceBind = -1;
	private boolean autoMaceEnabled = true;
	private int autoMaceDelay = 2;
	private boolean autoMaceRandomized = false;
	private int autoMaceRandomizedMin = 0;
	private int autoMaceRandomizedMax = 2;
	private boolean autoMaceTriggerbotTiming = true;
	private boolean autoMaceSilentAim = true;
	private boolean autoMaceStunSlam = true;
	private boolean autoMaceBreachCheck = true;
	private int autoPearlCatchBind = -1;
	private boolean autoPearlCatchEnabled = true;
	private int autoPearlCatchDelay = 2;
	private boolean autoPearlCatchRandomized = false;
	private int autoPearlCatchRandomizedMin = 0;
	private int autoPearlCatchRandomizedMax = 2;
	private String autoPearlCatchRotationMode = "SILENT";
	private int autoWTapBind = -1;
	private boolean autoWTapEnabled = true;
	private int autoWTapChance = 100;
	private int autoWTapReactionDelay = 1;
	private boolean autoWTapRandomized = false;
	private int autoWTapRandomizedMin = 0;
	private int autoWTapRandomizedMax = 2;
	private int autoWTapHoldLength = 2;
	private int menuBind = GLFW.GLFW_KEY_RIGHT_SHIFT;

	private ModuleSettings() {
	}

	public static ModuleSettings instance() {
		return INSTANCE;
	}

	public synchronized void load() {
		if (loaded) {
			return;
		}

		properties.clear();
		if (Files.exists(CONFIG_PATH)) {
			try (InputStream inputStream = Files.newInputStream(CONFIG_PATH)) {
				properties.load(inputStream);
			} catch (IOException ignored) {
			}
		}

		triggerbotBind = readInt("triggerbot.bind", -1);
		triggerbotEnabled = readBoolean("triggerbot.enabled", true);
		aimAssistBind = readInt("aimAssist.bind", -1);
		aimAssistEnabled = readBoolean("aimAssist.enabled", true);
		autoCrystalBind = readInt("autoCrystal.bind", -1);
		autoCrystalEnabled = readBoolean("autoCrystal.enabled", true);
		autoCrystalDelay = readInt("autoCrystal.delay", 2);
		autoCrystalRandomized = readBoolean("autoCrystal.randomized", false);
		autoCrystalRandomizedMin = readInt("autoCrystal.randomizedMin", 0);
		autoCrystalRandomizedMax = readInt("autoCrystal.randomizedMax", 2);
		autoCrystalCrystalMode = readString("autoCrystal.crystalMode", "BOTH");
		autoCrystalDoubleTap = readBoolean("autoCrystal.doubleTap", false);
		autoCrystalDamageTick = readBoolean("autoCrystal.damageTick", true);
		autoCrystalPauseOnKill = readBoolean("autoCrystal.pauseOnKill", true);
		autoCrystalHeadBob = readBoolean("autoCrystal.headBob", false);
		autoCrystalSilent = readBoolean("autoCrystal.silent", true);
		autoCrystalAvoidSilentAura = readBoolean("autoCrystal.avoidSilentAura", true);
		autoCrystalAvoidAnchorAura = readBoolean("autoCrystal.avoidAnchorAura", true);
		silentAuraBind = readInt("silentAura.bind", -1);
		silentAuraEnabled = readBoolean("silentAura.enabled", true);
		silentAuraAvoidAutoCrystal = readBoolean("silentAura.avoidAutoCrystal", true);
		silentAuraAvoidAnchorAura = readBoolean("silentAura.avoidAnchorAura", true);
		anchorAuraBind = readInt("anchorAura.bind", -1);
		anchorAuraEnabled = readBoolean("anchorAura.enabled", true);
		anchorAuraDelay = readInt("anchorAura.delay", 2);
		anchorAuraRandomized = readBoolean("anchorAura.randomized", false);
		anchorAuraRandomizedMin = readInt("anchorAura.randomizedMin", 0);
		anchorAuraRandomizedMax = readInt("anchorAura.randomizedMax", 2);
		anchorAuraSilentAim = readBoolean("anchorAura.silentAim", true);
		anchorAuraExtremelyOp = readBoolean("anchorAura.extremelyOp", false);
		anchorAuraAutoSwap = readBoolean("anchorAura.autoSwap", true);
		anchorAuraOnlyOwnAnchors = readBoolean("anchorAura.onlyOwnAnchors", false);
		anchorAuraAutomaticSafeAnchor = readBoolean("anchorAura.automaticSafeAnchor", true);
		anchorAuraAvoidAutoCrystal = readBoolean("anchorAura.avoidAutoCrystal", true);
		anchorAuraAvoidSilentAura = readBoolean("anchorAura.avoidSilentAura", true);
		autoTotemBind = readInt("autoTotem.bind", -1);
		autoTotemEnabled = readBoolean("autoTotem.enabled", true);
		autoTotemSpeed = readInt("autoTotem.speed", 4);
		autoTotemRandomized = readBoolean("autoTotem.randomized", false);
		autoTotemRandomizedMin = readInt("autoTotem.randomizedMin", 0);
		autoTotemRandomizedMax = readInt("autoTotem.randomizedMax", 2);
		autoTotemActions = readString("autoTotem.actions", "DOUBLE_HAND");
		autoTotemDoubleHand = readBoolean("autoTotem.doubleHand", true);
		autoTotemOpenInventory = readBoolean("autoTotem.openInventory", false);
		autoTotemHoverTotem = readBoolean("autoTotem.hoverTotem", true);
		autoTotemLegitMouseMovements = readBoolean("autoTotem.legitMouseMovements", false);
		autoTotemDynamicDelay = readBoolean("autoTotem.dynamicDelay", true);
		autoMaceBind = readInt("autoMace.bind", -1);
		autoMaceEnabled = readBoolean("autoMace.enabled", true);
		autoMaceDelay = readInt("autoMace.delay", 2);
		autoMaceRandomized = readBoolean("autoMace.randomized", false);
		autoMaceRandomizedMin = readInt("autoMace.randomizedMin", 0);
		autoMaceRandomizedMax = readInt("autoMace.randomizedMax", 2);
		autoMaceTriggerbotTiming = readBoolean("autoMace.triggerbotTiming", true);
		autoMaceSilentAim = readBoolean("autoMace.silentAim", true);
		autoMaceStunSlam = readBoolean("autoMace.stunSlam", true);
		autoMaceBreachCheck = readBoolean("autoMace.breachCheck", true);
		autoPearlCatchBind = readInt("autoPearlCatch.bind", -1);
		autoPearlCatchEnabled = readBoolean("autoPearlCatch.enabled", true);
		autoPearlCatchDelay = readInt("autoPearlCatch.delay", 2);
		autoPearlCatchRandomized = readBoolean("autoPearlCatch.randomized", false);
		autoPearlCatchRandomizedMin = readInt("autoPearlCatch.randomizedMin", 0);
		autoPearlCatchRandomizedMax = readInt("autoPearlCatch.randomizedMax", 2);
		autoPearlCatchRotationMode = readString("autoPearlCatch.rotationMode", "SILENT");
		autoWTapBind = readInt("autoWTap.bind", -1);
		autoWTapEnabled = readBoolean("autoWTap.enabled", true);
		autoWTapChance = readInt("autoWTap.chance", 100);
		autoWTapReactionDelay = readInt("autoWTap.reactionDelay", 1);
		autoWTapRandomized = readBoolean("autoWTap.randomized", false);
		autoWTapRandomizedMin = readInt("autoWTap.randomizedMin", 0);
		autoWTapRandomizedMax = readInt("autoWTap.randomizedMax", 2);
		autoWTapHoldLength = readInt("autoWTap.holdLength", 2);
		menuBind = readInt("menu.bind", GLFW.GLFW_KEY_RIGHT_SHIFT);
		loaded = true;
		save();
	}

	public synchronized void save() {
		properties.setProperty("triggerbot.bind", Integer.toString(triggerbotBind));
		properties.setProperty("triggerbot.enabled", Boolean.toString(triggerbotEnabled));
		properties.setProperty("aimAssist.bind", Integer.toString(aimAssistBind));
		properties.setProperty("aimAssist.enabled", Boolean.toString(aimAssistEnabled));
		properties.setProperty("autoCrystal.bind", Integer.toString(autoCrystalBind));
		properties.setProperty("autoCrystal.enabled", Boolean.toString(autoCrystalEnabled));
		properties.setProperty("autoCrystal.delay", Integer.toString(autoCrystalDelay));
		properties.setProperty("autoCrystal.randomized", Boolean.toString(autoCrystalRandomized));
		properties.setProperty("autoCrystal.randomizedMin", Integer.toString(autoCrystalRandomizedMin));
		properties.setProperty("autoCrystal.randomizedMax", Integer.toString(autoCrystalRandomizedMax));
		properties.setProperty("autoCrystal.crystalMode", autoCrystalCrystalMode);
		properties.setProperty("autoCrystal.doubleTap", Boolean.toString(autoCrystalDoubleTap));
		properties.setProperty("autoCrystal.damageTick", Boolean.toString(autoCrystalDamageTick));
		properties.setProperty("autoCrystal.pauseOnKill", Boolean.toString(autoCrystalPauseOnKill));
		properties.setProperty("autoCrystal.headBob", Boolean.toString(autoCrystalHeadBob));
		properties.setProperty("autoCrystal.silent", Boolean.toString(autoCrystalSilent));
		properties.setProperty("autoCrystal.avoidSilentAura", Boolean.toString(autoCrystalAvoidSilentAura));
		properties.setProperty("autoCrystal.avoidAnchorAura", Boolean.toString(autoCrystalAvoidAnchorAura));
		properties.setProperty("silentAura.bind", Integer.toString(silentAuraBind));
		properties.setProperty("silentAura.enabled", Boolean.toString(silentAuraEnabled));
		properties.setProperty("silentAura.avoidAutoCrystal", Boolean.toString(silentAuraAvoidAutoCrystal));
		properties.setProperty("silentAura.avoidAnchorAura", Boolean.toString(silentAuraAvoidAnchorAura));
		properties.setProperty("anchorAura.bind", Integer.toString(anchorAuraBind));
		properties.setProperty("anchorAura.enabled", Boolean.toString(anchorAuraEnabled));
		properties.setProperty("anchorAura.delay", Integer.toString(anchorAuraDelay));
		properties.setProperty("anchorAura.randomized", Boolean.toString(anchorAuraRandomized));
		properties.setProperty("anchorAura.randomizedMin", Integer.toString(anchorAuraRandomizedMin));
		properties.setProperty("anchorAura.randomizedMax", Integer.toString(anchorAuraRandomizedMax));
		properties.setProperty("anchorAura.silentAim", Boolean.toString(anchorAuraSilentAim));
		properties.setProperty("anchorAura.extremelyOp", Boolean.toString(anchorAuraExtremelyOp));
		properties.setProperty("anchorAura.autoSwap", Boolean.toString(anchorAuraAutoSwap));
		properties.setProperty("anchorAura.onlyOwnAnchors", Boolean.toString(anchorAuraOnlyOwnAnchors));
		properties.setProperty("anchorAura.automaticSafeAnchor", Boolean.toString(anchorAuraAutomaticSafeAnchor));
		properties.setProperty("anchorAura.avoidAutoCrystal", Boolean.toString(anchorAuraAvoidAutoCrystal));
		properties.setProperty("anchorAura.avoidSilentAura", Boolean.toString(anchorAuraAvoidSilentAura));
		properties.setProperty("autoTotem.bind", Integer.toString(autoTotemBind));
		properties.setProperty("autoTotem.enabled", Boolean.toString(autoTotemEnabled));
		properties.setProperty("autoTotem.speed", Integer.toString(autoTotemSpeed));
		properties.setProperty("autoTotem.randomized", Boolean.toString(autoTotemRandomized));
		properties.setProperty("autoTotem.randomizedMin", Integer.toString(autoTotemRandomizedMin));
		properties.setProperty("autoTotem.randomizedMax", Integer.toString(autoTotemRandomizedMax));
		properties.setProperty("autoTotem.actions", autoTotemActions);
		properties.setProperty("autoTotem.doubleHand", Boolean.toString(autoTotemDoubleHand));
		properties.setProperty("autoTotem.openInventory", Boolean.toString(autoTotemOpenInventory));
		properties.setProperty("autoTotem.hoverTotem", Boolean.toString(autoTotemHoverTotem));
		properties.setProperty("autoTotem.legitMouseMovements", Boolean.toString(autoTotemLegitMouseMovements));
		properties.setProperty("autoTotem.dynamicDelay", Boolean.toString(autoTotemDynamicDelay));
		properties.setProperty("autoMace.bind", Integer.toString(autoMaceBind));
		properties.setProperty("autoMace.enabled", Boolean.toString(autoMaceEnabled));
		properties.setProperty("autoMace.delay", Integer.toString(autoMaceDelay));
		properties.setProperty("autoMace.randomized", Boolean.toString(autoMaceRandomized));
		properties.setProperty("autoMace.randomizedMin", Integer.toString(autoMaceRandomizedMin));
		properties.setProperty("autoMace.randomizedMax", Integer.toString(autoMaceRandomizedMax));
		properties.setProperty("autoMace.triggerbotTiming", Boolean.toString(autoMaceTriggerbotTiming));
		properties.setProperty("autoMace.silentAim", Boolean.toString(autoMaceSilentAim));
		properties.setProperty("autoMace.stunSlam", Boolean.toString(autoMaceStunSlam));
		properties.setProperty("autoMace.breachCheck", Boolean.toString(autoMaceBreachCheck));
		properties.setProperty("autoPearlCatch.bind", Integer.toString(autoPearlCatchBind));
		properties.setProperty("autoPearlCatch.enabled", Boolean.toString(autoPearlCatchEnabled));
		properties.setProperty("autoPearlCatch.delay", Integer.toString(autoPearlCatchDelay));
		properties.setProperty("autoPearlCatch.randomized", Boolean.toString(autoPearlCatchRandomized));
		properties.setProperty("autoPearlCatch.randomizedMin", Integer.toString(autoPearlCatchRandomizedMin));
		properties.setProperty("autoPearlCatch.randomizedMax", Integer.toString(autoPearlCatchRandomizedMax));
		properties.setProperty("autoPearlCatch.rotationMode", autoPearlCatchRotationMode);
		properties.setProperty("autoWTap.bind", Integer.toString(autoWTapBind));
		properties.setProperty("autoWTap.enabled", Boolean.toString(autoWTapEnabled));
		properties.setProperty("autoWTap.chance", Integer.toString(autoWTapChance));
		properties.setProperty("autoWTap.reactionDelay", Integer.toString(autoWTapReactionDelay));
		properties.setProperty("autoWTap.randomized", Boolean.toString(autoWTapRandomized));
		properties.setProperty("autoWTap.randomizedMin", Integer.toString(autoWTapRandomizedMin));
		properties.setProperty("autoWTap.randomizedMax", Integer.toString(autoWTapRandomizedMax));
		properties.setProperty("autoWTap.holdLength", Integer.toString(autoWTapHoldLength));
		properties.setProperty("menu.bind", Integer.toString(menuBind));

		try {
			Path parent = CONFIG_PATH.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			try (OutputStream outputStream = Files.newOutputStream(CONFIG_PATH)) {
				properties.store(outputStream, "Triggerbot module settings");
			}
		} catch (IOException ignored) {
		}
	}

	public synchronized int triggerbotBind() {
		load();
		return triggerbotBind;
	}

	public synchronized void setTriggerbotBind(int value) {
		load();
		triggerbotBind = value;
		save();
	}

	public synchronized boolean triggerbotEnabled() {
		load();
		return triggerbotEnabled;
	}

	public synchronized void setTriggerbotEnabled(boolean value) {
		load();
		triggerbotEnabled = value;
		save();
	}

	public synchronized int aimAssistBind() {
		load();
		return aimAssistBind;
	}

	public synchronized void setAimAssistBind(int value) {
		load();
		aimAssistBind = value;
		save();
	}

	public synchronized boolean aimAssistEnabled() {
		load();
		return aimAssistEnabled;
	}

	public synchronized void setAimAssistEnabled(boolean value) {
		load();
		aimAssistEnabled = value;
		save();
	}

	public synchronized int autoCrystalBind() {
		load();
		return autoCrystalBind;
	}

	public synchronized void setAutoCrystalBind(int value) {
		load();
		autoCrystalBind = value;
		save();
	}

	public synchronized int autoCrystalDelay() {
		load();
		return autoCrystalDelay;
	}

	public synchronized void setAutoCrystalDelay(int value) {
		load();
		autoCrystalDelay = value;
		save();
	}

	public synchronized boolean autoCrystalRandomized() {
		load();
		return autoCrystalRandomized;
	}

	public synchronized void setAutoCrystalRandomized(boolean value) {
		load();
		autoCrystalRandomized = value;
		save();
	}

	public synchronized int autoCrystalRandomizedMin() {
		load();
		return autoCrystalRandomizedMin;
	}

	public synchronized void setAutoCrystalRandomizedMin(int value) {
		load();
		autoCrystalRandomizedMin = value;
		save();
	}

	public synchronized int autoCrystalRandomizedMax() {
		load();
		return autoCrystalRandomizedMax;
	}

	public synchronized void setAutoCrystalRandomizedMax(int value) {
		load();
		autoCrystalRandomizedMax = value;
		save();
	}

	public synchronized String autoCrystalCrystalMode() {
		load();
		return autoCrystalCrystalMode;
	}

	public synchronized void setAutoCrystalCrystalMode(String value) {
		load();
		autoCrystalCrystalMode = value;
		save();
	}

	public synchronized boolean autoCrystalDoubleTap() {
		load();
		return autoCrystalDoubleTap;
	}

	public synchronized void setAutoCrystalDoubleTap(boolean value) {
		load();
		autoCrystalDoubleTap = value;
		save();
	}

	public synchronized boolean autoCrystalDamageTick() {
		load();
		return autoCrystalDamageTick;
	}

	public synchronized void setAutoCrystalDamageTick(boolean value) {
		load();
		autoCrystalDamageTick = value;
		save();
	}

	public synchronized boolean autoCrystalPauseOnKill() {
		load();
		return autoCrystalPauseOnKill;
	}

	public synchronized void setAutoCrystalPauseOnKill(boolean value) {
		load();
		autoCrystalPauseOnKill = value;
		save();
	}

	public synchronized boolean autoCrystalHeadBob() {
		load();
		return autoCrystalHeadBob;
	}

	public synchronized void setAutoCrystalHeadBob(boolean value) {
		load();
		autoCrystalHeadBob = value;
		save();
	}

	public synchronized boolean autoCrystalSilent() {
		load();
		return autoCrystalSilent;
	}

	public synchronized void setAutoCrystalSilent(boolean value) {
		load();
		autoCrystalSilent = value;
		save();
	}

	public synchronized boolean autoCrystalAvoidSilentAura() {
		load();
		return autoCrystalAvoidSilentAura;
	}

	public synchronized void setAutoCrystalAvoidSilentAura(boolean value) {
		load();
		autoCrystalAvoidSilentAura = value;
		save();
	}

	public synchronized boolean autoCrystalAvoidAnchorAura() {
		load();
		return autoCrystalAvoidAnchorAura;
	}

	public synchronized void setAutoCrystalAvoidAnchorAura(boolean value) {
		load();
		autoCrystalAvoidAnchorAura = value;
		save();
	}

	public synchronized boolean autoCrystalEnabled() {
		load();
		return autoCrystalEnabled;
	}

	public synchronized void setAutoCrystalEnabled(boolean value) {
		load();
		autoCrystalEnabled = value;
		save();
	}

	public synchronized int silentAuraBind() {
		load();
		return silentAuraBind;
	}

	public synchronized void setSilentAuraBind(int value) {
		load();
		silentAuraBind = value;
		save();
	}

	public synchronized boolean silentAuraEnabled() {
		load();
		return silentAuraEnabled;
	}

	public synchronized void setSilentAuraEnabled(boolean value) {
		load();
		silentAuraEnabled = value;
		save();
	}

	public synchronized boolean silentAuraAvoidAutoCrystal() {
		load();
		return silentAuraAvoidAutoCrystal;
	}

	public synchronized void setSilentAuraAvoidAutoCrystal(boolean value) {
		load();
		silentAuraAvoidAutoCrystal = value;
		save();
	}

	public synchronized boolean silentAuraAvoidAnchorAura() {
		load();
		return silentAuraAvoidAnchorAura;
	}

	public synchronized void setSilentAuraAvoidAnchorAura(boolean value) {
		load();
		silentAuraAvoidAnchorAura = value;
		save();
	}

	public synchronized int anchorAuraBind() {
		load();
		return anchorAuraBind;
	}

	public synchronized void setAnchorAuraBind(int value) {
		load();
		anchorAuraBind = value;
		save();
	}

	public synchronized boolean anchorAuraEnabled() {
		load();
		return anchorAuraEnabled;
	}

	public synchronized void setAnchorAuraEnabled(boolean value) {
		load();
		anchorAuraEnabled = value;
		save();
	}

	public synchronized int anchorAuraDelay() {
		load();
		return anchorAuraDelay;
	}

	public synchronized void setAnchorAuraDelay(int value) {
		load();
		anchorAuraDelay = value;
		save();
	}

	public synchronized boolean anchorAuraRandomized() {
		load();
		return anchorAuraRandomized;
	}

	public synchronized void setAnchorAuraRandomized(boolean value) {
		load();
		anchorAuraRandomized = value;
		save();
	}

	public synchronized int anchorAuraRandomizedMin() {
		load();
		return anchorAuraRandomizedMin;
	}

	public synchronized void setAnchorAuraRandomizedMin(int value) {
		load();
		anchorAuraRandomizedMin = value;
		save();
	}

	public synchronized int anchorAuraRandomizedMax() {
		load();
		return anchorAuraRandomizedMax;
	}

	public synchronized void setAnchorAuraRandomizedMax(int value) {
		load();
		anchorAuraRandomizedMax = value;
		save();
	}

	public synchronized boolean anchorAuraSilentAim() {
		load();
		return anchorAuraSilentAim;
	}

	public synchronized void setAnchorAuraSilentAim(boolean value) {
		load();
		anchorAuraSilentAim = value;
		save();
	}

	public synchronized boolean anchorAuraExtremelyOp() {
		load();
		return anchorAuraExtremelyOp;
	}

	public synchronized void setAnchorAuraExtremelyOp(boolean value) {
		load();
		anchorAuraExtremelyOp = value;
		save();
	}

	public synchronized boolean anchorAuraAutoSwap() {
		load();
		return anchorAuraAutoSwap;
	}

	public synchronized void setAnchorAuraAutoSwap(boolean value) {
		load();
		anchorAuraAutoSwap = value;
		save();
	}

	public synchronized boolean anchorAuraOnlyOwnAnchors() {
		load();
		return anchorAuraOnlyOwnAnchors;
	}

	public synchronized void setAnchorAuraOnlyOwnAnchors(boolean value) {
		load();
		anchorAuraOnlyOwnAnchors = value;
		save();
	}

	public synchronized boolean anchorAuraAutomaticSafeAnchor() {
		load();
		return anchorAuraAutomaticSafeAnchor;
	}

	public synchronized void setAnchorAuraAutomaticSafeAnchor(boolean value) {
		load();
		anchorAuraAutomaticSafeAnchor = value;
		save();
	}

	public synchronized boolean anchorAuraAvoidAutoCrystal() {
		load();
		return anchorAuraAvoidAutoCrystal;
	}

	public synchronized void setAnchorAuraAvoidAutoCrystal(boolean value) {
		load();
		anchorAuraAvoidAutoCrystal = value;
		save();
	}

	public synchronized boolean anchorAuraAvoidSilentAura() {
		load();
		return anchorAuraAvoidSilentAura;
	}

	public synchronized void setAnchorAuraAvoidSilentAura(boolean value) {
		load();
		anchorAuraAvoidSilentAura = value;
		save();
	}

	public synchronized int autoTotemBind() {
		load();
		return autoTotemBind;
	}

	public synchronized void setAutoTotemBind(int value) {
		load();
		autoTotemBind = value;
		save();
	}

	public synchronized boolean autoTotemEnabled() {
		load();
		return autoTotemEnabled;
	}

	public synchronized void setAutoTotemEnabled(boolean value) {
		load();
		autoTotemEnabled = value;
		save();
	}

	public synchronized int autoTotemSpeed() {
		load();
		return autoTotemSpeed;
	}

	public synchronized void setAutoTotemSpeed(int value) {
		load();
		autoTotemSpeed = value;
		save();
	}

	public synchronized boolean autoTotemRandomized() {
		load();
		return autoTotemRandomized;
	}

	public synchronized void setAutoTotemRandomized(boolean value) {
		load();
		autoTotemRandomized = value;
		save();
	}

	public synchronized int autoTotemRandomizedMin() {
		load();
		return autoTotemRandomizedMin;
	}

	public synchronized void setAutoTotemRandomizedMin(int value) {
		load();
		autoTotemRandomizedMin = value;
		save();
	}

	public synchronized int autoTotemRandomizedMax() {
		load();
		return autoTotemRandomizedMax;
	}

	public synchronized void setAutoTotemRandomizedMax(int value) {
		load();
		autoTotemRandomizedMax = value;
		save();
	}

	public synchronized String autoTotemActions() {
		load();
		return autoTotemActions;
	}

	public synchronized void setAutoTotemActions(String value) {
		load();
		autoTotemActions = value;
		save();
	}

	public synchronized boolean autoTotemDoubleHand() {
		load();
		return autoTotemDoubleHand;
	}

	public synchronized void setAutoTotemDoubleHand(boolean value) {
		load();
		autoTotemDoubleHand = value;
		save();
	}

	public synchronized boolean autoTotemOpenInventory() {
		load();
		return autoTotemOpenInventory;
	}

	public synchronized void setAutoTotemOpenInventory(boolean value) {
		load();
		autoTotemOpenInventory = value;
		save();
	}

	public synchronized boolean autoTotemHoverTotem() {
		load();
		return autoTotemHoverTotem;
	}

	public synchronized void setAutoTotemHoverTotem(boolean value) {
		load();
		autoTotemHoverTotem = value;
		save();
	}

	public synchronized boolean autoTotemLegitMouseMovements() {
		load();
		return autoTotemLegitMouseMovements;
	}

	public synchronized void setAutoTotemLegitMouseMovements(boolean value) {
		load();
		autoTotemLegitMouseMovements = value;
		save();
	}

	public synchronized boolean autoTotemDynamicDelay() {
		load();
		return autoTotemDynamicDelay;
	}

	public synchronized void setAutoTotemDynamicDelay(boolean value) {
		load();
		autoTotemDynamicDelay = value;
		save();
	}

	public synchronized int autoMaceBind() {
		load();
		return autoMaceBind;
	}

	public synchronized void setAutoMaceBind(int value) {
		load();
		autoMaceBind = value;
		save();
	}

	public synchronized boolean autoMaceEnabled() {
		load();
		return autoMaceEnabled;
	}

	public synchronized void setAutoMaceEnabled(boolean value) {
		load();
		autoMaceEnabled = value;
		save();
	}

	public synchronized int autoMaceDelay() {
		load();
		return autoMaceDelay;
	}

	public synchronized void setAutoMaceDelay(int value) {
		load();
		autoMaceDelay = value;
		save();
	}

	public synchronized boolean autoMaceRandomized() {
		load();
		return autoMaceRandomized;
	}

	public synchronized void setAutoMaceRandomized(boolean value) {
		load();
		autoMaceRandomized = value;
		save();
	}

	public synchronized int autoMaceRandomizedMin() {
		load();
		return autoMaceRandomizedMin;
	}

	public synchronized void setAutoMaceRandomizedMin(int value) {
		load();
		autoMaceRandomizedMin = value;
		save();
	}

	public synchronized int autoMaceRandomizedMax() {
		load();
		return autoMaceRandomizedMax;
	}

	public synchronized void setAutoMaceRandomizedMax(int value) {
		load();
		autoMaceRandomizedMax = value;
		save();
	}

	public synchronized boolean autoMaceTriggerbotTiming() {
		load();
		return autoMaceTriggerbotTiming;
	}

	public synchronized void setAutoMaceTriggerbotTiming(boolean value) {
		load();
		autoMaceTriggerbotTiming = value;
		save();
	}

	public synchronized boolean autoMaceSilentAim() {
		load();
		return autoMaceSilentAim;
	}

	public synchronized void setAutoMaceSilentAim(boolean value) {
		load();
		autoMaceSilentAim = value;
		save();
	}

	public synchronized boolean autoMaceStunSlam() {
		load();
		return autoMaceStunSlam;
	}

	public synchronized void setAutoMaceStunSlam(boolean value) {
		load();
		autoMaceStunSlam = value;
		save();
	}

	public synchronized boolean autoMaceBreachCheck() {
		load();
		return autoMaceBreachCheck;
	}

	public synchronized void setAutoMaceBreachCheck(boolean value) {
		load();
		autoMaceBreachCheck = value;
		save();
	}

	public synchronized int autoPearlCatchBind() {
		load();
		return autoPearlCatchBind;
	}

	public synchronized void setAutoPearlCatchBind(int value) {
		load();
		autoPearlCatchBind = value;
		save();
	}

	public synchronized boolean autoPearlCatchEnabled() {
		load();
		return autoPearlCatchEnabled;
	}

	public synchronized void setAutoPearlCatchEnabled(boolean value) {
		load();
		autoPearlCatchEnabled = value;
		save();
	}

	public synchronized int autoPearlCatchDelay() {
		load();
		return autoPearlCatchDelay;
	}

	public synchronized void setAutoPearlCatchDelay(int value) {
		load();
		autoPearlCatchDelay = value;
		save();
	}

	public synchronized boolean autoPearlCatchRandomized() {
		load();
		return autoPearlCatchRandomized;
	}

	public synchronized void setAutoPearlCatchRandomized(boolean value) {
		load();
		autoPearlCatchRandomized = value;
		save();
	}

	public synchronized int autoPearlCatchRandomizedMin() {
		load();
		return autoPearlCatchRandomizedMin;
	}

	public synchronized void setAutoPearlCatchRandomizedMin(int value) {
		load();
		autoPearlCatchRandomizedMin = value;
		save();
	}

	public synchronized int autoPearlCatchRandomizedMax() {
		load();
		return autoPearlCatchRandomizedMax;
	}

	public synchronized void setAutoPearlCatchRandomizedMax(int value) {
		load();
		autoPearlCatchRandomizedMax = value;
		save();
	}

	public synchronized String autoPearlCatchRotationMode() {
		load();
		return autoPearlCatchRotationMode;
	}

	public synchronized void setAutoPearlCatchRotationMode(String value) {
		load();
		autoPearlCatchRotationMode = value;
		save();
	}

	public synchronized int autoWTapBind() {
		load();
		return autoWTapBind;
	}

	public synchronized void setAutoWTapBind(int value) {
		load();
		autoWTapBind = value;
		save();
	}

	public synchronized boolean autoWTapEnabled() {
		load();
		return autoWTapEnabled;
	}

	public synchronized void setAutoWTapEnabled(boolean value) {
		load();
		autoWTapEnabled = value;
		save();
	}

	public synchronized int autoWTapChance() {
		load();
		return autoWTapChance;
	}

	public synchronized void setAutoWTapChance(int value) {
		load();
		autoWTapChance = value;
		save();
	}

	public synchronized int autoWTapReactionDelay() {
		load();
		return autoWTapReactionDelay;
	}

	public synchronized void setAutoWTapReactionDelay(int value) {
		load();
		autoWTapReactionDelay = value;
		save();
	}

	public synchronized boolean autoWTapRandomized() {
		load();
		return autoWTapRandomized;
	}

	public synchronized void setAutoWTapRandomized(boolean value) {
		load();
		autoWTapRandomized = value;
		save();
	}

	public synchronized int autoWTapRandomizedMin() {
		load();
		return autoWTapRandomizedMin;
	}

	public synchronized void setAutoWTapRandomizedMin(int value) {
		load();
		autoWTapRandomizedMin = value;
		save();
	}

	public synchronized int autoWTapRandomizedMax() {
		load();
		return autoWTapRandomizedMax;
	}

	public synchronized void setAutoWTapRandomizedMax(int value) {
		load();
		autoWTapRandomizedMax = value;
		save();
	}

	public synchronized int autoWTapHoldLength() {
		load();
		return autoWTapHoldLength;
	}

	public synchronized void setAutoWTapHoldLength(int value) {
		load();
		autoWTapHoldLength = value;
		save();
	}

	public synchronized int menuBind() {
		load();
		return menuBind;
	}

	public synchronized void setMenuBind(int value) {
		load();
		menuBind = value;
		save();
	}

	private int readInt(String key, int fallback) {
		String value = properties.getProperty(key);
		if (value == null) {
			return fallback;
		}

		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException ignored) {
			return fallback;
		}
	}

	private boolean readBoolean(String key, boolean fallback) {
		String value = properties.getProperty(key);
		if (value == null) {
			return fallback;
		}

		return Boolean.parseBoolean(value.trim());
	}

	private String readString(String key, String fallback) {
		String value = properties.getProperty(key);
		if (value == null) {
			return fallback;
		}

		String trimmed = value.trim();
		return trimmed.isEmpty() ? fallback : trimmed;
	}
}