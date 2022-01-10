package com.pepedevs.wardrobe;

import com.pepedevs.corelib.adventure.MiniMessageUtils;
import com.pepedevs.corelib.utils.itemstack.ItemMetaBuilder;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.corelib.utils.xseries.XSound;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class Utils {

    public static ItemStack getButton(PluginConfig.Button button, XMaterial material) {
        return ItemMetaBuilder.of(material)
                .displayName(MiniMessageUtils.translate(Wardrobe.wardrobe().mini(), button.getName()))
                .lore(MiniMessageUtils.translate(Wardrobe.wardrobe().mini(), button.getLore()))
                .toItemStack();
    }

    public static ItemStack getSlotItem(PluginConfig.SlotItem slotItem, XMaterial material, Placeholder<?>... placeholders) {
        return getSlotItem(slotItem, material, PlaceholderResolver.placeholders(placeholders));
    }

    public static ItemStack getSlotItem(PluginConfig.SlotItem slotItem, XMaterial material, PlaceholderResolver resolver) {
        return ItemMetaBuilder.of(material)
                .displayName(MiniMessageUtils.translate(Wardrobe.wardrobe().mini(), slotItem.getName(), resolver))
                .lore(MiniMessageUtils.translate(Wardrobe.wardrobe().mini(), slotItem.getLore(), resolver))
                .toItemStack();
    }

    public static boolean canPlace(int slot, ItemStack stack) {
        if (slot < 9 && Utils.getArmorType(stack) == WardrobeSet.EnumArmor.HELMET) {
            return true;
        } else if (slot < 18 && Utils.getArmorType(stack) == WardrobeSet.EnumArmor.CHESTPLATE) {
            return true;
        } else if (slot < 27 && Utils.getArmorType(stack) == WardrobeSet.EnumArmor.LEGGINGS) {
            return true;
        } else if (slot < 36 && Utils.getArmorType(stack) == WardrobeSet.EnumArmor.BOOTS) {
            return true;
        }

        return false;
    }

    public static String toBase64(WardrobeSet set) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(set.getHelmet());
            dataOutput.writeObject(set.getChestplate());
            dataOutput.writeObject(set.getLeggings());
            dataOutput.writeObject(set.getBoots());
            dataOutput.writeInt(set.getState().ordinal());

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    public static WardrobeSet fromBase64(String data) {
        if (data == null)
            return WardrobeSet.NULL;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data))) {
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack helmet = (ItemStack) dataInput.readObject();
            ItemStack chestplate = (ItemStack) dataInput.readObject();
            ItemStack leggings = (ItemStack) dataInput.readObject();
            ItemStack boots = (ItemStack) dataInput.readObject();
            int i = dataInput.readInt();
            WardrobeSet.State state = WardrobeSet.State.LOCKED;

            if (i == 0)
                state = WardrobeSet.State.EQUIPPED;
            else if (i == 1)
                state = WardrobeSet.State.READY;
            else if (i == 2)
                state = WardrobeSet.State.EMPTY;

            dataInput.close();
            return new WardrobeSet(new ItemStack[]{helmet, chestplate, leggings, boots}, state);
        } catch (ClassNotFoundException | IOException e) {
            return WardrobeSet.NULL;
        }
    }

    public static boolean givePlayerEquippedArmor(Player player) {
        int armorCount = 0;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null)
                armorCount++;
        }
        int freeSpace = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i == null) {
                freeSpace++;
            }
            if (freeSpace >= armorCount) {
                // Give player that armor
                if (player.getInventory().getHelmet() != null)
                    player.getInventory().addItem(player.getInventory().getHelmet());
                if (player.getInventory().getChestplate() != null)
                    player.getInventory().addItem(player.getInventory().getChestplate());
                if (player.getInventory().getLeggings() != null)
                    player.getInventory().addItem(player.getInventory().getLeggings());
                if (player.getInventory().getBoots() != null)
                    player.getInventory().addItem(player.getInventory().getBoots());
                // Clear player armor slot
                Utils.clearPlayerArmor(player);
                return true;
            }
        }
        Wardrobe.wardrobe().adventure().player(player).sendMessage(MiniMessageUtils.translate(Wardrobe.wardrobe().config().getNoSpaceMessage()));
        XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
        return false;
    }

    public static void clearPlayerArmor(Player p) {
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
    }

    public static WardrobeSet.EnumArmor getArmorType(ItemStack item) {
        if (item == null)
            return WardrobeSet.EnumArmor.NONE;
        else if (item.getType().name().contains("HELMET") || item.getType().name().contains("PLAYER_HEAD")
                || item.getType().name().contains("CARVED_PUMPKIN")
                || item.getType().name().contains("SKULL_ITEM")
                || item.getType().name().contains("TURTLE_SHELL"))
            return WardrobeSet.EnumArmor.HELMET;
        else if (item.getType().name().contains("CHESTPLATE") || item.getType().name().contains("ELYTRA"))
            return WardrobeSet.EnumArmor.CHESTPLATE;
        else if (item.getType().name().contains("LEGGINGS"))
            return WardrobeSet.EnumArmor.LEGGINGS;
        else if (item.getType().name().contains("BOOTS"))
            return WardrobeSet.EnumArmor.BOOTS;
        return WardrobeSet.EnumArmor.NONE;
    }

    public static WardrobeSet.EnumArmor getArmorType(int slot) {
        if (slot < 9)
            return WardrobeSet.EnumArmor.HELMET;
        else if (slot < 18)
            return WardrobeSet.EnumArmor.CHESTPLATE;
        else if (slot < 27)
            return WardrobeSet.EnumArmor.LEGGINGS;
        else if (slot < 36)
            return WardrobeSet.EnumArmor.BOOTS;
        return WardrobeSet.EnumArmor.NONE;
    }

    public static Map.Entry<Integer, WardrobeSet> getFreeSlot(WardrobeSet.EnumArmor armor, Player player) {
        PlayerData data = PlayerData.data(player.getUniqueId());
        for (Map.Entry<Integer, WardrobeSet> entry : data.cache().entrySet()) {
            if (entry.getValue().get(armor) == null && entry.getValue().getState() != WardrobeSet.State.EQUIPPED) {
                return entry;
            }
        }

        return null;
    }

}
