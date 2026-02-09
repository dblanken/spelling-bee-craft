package com.spellingbeecraft;

import com.spellingbeecraft.config.DifficultyMode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;

public class SpellingBeeCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpellingBeeCraftMod.LOGGER.info("Spelling Bee Craft client initializing");
        registerCommands();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("spellingbee")
                    // No args — show status
                    .executes(context -> {
                        String status = SpellingBeeCraftMod.isEnabled() ? "Enabled" : "Disabled";
                        String difficulty = SpellingBeeCraftMod.getDifficulty().getDisplayName();
                        context.getSource().sendFeedback(
                                Component.literal("Spelling Bee Craft: " + status + " | Difficulty: " + difficulty)
                                        .withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                        );
                        return 1;
                    })
                    // /spellingbee on
                    .then(ClientCommandManager.literal("on")
                            .executes(context -> {
                                SpellingBeeCraftMod.setEnabled(true);
                                context.getSource().sendFeedback(
                                        Component.literal("Spelling Bee Craft: Enabled")
                                                .withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))
                                );
                                return 1;
                            })
                    )
                    // /spellingbee off
                    .then(ClientCommandManager.literal("off")
                            .executes(context -> {
                                SpellingBeeCraftMod.setEnabled(false);
                                context.getSource().sendFeedback(
                                        Component.literal("Spelling Bee Craft: Disabled")
                                                .withStyle(Style.EMPTY.withColor(ChatFormatting.RED))
                                );
                                return 1;
                            })
                    )
                    // /spellingbee difficulty <easy|medium|hard>
                    .then(ClientCommandManager.literal("difficulty")
                            .then(ClientCommandManager.literal("easy")
                                    .executes(context -> {
                                        SpellingBeeCraftMod.setDifficulty(DifficultyMode.EASY);
                                        context.getSource().sendFeedback(
                                                Component.literal("Spelling Bee Craft: Difficulty set to Easy")
                                                        .withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))
                                        );
                                        return 1;
                                    })
                            )
                            .then(ClientCommandManager.literal("medium")
                                    .executes(context -> {
                                        SpellingBeeCraftMod.setDifficulty(DifficultyMode.MEDIUM);
                                        context.getSource().sendFeedback(
                                                Component.literal("Spelling Bee Craft: Difficulty set to Medium")
                                                        .withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                                        );
                                        return 1;
                                    })
                            )
                            .then(ClientCommandManager.literal("hard")
                                    .executes(context -> {
                                        SpellingBeeCraftMod.setDifficulty(DifficultyMode.HARD);
                                        context.getSource().sendFeedback(
                                                Component.literal("Spelling Bee Craft: Difficulty set to Hard")
                                                        .withStyle(Style.EMPTY.withColor(ChatFormatting.RED))
                                        );
                                        return 1;
                                    })
                            )
                    )
                    // /spellingbee help
                    .then(ClientCommandManager.literal("help")
                            .executes(context -> {
                                context.getSource().sendFeedback(
                                        Component.literal("Spelling Bee Craft Commands:")
                                                .withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                                );
                                context.getSource().sendFeedback(
                                        Component.literal("  /spellingbee - Show current status")
                                );
                                context.getSource().sendFeedback(
                                        Component.literal("  /spellingbee on - Enable the mod")
                                );
                                context.getSource().sendFeedback(
                                        Component.literal("  /spellingbee off - Disable the mod")
                                );
                                context.getSource().sendFeedback(
                                        Component.literal("  /spellingbee difficulty <easy|medium|hard> - Set difficulty")
                                );
                                context.getSource().sendFeedback(
                                        Component.literal("  /spellingbee help - Show this help message")
                                );
                                return 1;
                            })
                    )
            );
        });
    }
}
