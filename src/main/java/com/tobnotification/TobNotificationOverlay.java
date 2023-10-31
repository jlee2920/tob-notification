package com.tobnotification;

import net.runelite.api.Client;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;


public class TobNotificationOverlay extends OverlayPanel
{
	private final TobNotificationConfig config;
	private final Client client;
	private TobNotificationPlugin plugin;

	private String message;

	@Inject
	private TobNotificationOverlay(TobNotificationConfig config, Client client, TobNotificationPlugin plugin)
	{
		this.config = config;
		this.client = client;
		this.plugin = plugin;

		panelComponent.setWrap(true);
		panelComponent.setOrientation(ComponentOrientation.VERTICAL);

		setPosition(OverlayPosition.TOP_LEFT);
		setPriority(OverlayPriority.HIGH);
		setResizable(true);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		String message = "";
		Boolean shouldNotify = false;
		switch (plugin.getBossName()) {
			case "Sotetseg":
				message = config.customSoteMessage();
				shouldNotify = config.shouldNotifySote();
				break;
			case "Xarpus":
				message = config.customXarpMessage();
				shouldNotify = config.shouldNotifyXarp();
				break;
			case "Verzik":
				message = config.customVerzikMessage();
				shouldNotify = config.shouldNotifyVerz();
				break;
		}

		if (shouldNotify) {
			panelComponent.getChildren().clear();

			panelComponent.getChildren().add(LineComponent.builder()
					.left(message)
					.build());

			if (config.shouldFlash()) {
				if (client.getGameCycle() % 40 >= 20)
				{
					panelComponent.setBackgroundColor(config.flashColor1());
				} else
				{
					panelComponent.setBackgroundColor(config.flashColor2());
				}
			} else {
				panelComponent.setBackgroundColor(config.flashColor1());
			}

			return super.render(graphics);
		}

		return null;
	}
}
