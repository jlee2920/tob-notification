package com.tobnotification;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.*;
import java.time.Duration;
import net.runelite.client.ui.overlay.OverlayManager;
import java.time.Instant;

@Slf4j
@PluginDescriptor(
	name = "Tob Notification",
	description = "Useful customizable notifications for events in tob",
	tags = {"tob", "notification"}
)

public class TobNotificationPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private TobNotificationConfig config;

	private static final int soteTimeout = 16;
	private static final int xarpTimeout = 5;
	private static final int verzTimeout = 7;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TobNotificationOverlay overlay;
	public String boss;

	private Instant ballStart;
	private Instant stareStart;
	private Instant greenBallStart;

	public String getBossName() {
		return boss;
	}

	@Provides
	TobNotificationConfig getConfig(ConfigManager configManager) {
		return configManager.getConfig(TobNotificationConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
		log.info("Tob Notification started!");
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
		log.info("Tob Notification stopped!");
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		if (ballStart != null) {
			final Duration soteBallStart = Duration.ofSeconds(soteTimeout);
			final Duration sinceBallStart = Duration.between(ballStart, Instant.now());

			if (sinceBallStart.compareTo(soteBallStart) >= 0) {
				overlayManager.remove(overlay);
				ballStart = null;
			}
		}
		if (stareStart != null) {
			final Duration xarpStareStart = Duration.ofSeconds(xarpTimeout);
			final Duration sinceStareStart = Duration.between(stareStart, Instant.now());

			if (sinceStareStart.compareTo(xarpStareStart) >= 0) {
				overlayManager.remove(overlay);
				stareStart = null;
			}
		}
		if (greenBallStart != null) {
			final Duration verzBallStart = Duration.ofSeconds(verzTimeout);
			final Duration sinceVerzBallStart = Duration.between(greenBallStart, Instant.now());

			if (sinceVerzBallStart.compareTo(verzBallStart) >= 0) {
				overlayManager.remove(overlay);
				greenBallStart = null;
			}
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		if (chatMessage.getType().equals(ChatMessageType.GAMEMESSAGE)) {
			switch (chatMessage.getMessage()) {
				case "A large ball of energy is shot your way...":
					ballStart = Instant.now();
					this.boss = "Sotetseg";
					overlayManager.add(overlay);
					break;
				case "Xarpus begins to stare intently.":
					this.boss = "Xarpus";
					overlayManager.add(overlay);
					stareStart = Instant.now();
					break;
				case "A powerful projectile bounces into your direction...":
					this.boss = "Verzik";
					overlayManager.add(overlay);
					greenBallStart = Instant.now();
					break;
			}
		}
	}
}