package com.example;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerbotMod implements ModInitializer {

	public static final String MOD_ID = "triggerbot";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	static {
		// Best-effort: some launchers force AWT headless. Must run early if any future UI uses Swing.
		System.setProperty("java.awt.headless", "false");
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Triggerbot mod loaded.");
	}
}