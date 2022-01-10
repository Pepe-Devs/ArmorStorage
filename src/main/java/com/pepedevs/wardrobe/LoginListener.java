package com.pepedevs.wardrobe;

import com.pepedevs.wardrobe.gui.WardrobeGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class LoginListener implements Listener {

    private final Wardrobe plugin;

    public LoginListener(Wardrobe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DatabaseUtils.insertNewData(player, WardrobeGui.EnumPage.PAGE_1);
        DatabaseUtils.insertNewData(player, WardrobeGui.EnumPage.PAGE_2);
        PlayerData.handle(player.getUniqueId()).thenAccept(data -> {
            for (Map.Entry<Integer, WardrobeSet> entry : data.cache().entrySet()) {
                if (entry.getValue().getState() == WardrobeSet.State.EQUIPPED) {
                    entry.getValue().apply(player);
                }
            }
        });
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        PlayerData.data(event.getPlayer().getUniqueId()).saveToDatabase();
    }

}
