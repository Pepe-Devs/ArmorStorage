package com.pepedevs.wardrobe;

import com.pepedevs.wardrobe.gui.WardrobeGui;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Language;

public class DatabaseUtils {

    public static final String NULL_SET = Utils.toBase64(WardrobeSet.NULL);
    private static final Wardrobe plugin = Wardrobe.wardrobe();

    public static boolean resetAllPlayerWardrobe(Player player) {
        DatabaseUtils.resetPagePlayerWardrobe(player, WardrobeGui.EnumPage.PAGE_1);
        DatabaseUtils.resetPagePlayerWardrobe(player, WardrobeGui.EnumPage.PAGE_2);
        return true;
    }

    public static boolean resetPagePlayerWardrobe(Player player, WardrobeGui.EnumPage page) {
        @Language("SQL")
        String resetQuery = "DELETE FROM `Wardrobe-Page-" + page.value() + "` WHERE `UUID` = '" + player.getUniqueId() + "';";
        plugin.database().updateAsync(resetQuery);
        DatabaseUtils.insertNewData(player, page);
        return true;
    }

    public static boolean resetSlotPlayerWardrobe(Player player, String slot) {
        if (Integer.parseInt(slot) >= 1 && Integer.parseInt(slot) <= 9) {
            @Language("SQL")
            String resetSlotQuery = "UPDATE `Wardrobe-Page-1` SET `SLOT-" + slot + "` = '" + NULL_SET + "' WHERE UUID = '" + player.getUniqueId() + "';";
            plugin.database().updateAsync(resetSlotQuery);
            return true;
        } else if (Integer.parseInt(slot) >= 10 && Integer.parseInt(slot) <= 18) {
            @Language("SQL")
            String resetSlotQuery = "UPDATE `Wardrobe-Page-2` SET `SLOT-" + slot + "` = '" + NULL_SET + "' WHERE UUID = '" + player.getUniqueId() + "';";
            plugin.database().updateAsync(resetSlotQuery);
            return true;
        }
        return false;
    }

    public static void insertNewData(Player player, WardrobeGui.EnumPage page) {
        if (page == WardrobeGui.EnumPage.PAGE_1) {
            @Language("SQL")
            String dataQuery = "INSERT IGNORE INTO `Wardrobe-Page-1` (`UUID`, `NAME`, `SLOT-1`, `SLOT-2`, `SLOT-3`, `SLOT-4`, `SLOT-5`, `SLOT-6`, `SLOT-7`, `SLOT-8`, `SLOT-9`) " +
                    "VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET +
                    "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "');";
            plugin.database().updateAsync(dataQuery);
        } else {
            @Language("SQL")
            String dataQuery = "INSERT IGNORE INTO `Wardrobe-Page-2` (`UUID`, `NAME`, `SLOT-10`, `SLOT-11`, `SLOT-12`, `SLOT-13`, `SLOT-14`, " +
                    "`SLOT-15`, `SLOT-16`, `SLOT-17`, `SLOT-18`) VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', '" +
                    NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "', '"
                    + NULL_SET + "', '" + NULL_SET + "', '" + NULL_SET + "');";
            plugin.database().updateAsync(dataQuery);
        }
    }

}
