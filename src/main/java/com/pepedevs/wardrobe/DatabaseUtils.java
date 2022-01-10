package com.pepedevs.wardrobe;

import com.pepedevs.wardrobe.gui.WardrobeGui;
import org.bukkit.entity.Player;

public class DatabaseUtils {

    public static final String NULL_SET = Utils.toBase64(WardrobeSet.NULL);

    public static boolean resetAllPlayerWardrobe(Player player) {
        DatabaseUtils.resetPagePlayerWardrobe(player, WardrobeGui.EnumPage.PAGE_1);
        DatabaseUtils.resetPagePlayerWardrobe(player, WardrobeGui.EnumPage.PAGE_2);
        return true;
    }

    public static boolean resetPagePlayerWardrobe(Player player, WardrobeGui.EnumPage page) {
        Wardrobe.wardrobe().database().updateAsync("DELETE FROM `Wardrobe-Page-" + page.value() + "` WHERE `UUID` = '" + player.getUniqueId() + "';");
        DatabaseUtils.insertNewData(player, page);
        return true;
    }

    public static boolean resetSlotPlayerWardrobe(Player player, String slot) {
        if (Integer.parseInt(slot) >= 1 && Integer.parseInt(slot) <= 9) {
            Wardrobe.wardrobe().database().updateAsync("UPDATE `Wardrobe-Page-1` SET `SLOT-" + slot + "` = '" + NULL_SET + "' WHERE UUID = '" + player.getUniqueId() + "';");
            return true;
        } else if (Integer.parseInt(slot) >= 10 && Integer.parseInt(slot) <= 18) {
            Wardrobe.wardrobe().database().updateAsync("UPDATE `Wardrobe-Page-2` SET `SLOT-" + slot + "` = '" + NULL_SET + "' WHERE UUID = '" + player.getUniqueId() + "';");
            return true;
        }
        return false;
    }

    public static void insertNewData(Player player, WardrobeGui.EnumPage page) {
        if (page == WardrobeGui.EnumPage.PAGE_1) {
            Wardrobe.wardrobe().database().updateAsync("INSERT IGNORE INTO `Wardrobe-Page-1` (`UUID`, `NAME`, `SLOT-1`, `SLOT-2`, `SLOT-3`, `SLOT-4`, `SLOT-5`, `SLOT-6`, `SLOT-7`, `SLOT-8`, `SLOT-9`) " +
                    "VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET +
                    "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "');");
        } else {
            Wardrobe.wardrobe().database().updateAsync("INSERT IGNORE INTO `Wardrobe-Page-2` (`UUID`, `NAME`, `SLOT-10`, `SLOT-11`, `SLOT-12`, `SLOT-13`, `SLOT-14`, " +
                    "`SLOT-15`, `SLOT-16`, `SLOT-17`, `SLOT-18`) VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', '" +
                    NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '"
                    + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "');");
        }
    }

}
