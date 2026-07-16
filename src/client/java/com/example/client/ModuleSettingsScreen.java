package com.example.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

/**
 * Per-module settings page. Vanilla-style widgets only (buttons / cycle / sliders).
 */
public final class ModuleSettingsScreen extends Screen {
	private static final int WIDGET_WIDTH = 220;
	private static final int WIDGET_HEIGHT = 20;
	private static final int ROW_GAP = 22;
	private static final int OPTIONS_PER_PAGE = 8;

	private final Screen parent;
	private final ModuleId module;
	private int page;

	public ModuleSettingsScreen(Screen parent, ModuleId module) {
		super(Component.literal(module.displayName() + " Settings"));
		this.parent = parent;
		this.module = module;
	}

	@Override
	protected void init() {
		ModuleSettings settings = ModuleSettings.instance();
		List<OptionEntry> options = buildOptions(settings);

		int centerX = this.width / 2 - WIDGET_WIDTH / 2;
		int startY = 36;
		int totalPages = Math.max(1, (options.size() + OPTIONS_PER_PAGE - 1) / OPTIONS_PER_PAGE);
		if (page >= totalPages) {
			page = totalPages - 1;
		}
		if (page < 0) {
			page = 0;
		}

		int from = page * OPTIONS_PER_PAGE;
		int to = Math.min(options.size(), from + OPTIONS_PER_PAGE);
		int y = startY;
		for (int i = from; i < to; i++) {
			options.get(i).add(this, centerX, y, WIDGET_WIDTH, WIDGET_HEIGHT);
			y += ROW_GAP;
		}

		int navY = this.height - 28;
		int half = 100;
		this.addRenderableWidget(Button.builder(Component.literal("Back"), button -> this.minecraft.setScreen(parent))
			.bounds(this.width / 2 - half - 4, navY, half, WIDGET_HEIGHT)
			.build());

		if (totalPages > 1) {
			int finalTotalPages = totalPages;
			this.addRenderableWidget(Button.builder(
				Component.literal("Page " + (page + 1) + "/" + totalPages),
				button -> {
					page = (page + 1) % finalTotalPages;
					this.rebuildWidgets();
				}
			).bounds(this.width / 2 + 4, navY, half, WIDGET_HEIGHT).build());
		} else {
			this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> this.minecraft.setScreen(parent))
				.bounds(this.width / 2 + 4, navY, half, WIDGET_HEIGHT)
				.build());
		}
	}

	private List<OptionEntry> buildOptions(ModuleSettings s) {
		List<OptionEntry> options = new ArrayList<>();
		switch (module) {
			case TRIGGERBOT -> {
				options.add(bool("Enabled", s::triggerbotEnabled, s::setTriggerbotEnabled));
				options.add(bind("Toggle Bind", s::triggerbotBind, s::setTriggerbotBind));
			}
			case AIM_ASSIST -> {
				options.add(bool("Enabled", s::aimAssistEnabled, s::setAimAssistEnabled));
				options.add(bind("Toggle Bind", s::aimAssistBind, s::setAimAssistBind));
			}
			case AUTO_CRYSTAL -> {
				options.add(bool("Enabled", s::autoCrystalEnabled, s::setAutoCrystalEnabled));
				options.add(bind("Toggle Bind", s::autoCrystalBind, s::setAutoCrystalBind));
				options.add(slider("Delay (ticks)", 0, 20, s::autoCrystalDelay, s::setAutoCrystalDelay));
				options.add(bool("Randomized Delay", s::autoCrystalRandomized, s::setAutoCrystalRandomized));
				options.add(slider("Random Min", 0, 20, s::autoCrystalRandomizedMin, s::setAutoCrystalRandomizedMin));
				options.add(slider("Random Max", 0, 20, s::autoCrystalRandomizedMax, s::setAutoCrystalRandomizedMax));
				options.add(cycle("Crystal Mode", List.of("BOTH", "CROSSHAIR", "NEARBY"), s::autoCrystalCrystalMode, s::setAutoCrystalCrystalMode));
				options.add(bool("Double Tap", s::autoCrystalDoubleTap, s::setAutoCrystalDoubleTap));
				options.add(bool("Damage Tick", s::autoCrystalDamageTick, s::setAutoCrystalDamageTick));
				options.add(bool("Pause On Kill", s::autoCrystalPauseOnKill, s::setAutoCrystalPauseOnKill));
				options.add(bool("Head Bob", s::autoCrystalHeadBob, s::setAutoCrystalHeadBob));
				options.add(bool("Silent", s::autoCrystalSilent, s::setAutoCrystalSilent));
				options.add(bool("Avoid Silent Aura", s::autoCrystalAvoidSilentAura, s::setAutoCrystalAvoidSilentAura));
				options.add(bool("Avoid Anchor Aura", s::autoCrystalAvoidAnchorAura, s::setAutoCrystalAvoidAnchorAura));
			}
			case SILENT_AURA -> {
				options.add(bool("Enabled", s::silentAuraEnabled, s::setSilentAuraEnabled));
				options.add(bind("Toggle Bind", s::silentAuraBind, s::setSilentAuraBind));
				options.add(bool("Avoid Auto Crystal", s::silentAuraAvoidAutoCrystal, s::setSilentAuraAvoidAutoCrystal));
				options.add(bool("Avoid Anchor Aura", s::silentAuraAvoidAnchorAura, s::setSilentAuraAvoidAnchorAura));
			}
			case ANCHOR_AURA -> {
				options.add(bool("Enabled", s::anchorAuraEnabled, s::setAnchorAuraEnabled));
				options.add(bind("Toggle Bind", s::anchorAuraBind, s::setAnchorAuraBind));
				options.add(slider("Delay (ticks)", 0, 20, s::anchorAuraDelay, s::setAnchorAuraDelay));
				options.add(bool("Randomized Delay", s::anchorAuraRandomized, s::setAnchorAuraRandomized));
				options.add(slider("Random Min", 0, 20, s::anchorAuraRandomizedMin, s::setAnchorAuraRandomizedMin));
				options.add(slider("Random Max", 0, 20, s::anchorAuraRandomizedMax, s::setAnchorAuraRandomizedMax));
				options.add(bool("Silent Aim", s::anchorAuraSilentAim, s::setAnchorAuraSilentAim));
				options.add(bool("Extremely OP", s::anchorAuraExtremelyOp, s::setAnchorAuraExtremelyOp));
				options.add(bool("Auto Swap", s::anchorAuraAutoSwap, s::setAnchorAuraAutoSwap));
				options.add(bool("Only Own Anchors", s::anchorAuraOnlyOwnAnchors, s::setAnchorAuraOnlyOwnAnchors));
				options.add(bool("Automatic Safe Anchor", s::anchorAuraAutomaticSafeAnchor, s::setAnchorAuraAutomaticSafeAnchor));
				options.add(bool("Avoid Auto Crystal", s::anchorAuraAvoidAutoCrystal, s::setAnchorAuraAvoidAutoCrystal));
				options.add(bool("Avoid Silent Aura", s::anchorAuraAvoidSilentAura, s::setAnchorAuraAvoidSilentAura));
			}
			case AUTO_TOTEM -> {
				options.add(bool("Enabled", s::autoTotemEnabled, s::setAutoTotemEnabled));
				options.add(bind("Toggle Bind", s::autoTotemBind, s::setAutoTotemBind));
				options.add(slider("Speed (ticks)", 0, 40, s::autoTotemSpeed, s::setAutoTotemSpeed));
				options.add(bool("Randomized", s::autoTotemRandomized, s::setAutoTotemRandomized));
				options.add(slider("Random Min", 0, 20, s::autoTotemRandomizedMin, s::setAutoTotemRandomizedMin));
				options.add(slider("Random Max", 0, 20, s::autoTotemRandomizedMax, s::setAutoTotemRandomizedMax));
				options.add(cycle("Actions", List.of("DOUBLE_HAND", "OPEN_INVENTORY", "HOVER"), s::autoTotemActions, s::setAutoTotemActions));
				options.add(bool("Double Hand", s::autoTotemDoubleHand, s::setAutoTotemDoubleHand));
				options.add(bool("Open Inventory", s::autoTotemOpenInventory, s::setAutoTotemOpenInventory));
				options.add(bool("Hover Totem", s::autoTotemHoverTotem, s::setAutoTotemHoverTotem));
				options.add(bool("Legit Mouse Moves", s::autoTotemLegitMouseMovements, s::setAutoTotemLegitMouseMovements));
				options.add(bool("Dynamic Delay", s::autoTotemDynamicDelay, s::setAutoTotemDynamicDelay));
			}
			case AUTO_MACE -> {
				options.add(bool("Enabled", s::autoMaceEnabled, s::setAutoMaceEnabled));
				options.add(bind("Toggle Bind", s::autoMaceBind, s::setAutoMaceBind));
				options.add(slider("Delay (ticks)", 0, 20, s::autoMaceDelay, s::setAutoMaceDelay));
				options.add(bool("Randomized Delay", s::autoMaceRandomized, s::setAutoMaceRandomized));
				options.add(slider("Random Min", 0, 20, s::autoMaceRandomizedMin, s::setAutoMaceRandomizedMin));
				options.add(slider("Random Max", 0, 20, s::autoMaceRandomizedMax, s::setAutoMaceRandomizedMax));
				options.add(bool("Triggerbot Timing", s::autoMaceTriggerbotTiming, s::setAutoMaceTriggerbotTiming));
				options.add(bool("Silent Aim", s::autoMaceSilentAim, s::setAutoMaceSilentAim));
				options.add(bool("Stun Slam", s::autoMaceStunSlam, s::setAutoMaceStunSlam));
				options.add(bool("Breach Check", s::autoMaceBreachCheck, s::setAutoMaceBreachCheck));
			}
			case AUTO_PEARL_CATCH -> {
				options.add(bool("Enabled", s::autoPearlCatchEnabled, s::setAutoPearlCatchEnabled));
				options.add(bind("Toggle Bind", s::autoPearlCatchBind, s::setAutoPearlCatchBind));
				options.add(slider("Delay (ticks)", 0, 20, s::autoPearlCatchDelay, s::setAutoPearlCatchDelay));
				options.add(bool("Randomized Delay", s::autoPearlCatchRandomized, s::setAutoPearlCatchRandomized));
				options.add(slider("Random Min", 0, 20, s::autoPearlCatchRandomizedMin, s::setAutoPearlCatchRandomizedMin));
				options.add(slider("Random Max", 0, 20, s::autoPearlCatchRandomizedMax, s::setAutoPearlCatchRandomizedMax));
				options.add(cycle("Rotation Mode", List.of("SILENT", "NORMAL", "NONE"), s::autoPearlCatchRotationMode, s::setAutoPearlCatchRotationMode));
			}
			case AUTO_W_TAP -> {
				options.add(bool("Enabled", s::autoWTapEnabled, s::setAutoWTapEnabled));
				options.add(bind("Toggle Bind", s::autoWTapBind, s::setAutoWTapBind));
				options.add(slider("Chance %", 0, 100, s::autoWTapChance, s::setAutoWTapChance));
				options.add(slider("Reaction Delay", 0, 20, s::autoWTapReactionDelay, s::setAutoWTapReactionDelay));
				options.add(slider("Hold Length", 0, 20, s::autoWTapHoldLength, s::setAutoWTapHoldLength));
				options.add(bool("Randomized", s::autoWTapRandomized, s::setAutoWTapRandomized));
				options.add(slider("Random Min", 0, 20, s::autoWTapRandomizedMin, s::setAutoWTapRandomizedMin));
				options.add(slider("Random Max", 0, 20, s::autoWTapRandomizedMax, s::setAutoWTapRandomizedMax));
			}
			case MENU -> {
				options.add(bind("Menu Bind (RShift=344)", s::menuBind, s::setMenuBind));
				options.add(button("Reset Menu Bind to RShift", () -> s.setMenuBind(GLFW.GLFW_KEY_RIGHT_SHIFT)));
			}
		}
		return options;
	}

	private static OptionEntry bool(String name, Supplier<Boolean> getter, Consumer<Boolean> setter) {
		return (screen, x, y, w, h) -> screen.addRenderableWidget(
			CycleButton.onOffBuilder(getter.get())
				.create(x, y, w, h, Component.literal(name), (button, value) -> setter.accept(value))
		);
	}

	private static OptionEntry slider(String name, int min, int max, Supplier<Integer> getter, IntConsumer setter) {
		return (screen, x, y, w, h) -> screen.addRenderableWidget(
			new IntValueSlider(x, y, w, h, name, min, max, getter.get(), setter)
		);
	}

	private static OptionEntry cycle(String name, List<String> values, Supplier<String> getter, Consumer<String> setter) {
		return (screen, x, y, w, h) -> {
			String current = getter.get();
			if (!values.contains(current)) {
				current = values.getFirst();
			}
			screen.addRenderableWidget(
				CycleButton.<String>builder(Component::literal, current)
					.withValues(values)
					.create(x, y, w, h, Component.literal(name), (button, value) -> setter.accept(value))
			);
		};
	}

	private static OptionEntry bind(String name, Supplier<Integer> getter, IntConsumer setter) {
		// Cycle common binds: unbound (-1), RShift, RCtrl, G, H, J, K, L, V, B, N, M
		int[] binds = {-1, GLFW.GLFW_KEY_RIGHT_SHIFT, GLFW.GLFW_KEY_RIGHT_CONTROL, GLFW.GLFW_KEY_G, GLFW.GLFW_KEY_H,
			GLFW.GLFW_KEY_J, GLFW.GLFW_KEY_K, GLFW.GLFW_KEY_L, GLFW.GLFW_KEY_V, GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_N, GLFW.GLFW_KEY_M};
		return (screen, x, y, w, h) -> {
			int current = getter.get();
			screen.addRenderableWidget(Button.builder(Component.literal(name + ": " + keyName(current)), button -> {
				int found = -1;
				int now = getter.get();
				for (int i = 0; i < binds.length; i++) {
					if (binds[i] == now) {
						found = i;
						break;
					}
				}
				if (found < 0) {
					found = 0;
				}
				int next = binds[(found + 1) % binds.length];
				setter.accept(next);
				button.setMessage(Component.literal(name + ": " + keyName(next)));
			}).bounds(x, y, w, h).build());
		};
	}

	private static OptionEntry button(String label, Runnable action) {
		return (screen, x, y, w, h) -> screen.addRenderableWidget(
			Button.builder(Component.literal(label), b -> {
				action.run();
				// rebuild so bind label refreshes
				if (screen instanceof ModuleSettingsScreen settingsScreen) {
					settingsScreen.rebuildWidgets();
				}
			}).bounds(x, y, w, h).build()
		);
	}

	private static String keyName(int key) {
		if (key < 0) {
			return "NONE";
		}
		return switch (key) {
			case GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT";
			case GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCTRL";
			case GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT";
			case GLFW.GLFW_KEY_LEFT_CONTROL -> "LCTRL";
			default -> {
				String name = GLFW.glfwGetKeyName(key, 0);
				yield name != null ? name.toUpperCase() : ("KEY " + key);
			}
		};
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
		graphics.drawCenteredString(this.font, "Settings save automatically", this.width / 2, 24, 0xA0A0A0);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@FunctionalInterface
	private interface OptionEntry {
		void add(ModuleSettingsScreen screen, int x, int y, int w, int h);
	}
}
