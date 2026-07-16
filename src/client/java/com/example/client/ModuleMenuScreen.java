package com.example.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Main module list. Each row: toggle ON/OFF + open settings.
 */
public final class ModuleMenuScreen extends Screen {
	private static final int TOGGLE_WIDTH = 150;
	private static final int SETTINGS_WIDTH = 70;
	private static final int ROW_HEIGHT = 20;
	private static final int ROW_GAP = 22;
	private static final int MODULES_PER_PAGE = 8;

	private int page;

	public ModuleMenuScreen() {
		super(Component.literal("Triggerbot Modules"));
	}

	@Override
	protected void init() {
		ModuleSettings settings = ModuleSettings.instance();
		ModuleId[] modules = ModuleId.values();
		int totalPages = Math.max(1, (modules.length + MODULES_PER_PAGE - 1) / MODULES_PER_PAGE);
		if (page >= totalPages) {
			page = totalPages - 1;
		}

		int rowWidth = TOGGLE_WIDTH + 4 + SETTINGS_WIDTH;
		int left = this.width / 2 - rowWidth / 2;
		int y = 36;

		int from = page * MODULES_PER_PAGE;
		int to = Math.min(modules.length, from + MODULES_PER_PAGE);
		for (int i = from; i < to; i++) {
			ModuleId module = modules[i];
			boolean enabled = module.isEnabled(settings);
			String toggleLabel = module == ModuleId.MENU
				? module.displayName()
				: module.displayName() + ": " + (enabled ? "ON" : "OFF");

			if (module == ModuleId.MENU) {
				this.addRenderableWidget(Button.builder(Component.literal(toggleLabel), button ->
					this.minecraft.setScreen(new ModuleSettingsScreen(this, module))
				).bounds(left, y, rowWidth, ROW_HEIGHT).build());
			} else {
				this.addRenderableWidget(Button.builder(Component.literal(toggleLabel), button -> {
					boolean next = !module.isEnabled(ModuleSettings.instance());
					module.setEnabled(ModuleSettings.instance(), next);
					button.setMessage(Component.literal(module.displayName() + ": " + (next ? "ON" : "OFF")));
				}).bounds(left, y, TOGGLE_WIDTH, ROW_HEIGHT).build());

				this.addRenderableWidget(Button.builder(Component.literal("Settings"), button ->
					this.minecraft.setScreen(new ModuleSettingsScreen(this, module))
				).bounds(left + TOGGLE_WIDTH + 4, y, SETTINGS_WIDTH, ROW_HEIGHT).build());
			}
			y += ROW_GAP;
		}

		int navY = this.height - 28;
		int half = 100;
		this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> this.onClose())
			.bounds(this.width / 2 - half - 4, navY, half, ROW_HEIGHT)
			.build());

		if (totalPages > 1) {
			int finalTotalPages = totalPages;
			this.addRenderableWidget(Button.builder(
				Component.literal("Page " + (page + 1) + "/" + totalPages),
				button -> {
					page = (page + 1) % finalTotalPages;
					this.rebuildWidgets();
				}
			).bounds(this.width / 2 + 4, navY, half, ROW_HEIGHT).build());
		} else {
			this.addRenderableWidget(Button.builder(Component.literal("Close"), button -> this.onClose())
				.bounds(this.width / 2 + 4, navY, half, ROW_HEIGHT)
				.build());
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
		graphics.drawCenteredString(this.font, "Right Shift opens/closes · click Settings per module", this.width / 2, 24, 0xA0A0A0);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
