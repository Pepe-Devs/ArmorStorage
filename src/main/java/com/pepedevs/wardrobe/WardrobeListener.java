package com.pepedevs.wardrobe;

import com.pepedevs.wardrobe.gui.MenuHolder;
import com.pepedevs.wardrobe.gui.WardrobeGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Map;

public class WardrobeListener implements Listener {

    @EventHandler
    public void handleGuiClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || !(event.getWhoClicked() instanceof Player))
            return;

        if (!(event.getView().getTopInventory().getHolder() instanceof MenuHolder))
            return;

        MenuHolder menuHolder = (MenuHolder) event.getView().getTopInventory().getHolder();
        WardrobeGui gui = menuHolder.getGui();

        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            if (event.getClick() == ClickType.SHIFT_LEFT) {
                WardrobeSet.EnumArmor armor = Utils.getArmorType(event.getCurrentItem());
                if (armor != null) {
                    Map.Entry<Integer, WardrobeSet> free = Utils.getFreeSlot(armor, (Player) event.getWhoClicked());
                    if (free != null && free.getKey() <= 9 * gui.page().value() && free.getKey() >= 9 * (gui.page().value() - 1)) {
                        free.getValue().set(armor, event.getCurrentItem());
                        event.getInventory().setItem(free.getKey(), event.getCurrentItem());
                        if (free.getValue().invalidate()) {
                            int button = (free.getKey() % 9) + 36;
                            gui.setButton(free.getKey(), button, event.getInventory(), PlayerData.data(event.getWhoClicked().getUniqueId()), free.getValue(), gui.page());
                        }
                        ((Player) event.getWhoClicked()).updateInventory();
                    }
                }
            }
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        boolean b = true;
        if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
            b = gui.clickActions()[slot].handleClick(player, event.getInventory(), event.getCurrentItem(), slot);
        } else if (event.getClick() == ClickType.SHIFT_LEFT) {
            b = gui.clickActions()[slot].handleShiftClick(player, event.getInventory(), event.getCurrentItem(), slot);
        }
        event.setCancelled(b);
    }

}
