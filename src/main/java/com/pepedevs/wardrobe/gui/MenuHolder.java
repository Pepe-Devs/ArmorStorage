package com.pepedevs.wardrobe.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class MenuHolder implements InventoryHolder {

    private final WardrobeGui gui;
    private final Inventory inventory;

    public MenuHolder(WardrobeGui gui, Inventory inventory) {
        this.gui = gui;
        this.inventory = inventory;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public WardrobeGui getGui() {
        return this.gui;
    }

}
