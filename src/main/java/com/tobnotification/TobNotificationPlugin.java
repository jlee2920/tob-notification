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
import net.runelite.api.coords.WorldPoint;

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

	// Default timeouts for different encounters
	private static final int soteTimeout = 10;
	private static final int xarpTimeout = 5;
	private static final int verzTimeout = 7;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TobNotificationOverlay overlay;
	public String boss;

	// Instant objects when the different tob events start
	private Instant ballStart;
	private Instant stareStart;
	private Instant greenBallStart;

	// Map IDs to ensure this plugin only works within Tob
	private static final int SOTE_REGION = 13123;
	private static final int SOTE_MAZE_REGION = 13379;
	private static final int XARPUS_REGION = 12612;
	private static final int VERZIK_REGION = 12611;

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
		if (isInTob()) {
			if (ballStart != null) {
				// Once you enter the maze, instantly clear the overlay
				if (WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() == SOTE_MAZE_REGION) {
					overlayManager.remove(overlay);
					ballStart = null;
					return;
				}

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
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		if (isInTob() && chatMessage.getType().equals(ChatMessageType.GAMEMESSAGE)) {
			if (chatMessage.getMessage().contains("A large ball of energy is shot your way...")) {
				this.boss = "Sotetseg";
				overlayManager.add(overlay);
				ballStart = Instant.now();
			} else if (chatMessage.getMessage().contains("Xarpus begins to stare intently.")) {
				this.boss = "Xarpus";
				overlayManager.add(overlay);
				stareStart = Instant.now();
			} else if (chatMessage.getMessage().contains("Verzik Vitur fires a powerful projectile in your direction...") ||
					   chatMessage.getMessage().contains("A powerful projectile bounces into your direction...")) {
				this.boss = "Verzik";
				overlayManager.add(overlay);
				greenBallStart = Instant.now();
			}
		}
	}

	//Check to see if it's within the specified tob rooms
	private boolean isInTob() {
		return WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() == SOTE_REGION ||
				WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() == SOTE_MAZE_REGION ||
				WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() == XARPUS_REGION ||
				WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID() == VERZIK_REGION;
	}
}