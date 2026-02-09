package com.spellingbeecraft.mixin.client;

import com.spellingbeecraft.SpellingBeeCraftMod;
import com.spellingbeecraft.config.ModConfig;
import com.spellingbeecraft.config.DifficultyMode;
import com.spellingbeecraft.search.FeedbackRenderer;
import com.spellingbeecraft.search.SearchResult;
import com.spellingbeecraft.search.WordMatcher;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    private CreativeInventoryScreenMixin() {
        super(null, null, null);
    }

    @Shadow
    private EditBox searchBox;

    @Shadow
    private static CreativeModeTab selectedTab;

    @Unique
    private SearchResult lastSearchResult = SearchResult.emptyQuery();

    @Unique
    private boolean enterPressed = false;

    @Unique
    private String lastFilteredQuery = "";

    @Inject(method = "refreshSearchResults", at = @At("HEAD"), cancellable = true)
    private void onRefreshSearchResults(CallbackInfo ci) {
        if (!SpellingBeeCraftMod.isEnabled()) {
            lastSearchResult = SearchResult.emptyQuery();
            return;
        }

        if (selectedTab == null || selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
            return;
        }

        String query = searchBox.getValue();

        // In ON_ENTER mode, only filter when Enter was pressed
        ModConfig config = SpellingBeeCraftMod.getConfigManager().getConfig();
        if (config.getSearchMode() == ModConfig.SearchMode.ON_ENTER) {
            if (!enterPressed) {
                // Still show the last filtered results
                if (!lastFilteredQuery.isEmpty()) {
                    query = lastFilteredQuery;
                } else {
                    return; // Let vanilla handle it until Enter is pressed
                }
            } else {
                enterPressed = false;
                lastFilteredQuery = query;
            }
        }

        ci.cancel();

        // Get the creative menu
        CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) (Object) this;
        CreativeModeInventoryScreen.ItemPickerMenu menu = screen.getMenu();

        // Get all search tab items
        Collection<ItemStack> allItems = CreativeModeTabs.searchTab().getSearchTabDisplayItems();

        if (query == null || query.isBlank()) {
            // Show all items
            menu.items.clear();
            menu.items.addAll(allItems);
            lastSearchResult = SearchResult.emptyQuery();
        } else {
            // Filter using our WordMatcher
            List<ItemStack> itemList = new ArrayList<>(allItems);
            DifficultyMode difficulty = SpellingBeeCraftMod.getDifficulty();
            List<ItemStack> filtered = SpellingBeeCraftMod.getItemFilter().filterItems(
                    itemList,
                    stack -> stack.getHoverName().getString(),
                    query,
                    difficulty
            );

            menu.items.clear();
            menu.items.addAll(filtered);

            if (filtered.isEmpty()) {
                lastSearchResult = SearchResult.noMatches(query);
            } else {
                // Check if any match was fuzzy (Easy mode only)
                boolean anyFuzzy = false;
                if (difficulty == DifficultyMode.EASY) {
                    WordMatcher matcher = new WordMatcher();
                    String finalQuery = query;
                    anyFuzzy = filtered.stream().anyMatch(stack ->
                            matcher.matchType(finalQuery, stack.getHoverName().getString(), difficulty)
                                    == WordMatcher.MatchType.FUZZY
                    );
                }
                if (anyFuzzy) {
                    lastSearchResult = SearchResult.fuzzyMatches(filtered.size(), query);
                } else {
                    lastSearchResult = SearchResult.hasMatches(filtered.size(), query);
                }
            }
        }

        // Reset scroll to top
        menu.scrollTo(0.0f);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        // 257 = GLFW_KEY_ENTER, 335 = GLFW_KEY_KP_ENTER
        if (SpellingBeeCraftMod.isEnabled() && (keyCode == 257 || keyCode == 335)) {
            ModConfig config = SpellingBeeCraftMod.getConfigManager().getConfig();
            if (config.getSearchMode() == ModConfig.SearchMode.ON_ENTER) {
                enterPressed = true;
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!SpellingBeeCraftMod.isEnabled()) {
            return;
        }

        if (selectedTab == null || selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
            return;
        }

        ModConfig config = SpellingBeeCraftMod.getConfigManager().getConfig();
        int panelBottom = this.topPos + this.imageHeight;
        FeedbackRenderer.render(guiGraphics, searchBox, lastSearchResult, config,
                SpellingBeeCraftMod.getDifficulty(), panelBottom);
    }
}
