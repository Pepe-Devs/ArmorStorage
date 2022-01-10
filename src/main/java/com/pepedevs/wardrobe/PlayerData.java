package com.pepedevs.wardrobe;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    private static final Wardrobe PLUGIN = Wardrobe.wardrobe();
    private static final Map<UUID, PlayerData> PLAYER_DATA = new ConcurrentHashMap<>();

    private final UUID playerUUID;
    private final Map<Integer, WardrobeSet> setCache = new ConcurrentHashMap<>();

    public static CompletableFuture<PlayerData> handle(UUID uuid) {
        PlayerData data = new PlayerData(uuid);
        PLAYER_DATA.put(uuid, data);
        return data.updateFromDatabase();
    }

    public static PlayerData data(UUID uuid) {
        return PLAYER_DATA.get(uuid);
    }

    protected PlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UUID uuid() {
        return playerUUID;
    }

    public Player player() {
        return Bukkit.getPlayer(playerUUID);
    }

    public Map<Integer, WardrobeSet> cache() {
        return setCache;
    }

    public CompletableFuture<PlayerData> updateFromDatabase() {
        this.setCache.clear();
        return CompletableFuture.supplyAsync(() -> {
            try {
                @Language("SQL")
                String queryPage1  = "SELECT * FROM `Wardrobe-Page-1` WHERE `UUID` = '" + playerUUID + "';";
                PLUGIN.database().query(queryPage1, result -> {
                    if (result.next()) {
                        for (int i = 1; i <= 9; i++) {
                            String encoded = result.getString("SLOT-" + i);
                            WardrobeSet set = WardrobeSet.fromString(encoded);
                            this.setCache.put(i, set);
                        }
                    }
                });
                @Language("SQL")
                String queryPage2 = "SELECT * FROM `Wardrobe-Page-2` WHERE `UUID` = '" + playerUUID + "';";
                PLUGIN.database().query(queryPage2, result -> {
                    if (result.next()) {
                        for (int i = 10; i <= 18; i++) {
                            String encoded = result.getString("SLOT-" + i);
                            WardrobeSet set = WardrobeSet.fromString(encoded);
                            this.setCache.put(i, set);
                        }
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return this;
        });
    }

    public CompletableFuture<PlayerData> saveToDatabase() {
        return CompletableFuture.supplyAsync(() -> {
            for (WardrobeSet value : this.setCache.values()) {
                value.invalidate();
            }
            try {
                @Language("SQL")
                String queryPage1 = "UPDATE `Wardrobe-Page-1` SET `SLOT-1` = '" + this.setCache.getOrDefault(1, WardrobeSet.NULL) + "', `SLOT-2` = '" + this.setCache.getOrDefault(2, WardrobeSet.NULL) + "', `SLOT-3` = '" + this.setCache.getOrDefault(3, WardrobeSet.NULL) + "', " +
                        "`SLOT-4` = '" + this.setCache.getOrDefault(4, WardrobeSet.NULL) + "', `SLOT-5` = '" + this.setCache.getOrDefault(5, WardrobeSet.NULL) + "', `SLOT-6` = '" + this.setCache.getOrDefault(6, WardrobeSet.NULL) + "', `SLOT-7` = '" + this.setCache.getOrDefault(7, WardrobeSet.NULL) +
                        "', `SLOT-8` = '" + this.setCache.getOrDefault(8, WardrobeSet.NULL) + "', `SLOT-9` = '" + this.setCache.getOrDefault(9, WardrobeSet.NULL) + "' WHERE `UUID` = '" + this.uuid() + "';";
                PLUGIN.database().query(queryPage1);
                @Language("SQL")
                String queryPage2 = "UPDATE `Wardrobe-Page-2` SET `SLOT-10` = '" + this.setCache.getOrDefault(10, WardrobeSet.NULL) + "', `SLOT-11` = '" + this.setCache.getOrDefault(11, WardrobeSet.NULL) + "', `SLOT-12` = '" + this.setCache.getOrDefault(12, WardrobeSet.NULL) + "', " +
                        "`SLOT-13` = '" + this.setCache.getOrDefault(13, WardrobeSet.NULL) + "', `SLOT-14` = '" + this.setCache.getOrDefault(14, WardrobeSet.NULL) + "', `SLOT-15` = '" + this.setCache.getOrDefault(15, WardrobeSet.NULL) + "', `SLOT-16` = '" + this.setCache.getOrDefault(16, WardrobeSet.NULL) +
                        "', `SLOT-17` = '" + this.setCache.getOrDefault(17, WardrobeSet.NULL) + "', `SLOT-18` = '" + this.setCache.getOrDefault(18, WardrobeSet.NULL) + "' WHERE `UUID` = '" + this.uuid() + "';";
                PLUGIN.database().query(queryPage2);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return this;
        });
    }

}
