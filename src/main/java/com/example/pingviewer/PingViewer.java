package com.example.pingviewer;

import com.example.pingviewer.config.PingViewerConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class PingViewer implements ModInitializer {

	public static PingViewerConfig CONFIG;

	@Override
	public void onInitialize() {
		CONFIG = PingViewerConfig.load();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
					Commands.literal("ping")
							.executes(context -> {
								CommandSourceStack source = context.getSource();

								ServerPlayer player = source.getPlayerOrException();
								int ping = player.connection.latency();

								String msg = CONFIG.text.replace("%ping%", String.valueOf(ping));
								Component text = Component.literal(msg);

								text = applyPingColor(text, ping);

								if (CONFIG.bold) text = text.copy().withStyle(ChatFormatting.BOLD);
								if (CONFIG.italic) text = text.copy().withStyle(ChatFormatting.ITALIC);
								if (CONFIG.underlined) text = text.copy().withStyle(ChatFormatting.UNDERLINE);

								final var finalText = text;
								source.sendSuccess(() -> finalText, false);
								return 1;
							})
			);
		});
	}

	private Component applyPingColor(Component text, int ping) {
		if (ping <= 100) {
			return text.copy().withStyle(ChatFormatting.GREEN);
		} else if (ping <= 200) {
			return text.copy().withStyle(ChatFormatting.YELLOW);
		} else {
			return text.copy().withStyle(ChatFormatting.RED);
		}
	}
}