package com.pepedevs.wardrobe.gui;

import com.pepedevs.corelib.adventure.AdventureUtils;
import com.pepedevs.corelib.adventure.MiniMessageUtils;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.itemstack.ItemMetaBuilder;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.corelib.utils.xseries.XSound;
import com.pepedevs.wardrobe.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class WardrobeGui {

    public static final String DEFAULT_TITLE = "Wardrobe";

    private final Wardrobe plugin;

    private final Player player;
    private final EnumPage page;
    private final String title;
    private final ClickAction[] clickActions;

    public static void openGui(Player player, EnumPage page) {
        WardrobeGui gui = new WardrobeGui(Wardrobe.wardrobe(), player, page);
        gui.open();
    }

    public WardrobeGui(Wardrobe plugin, Player player, EnumPage page) {
        this.plugin = plugin;
        this.player = player;
        this.page = page;
        this.title = StringUtils.defaultIfBlank(this.plugin.config().getTitle(), DEFAULT_TITLE);
        this.clickActions = new ClickAction[54];
        Arrays.fill(this.clickActions, ClickAction.VOID);
    }

    public void open() {
        this.setup(this.player, this.page).handle((inventory, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }

            return inventory;
        }).thenAccept(inventory -> this.plugin.getServer().getScheduler().runTask(this.plugin, () -> this.player.openInventory(inventory)));
    }

    private CompletableFuture<Inventory> setup(Player player, EnumPage page) {
        return CompletableFuture.supplyAsync(() -> {
            String title = AdventureUtils.toVanillaString(MiniMessageUtils.translate(Wardrobe.wardrobe().mini(), this.title + " (" + page.value() + "/2)"));
            Inventory inventory = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, 54, title)), 54, title);
            ItemStack background = ItemMetaBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE)
                    .displayName(Component.space())
                    .toItemStack();
            for (int i = 45; i <= 53; i++) {
                inventory.setItem(i, background);
            }

            this.createButtons(inventory, page);
            this.createLockedBackground(inventory, page);
            this.fillSlots(inventory, page, player);
            return inventory;
        });
    }

    private void createButtons(Inventory inventory, EnumPage page) {
        // Go Back button
        PluginConfig.ConfigurableButton go_back_Configurable_button = this.plugin.config().getButton(PluginConfig.ConfigurableButton.GO_BACK_BUTTON);
        if (go_back_Configurable_button.isEnable()) {
            ItemStack goBack = Utils.getButton(go_back_Configurable_button, XMaterial.ARROW);
            this.clickActions[go_back_Configurable_button.getSlot()] = ClickAction.GO_BACK;
            inventory.setItem(go_back_Configurable_button.getSlot(), goBack);
        }
        // Close button
        PluginConfig.ConfigurableButton close_Configurable_button = this.plugin.config().getButton(PluginConfig.ConfigurableButton.CLOSE_BUTTON);
        if (close_Configurable_button.isEnable()) {
            ItemStack close = Utils.getButton(close_Configurable_button, XMaterial.BARRIER);
            this.clickActions[close_Configurable_button.getSlot()] = ClickAction.CLOSE;
            inventory.setItem(close_Configurable_button.getSlot(), close);
        }

        if (page == EnumPage.PAGE_1) {
            // Next page button
            PluginConfig.ConfigurableButton next_page_Configurable_button = this.plugin.config().getButton(PluginConfig.ConfigurableButton.NEXT_PAGE_BUTTON);
            if (next_page_Configurable_button.isEnable()) {
                ItemStack nextPage = Utils.getButton(next_page_Configurable_button, XMaterial.ARROW);
                this.clickActions[next_page_Configurable_button.getSlot()] = ClickAction.NEXT_PAGE;
                inventory.setItem(next_page_Configurable_button.getSlot(), nextPage);
            }
        } else if (page == EnumPage.PAGE_2) {
            // Previous page button
            PluginConfig.ConfigurableButton previous_page_Configurable_button = this.plugin.config().getButton(PluginConfig.ConfigurableButton.PREVIOUS_PAGE_BUTTON);
            if (previous_page_Configurable_button.isEnable()) {
                ItemStack previousPage = Utils.getButton(previous_page_Configurable_button, XMaterial.ARROW);
                this.clickActions[previous_page_Configurable_button.getSlot()] = ClickAction.PREVIOUS_PAGE;
                inventory.setItem(previous_page_Configurable_button.getSlot(), previousPage);
            }
        }
    }

    private void createLockedBackground(Inventory inventory, EnumPage page) {
        int increase;
        if (page == EnumPage.PAGE_1)
            increase = 1;
        else
            increase = 10;

        for (int i = 0; i <= 44; i++) {
            XMaterial background = XMaterial.BLACK_STAINED_GLASS_PANE;
            int slot = (i % 9) + increase;
            PluginConfig.Slot s = this.plugin.config().getSlot(slot);
            Placeholder<String> slotPlaceholder = Placeholder.miniMessage("slot", String.valueOf(slot));
            Placeholder<String> permissionPlaceholder = Placeholder.miniMessage("permission_require_prefix", s.getPermission());
            ItemStack item;
            if (i < 9) {
                item = Utils.getSlotItem(this.plugin.config().getLockedSlot(PluginConfig.ConfigurableSlotItem.HELMET_SLOT), background, slotPlaceholder, permissionPlaceholder);
            } else if (i < 18) {
                item = Utils.getSlotItem(this.plugin.config().getLockedSlot(PluginConfig.ConfigurableSlotItem.CHESTPLATE_SLOT), background, slotPlaceholder, permissionPlaceholder);
            } else if (i < 27) {
                item = Utils.getSlotItem(this.plugin.config().getLockedSlot(PluginConfig.ConfigurableSlotItem.LEGGINGS_SLOT), background, slotPlaceholder, permissionPlaceholder);
            } else if (i < 36) {
                item = Utils.getSlotItem(this.plugin.config().getLockedSlot(PluginConfig.ConfigurableSlotItem.BOOTS_SLOT), background, slotPlaceholder, permissionPlaceholder);
            } else {
                item = Utils.getSlotItem(this.plugin.config().getLockedSlot(PluginConfig.ConfigurableSlotItem.LOCKED_BUTTON), XMaterial.RED_DYE, slotPlaceholder, permissionPlaceholder);
            }
            inventory.setItem(i, item);
        }
    }

    private void fillSlots(Inventory inventory, EnumPage page, Player player) {
        int increase;
        if (page == EnumPage.PAGE_1) {
            increase = 0;
        } else {
            increase = 9;
        }

        PlayerData data = PlayerData.data(player.getUniqueId());
        for (int i = 1; i <= 9; i++) {
            PluginConfig.Slot s = this.plugin.config().getSlot(i);
            if (player.hasPermission(s.getPermission())) {
                WardrobeSet set = data.cache().get(i + increase);
                int finalI = i - 1;
                ClickAction action = this.getClickAction(set, data, finalI + 36).get();
                ClickAction def = this.getDefaultClick(set, data, finalI + 36).get();
                this.setOrDefault(set.getHelmet(), action, () -> this.getBackground(finalI, page), def, inventory, finalI);
                this.setOrDefault(set.getChestplate(), action, () -> this.getBackground(finalI + 9, page), def, inventory, finalI + 9);
                this.setOrDefault(set.getLeggings(), action, () -> this.getBackground(finalI + 18, page), def, inventory, finalI + 18);
                this.setOrDefault(set.getBoots(), action, () -> this.getBackground(finalI + 27, page), def, inventory, finalI + 27);
                set.invalidate();
                this.setButton(finalI, finalI + 36, inventory, data, set, page);
            }
        }
    }

    private ItemStack getBackground(int slot, EnumPage page) {
        XMaterial material = XMaterial.BLACK_STAINED_GLASS_PANE;
        switch (slot % 9) {
            case 0:
                material = XMaterial.RED_STAINED_GLASS_PANE;
                break;
            case 1:
                material = XMaterial.ORANGE_STAINED_GLASS_PANE;
                break;
            case 2:
                material = XMaterial.YELLOW_STAINED_GLASS_PANE;
                break;
            case 3:
                material = XMaterial.LIME_STAINED_GLASS_PANE;
                break;
            case 4:
                material = XMaterial.GREEN_STAINED_GLASS_PANE;
                break;
            case 5:
                material = XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE;
                break;
            case 6:
                material = XMaterial.BLUE_STAINED_GLASS_PANE;
                break;
            case 7:
                material = XMaterial.MAGENTA_STAINED_GLASS_PANE;
                break;
            case 8:
                material = XMaterial.PURPLE_STAINED_GLASS_PANE;
                break;
        }

        int increase;
        if (page == EnumPage.PAGE_1)
            increase = 1;
        else
            increase = 10;

        Placeholder<String> placeholder = Placeholder.miniMessage("slot", String.valueOf(slot % 9 + increase));
        if (slot >= 0 && slot <= 8) {
            return Utils.getSlotItem(this.plugin.config().getAvailableSlot(PluginConfig.ConfigurableSlotItem.HELMET_SLOT), material, placeholder);
        } else if (slot >= 9 && slot <= 17) {
            return Utils.getSlotItem(this.plugin.config().getAvailableSlot(PluginConfig.ConfigurableSlotItem.CHESTPLATE_SLOT), material, placeholder);
        } else if (slot >= 18 && slot <= 26) {
            return Utils.getSlotItem(this.plugin.config().getAvailableSlot(PluginConfig.ConfigurableSlotItem.LEGGINGS_SLOT), material, placeholder);
        } else if (slot >= 27 && slot <= 35) {
            return Utils.getSlotItem(this.plugin.config().getAvailableSlot(PluginConfig.ConfigurableSlotItem.BOOTS_SLOT), material, placeholder);
        }

        return null;
    }

    private void setOrDefault(ItemStack item, ClickAction action, Supplier<ItemStack> defaultItem, ClickAction defaultAction, Inventory inventory, int slot) {
        if (item != null) {
            inventory.setItem(slot, item);
            this.clickActions[slot] = action;
        } else {
            inventory.setItem(slot, defaultItem.get());
            this.clickActions[slot] = defaultAction;
        }
    }

    public void setButton(int slot, int buttonSlot, Inventory inv, PlayerData data, WardrobeSet set, EnumPage page) {
        int increase;
        if (page == EnumPage.PAGE_1)
            increase = 1;
        else
            increase = 10;

        Placeholder<String> placeholder = Placeholder.miniMessage("slot", String.valueOf(slot % 9 + increase));
        if (set.getState() == WardrobeSet.State.READY) {
            inv.setItem(buttonSlot, Utils.getSlotItem(this.plugin.config().getAvailableSlot(PluginConfig.ConfigurableSlotItem.READY_BUTTON), XMaterial.PINK_DYE, placeholder));
            ClickAction action = new ClickAction() {
                @Override
                public boolean handleClick(Player player, Inventory inventory, ItemStack item, int slot) {
                    boolean b = false;
                    for (Map.Entry<Integer, WardrobeSet> sets : data.cache().entrySet()) {
                        if (sets.getValue().getState() == WardrobeSet.State.EQUIPPED) {
                            b = true;
                            sets.getValue().setState(WardrobeSet.State.READY);
                            if (sets.getKey() < 10 && page == EnumPage.PAGE_1) {
                                setButton(sets.getKey(), sets.getKey() + 36 - 1, inventory, data, sets.getValue(), page);
                            } else if (sets.getKey() <= 18 && page == EnumPage.PAGE_2) {
                                setButton(sets.getKey(), sets.getKey() + 36 - 10, inventory, data, sets.getValue(), page);
                            }
                            break;
                        }
                    }

                    if (!b) {
                        if (Utils.givePlayerEquippedArmor(player))
                            return true;
                    }

                    set.apply(player);
                    set.setState(WardrobeSet.State.EQUIPPED);
                    inventory.setItem(buttonSlot, Utils.getSlotItem(plugin.config().getAvailableSlot(PluginConfig.ConfigurableSlotItem.EQUIPPED_BUTTON), XMaterial.LIME_DYE, placeholder));
                    clickActions[buttonSlot] = ClickAction.VOID;

                    player.updateInventory();

                    return true;
                }
            };
            this.clickActions[buttonSlot] = action;
        } else if (set.getState() == WardrobeSet.State.EQUIPPED) {
            inv.setItem(buttonSlot, Utils.getSlotItem(this.plugin.config().getAvailableSlot(PluginConfig.ConfigurableSlotItem.EQUIPPED_BUTTON), XMaterial.LIME_DYE, placeholder));
            this.clickActions[buttonSlot] = ClickAction.VOID;
        } else if (set.getState() == WardrobeSet.State.EMPTY) {
            inv.setItem(buttonSlot, Utils.getSlotItem(this.plugin.config().getAvailableSlot(PluginConfig.ConfigurableSlotItem.EMPTY_SLOT_BUTTON), XMaterial.GRAY_DYE, placeholder));
            this.clickActions[buttonSlot] = ClickAction.VOID;
        }
    }

    private Supplier<ClickAction> getClickAction(WardrobeSet set, PlayerData data, int buttonSlot) {
        return () -> new ClickAction() {
            @Override
            public boolean handleClick(Player player, Inventory inventory, ItemStack item, int slot) {
                if (set.getState() == WardrobeSet.State.EQUIPPED) {
                    plugin.adventure().player(player).sendMessage(MiniMessageUtils.translate(plugin.mini(), plugin.config().getModifyArmorDeniedMessage()));
                    XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                    return true;
                }

                player.setItemOnCursor(item);
                inventory.setItem(slot, getBackground(slot, page));
                clickActions[slot] = getDefaultClick(set, data, buttonSlot).get();
                set.set(Utils.getArmorType(slot), null);

                if (set.getState() == WardrobeSet.State.READY && !set.hasAny()) {
                    set.setState(WardrobeSet.State.EMPTY);
                    setButton(slot, buttonSlot, inventory, data, set, page);
                }
                return true;
            }

            @Override
            public boolean handleShiftClick(Player player, Inventory inventory, ItemStack item, int slot) {
                if (set.getState() == WardrobeSet.State.EQUIPPED) {
                    plugin.adventure().player(player).sendMessage(MiniMessageUtils.translate(plugin.mini(), plugin.config().getModifyArmorDeniedMessage()));
                    XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                    return true;
                }

                inventory.setItem(slot, getBackground(slot, page));
                clickActions[slot] = getDefaultClick(set, data, buttonSlot).get();
                set.set(Utils.getArmorType(slot), null);

                if (set.getState() == WardrobeSet.State.READY && !set.hasAny()) {
                    set.setState(WardrobeSet.State.EMPTY);
                    setButton(slot, buttonSlot, inventory, data, set, page);
                }

                return false;
            }
        };
    }

    private Supplier<ClickAction> getDefaultClick(WardrobeSet set, PlayerData data, int buttonSlot) {
        return () -> new ClickAction() {
            @Override
            public boolean handleClick(Player player, Inventory inventory, ItemStack item, int slot) {
                if (set.getState() == WardrobeSet.State.EQUIPPED) {
                    plugin.adventure().player(player).sendMessage(MiniMessageUtils.translate(plugin.mini(), plugin.config().getModifyArmorDeniedMessage()));
                    XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                    return true;
                }

                if (Utils.canPlace(slot, player.getItemOnCursor())) {
                    inventory.setItem(slot, player.getItemOnCursor());
                    player.setItemOnCursor(null);
                    clickActions[slot] = getClickAction(set, data, buttonSlot).get();
                    set.set(Utils.getArmorType(slot), item);
                    if (set.getState() != WardrobeSet.State.EMPTY) {
                        set.setState(WardrobeSet.State.READY);
                        setButton(slot, buttonSlot, inventory, data, set, page);
                    }
                }
                return true;
            }
        };
    }

    public Player player() {
        return player;
    }

    public EnumPage page() {
        return page;
    }

    public String title() {
        return title;
    }

    public ClickAction[] clickActions() {
        return clickActions;
    }

    public enum EnumPage {
        PAGE_1((byte) 1),
        PAGE_2((byte) 2),
        ;

        private final byte page;

        EnumPage(byte page) {
            this.page = page;
        }

        public byte value() {
            return page;
        }
    }

}
