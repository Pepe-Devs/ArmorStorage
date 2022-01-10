package com.pepedevs.wardrobe.gui;

import com.pepedevs.wardrobe.PluginConfig;
import com.pepedevs.wardrobe.Wardrobe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface ClickAction {

    ClickAction VOID = new ClickAction(){};
    ClickAction GO_BACK = new ClickAction() {
        @Override
        public boolean handleClick(Player player, Inventory inventory, ItemStack item, int slot) {
            PluginConfig.Button button = Wardrobe.wardrobe().config().getButton(PluginConfig.Button.GO_BACK_BUTTON);
            player.performCommand(button.getCommand());
            return true;
        }
    };
    ClickAction PREVIOUS_PAGE = new ClickAction() {
        @Override
        public boolean handleClick(Player player, Inventory inventory, ItemStack item, int slot) {
            player.closeInventory();
            WardrobeGui.openGui(player, WardrobeGui.EnumPage.PAGE_1);
            return true;
        }
    };
    ClickAction NEXT_PAGE = new ClickAction() {
        @Override
        public boolean handleClick(Player player, Inventory inventory, ItemStack item, int slot) {
            player.closeInventory();
            WardrobeGui.openGui(player, WardrobeGui.EnumPage.PAGE_2);
            return true;
        }
    };
    ClickAction CLOSE = new ClickAction() {
        @Override
        public boolean handleClick(Player player, Inventory inventory, ItemStack item, int slot) {
            player.closeInventory();
            return true;
        }
    };

    default boolean handleClick(Player player, Inventory inventory, ItemStack item, int slot) {
        return true;
    }

    default boolean handleShiftClick(Player player, Inventory inventory, ItemStack item, int slot) {
        return true;
    }

}
