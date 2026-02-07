package com.example.pingviewer;

import com.example.pingviewer.config.PingViewerConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PingViewer implements ModInitializer {

	public static PingViewerConfig CONFIG;

	@Override
	public void onInitialize() {
		CONFIG = PingViewerConfig.load();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
					CommandManager.literal("ping")
							.executes(context -> {
								ServerCommandSource source = context.getSource();
								int ping = source.getPlayer().networkHandler.getLatency();

								String raw = CONFIG.text;
								String[] parts = raw.split("%ping%");

// Decide ping color
								Formatting pingColor;
								if (ping <= 100) {
									pingColor = Formatting.GREEN;
								} else if (ping <= 200) {
									pingColor = Formatting.YELLOW;
								} else {
									pingColor = Formatting.RED;
								}

// Start building message
								MutableText message = Text.literal("");

// Before %ping%
								MutableText before = Text.literal(parts[0]);
								applyFormatting(before);
								message.append(before);

// The ping itself (forced color)
								message.append(
										Text.literal(ping + "ms").formatted(pingColor)
								);

// After %ping% (if any)
								if (parts.length > 1) {
									MutableText after = Text.literal(parts[1]);
									applyFormatting(after);
									message.append(after);
								}

								source.sendMessage(message);

								return 1;
							})
			);
		});
	}

	private static void applyFormatting(MutableText text) {
		Formatting color = Formatting.byName(CONFIG.color.toUpperCase());
		if (color != null) {
			text.formatted(color);
		}

		if (CONFIG.bold) text.formatted(Formatting.BOLD);
		if (CONFIG.italic) text.formatted(Formatting.ITALIC);
		if (CONFIG.underlined) text.formatted(Formatting.UNDERLINE);
	}
}
