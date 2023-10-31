package com.tobnotification;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Alpha;
import java.awt.Color;

@ConfigGroup("tobnotification")
public interface TobNotificationConfig extends Config
{
	@ConfigItem(
			keyName = "shouldFlash",
			name = "Flash the Reminder Box",
			description = "Makes the reminder box flash between the defined colors.",
			position = 1
	)
	default boolean shouldFlash() { return false; }

	@Alpha
	@ConfigItem(
			keyName = "flashColor1",
			name = "Flash Color #1",
			description = "The first color to flash between, also controls the non-flashing color.",
			position = 2
	)
	default Color flashColor1() { return new Color(255, 0, 0, 150); }

	@Alpha
	@ConfigItem(
			keyName = "flashColor2",
			name = "Flash Color #2",
			description = "The second color to flash between.",
			position = 3
	)
	default Color flashColor2() { return new Color(70, 61, 50, 150); }

	@ConfigItem(
			keyName = "shouldNotifySote",
			name = "Sotetseg Notification",
			description = "Turns on the custom notification for sotetseg",
			position = 4
	)
	default boolean shouldNotifySote() { return true; }

	@ConfigItem(
			keyName = "customSoteMessage",
			name = "Custom Sotetseg Ball Message",
			description = "Allows a custom message when the sotetseg ball is targeting you",
			position = 5
	)
	default String customSoteMessage() { return "THERE IS A DEATH BALL YOU IDIOT!!!!"; }

	@ConfigItem(
			keyName = "shouldNotifyXarp",
			name = "Xarpus Notification",
			description = "Turns on the custom notification for xarpus",
			position = 6
	)
	default boolean shouldNotifyXarp() { return true; }

	@ConfigItem(
			keyName = "customXarpMessage",
			name = "Custom Xarpus Screech Message",
			description = "Allows a custom message when Xarpus screeches",
			position = 7
	)
	default String customXarpMessage() { return "SCREEEEEEEEEEEEEEECH!!!!!!"; }

	@ConfigItem(
			keyName = "shouldNotifyVerz",
			name = "Verzik Notification",
			description = "Turns on the custom notification for verzik",
			position = 8
	)
	default boolean shouldNotifyVerz() { return true; }

	@ConfigItem(
			keyName = "customVerzikMessage",
			name = "Custom Verzik Green Ball Message",
			description = "Allows a custom message when the green ball is on you",
			position = 9
	)
	default String customVerzikMessage() { return "WEEWOOWEEWOOWEEWOO GREEN BALL!!!!!!!"; }
}
