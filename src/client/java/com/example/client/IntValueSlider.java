package com.example.client;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;

/** Simple integer slider for module settings screens. */
public final class IntValueSlider extends AbstractSliderButton {
	private final String label;
	private final int min;
	private final int max;
	private final IntConsumer onChange;

	public IntValueSlider(int x, int y, int width, int height, String label, int min, int max, int value, IntConsumer onChange) {
		super(x, y, width, height, Component.empty(), toSlider(min, max, value));
		this.label = label;
		this.min = min;
		this.max = Math.max(min, max);
		this.onChange = onChange;
		updateMessage();
	}

	public int currentValue() {
		if (max <= min) {
			return min;
		}
		return min + (int) Math.round(this.value * (max - min));
	}

	@Override
	protected void updateMessage() {
		this.setMessage(Component.literal(label + ": " + currentValue()));
	}

	@Override
	protected void applyValue() {
		onChange.accept(currentValue());
		updateMessage();
	}

	private static double toSlider(int min, int max, int value) {
		if (max <= min) {
			return 0.0D;
		}
		int clamped = Math.max(min, Math.min(max, value));
		return (clamped - min) / (double) (max - min);
	}
}
