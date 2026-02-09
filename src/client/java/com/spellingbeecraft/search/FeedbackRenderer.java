package com.spellingbeecraft.search;

import com.spellingbeecraft.config.DifficultyMode;
import com.spellingbeecraft.config.ModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;

public class FeedbackRenderer {

    private static final int COLOR_GRAY = 0xFF888888;
    private static final int COLOR_RED = 0xFFFF4444;
    private static final int COLOR_GREEN = 0xFF44FF44;
    private static final int COLOR_YELLOW = 0xFFFFAA00;

    /**
     * @param panelBottom the Y coordinate of the bottom edge of the inventory panel
     */
    public static void render(GuiGraphics guiGraphics, EditBox searchBox, SearchResult result,
                               ModConfig config, DifficultyMode difficulty, int panelBottom) {
        if (searchBox == null) return;

        net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;
        int x = searchBox.getX();
        int y = searchBox.getY();
        int width = searchBox.getWidth();
        int height = searchBox.getHeight();

        // Draw status message below the inventory panel
        if (config.isShowStatusMessages()) {
            String message = switch (result.getStatus()) {
                case EMPTY_QUERY -> "Type complete words to search";
                case NO_MATCHES -> "No items match \"" + result.getQuery() + "\"";
                case FUZZY_MATCHES -> "Almost! Try spelling the complete word";
                case HAS_MATCHES -> "Found " + result.getMatchCount() + " items";
            };

            int textColor = switch (result.getStatus()) {
                case EMPTY_QUERY -> COLOR_GRAY;
                case NO_MATCHES -> COLOR_RED;
                case FUZZY_MATCHES -> COLOR_YELLOW;
                case HAS_MATCHES -> COLOR_GREEN;
            };

            int msgWidth = font.width(message);
            int msgX = x + (width - msgWidth) / 2;

            // Offset past the bottom tabs (28px) before drawing text
            guiGraphics.drawString(font, message, msgX, panelBottom + 32, textColor, true);
        }

        // Draw difficulty indicator below status message
        String diffLabel = "[" + difficulty.getDisplayName() + "]";
        int diffColor = switch (difficulty) {
            case EASY -> COLOR_GREEN;
            case MEDIUM -> 0xFFFFFF44;
            case HARD -> COLOR_RED;
        };

        int diffWidth = font.width(diffLabel);
        int diffX = x + (width - diffWidth) / 2;

        guiGraphics.drawString(font, diffLabel, diffX, panelBottom + 44, diffColor, true);
    }

}
