package com.pepedevs.wardrobe.commands;

import com.pepedevs.wardrobe.gui.WardrobeGui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BetterWardrobeCommand implements CommandExecutor {

    private final CommandHelper helper;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                args = new String[]{"open", "1"};
            }
            switch (args[0].toLowerCase()) {
                case "open": {
                    if (args.length < 2) {
                        helper.openWardrobe(player.getUniqueId(), WardrobeGui.EnumPage.PAGE_1);
                        return true;
                    }
                    WardrobeGui.EnumPage page;
                    if (args[1].equalsIgnoreCase("2") || args[1].equalsIgnoreCase("page2")) page = WardrobeGui.EnumPage.PAGE_2;
                    else page = WardrobeGui.EnumPage.PAGE_1;
                    helper.openWardrobe(player.getUniqueId(), page);
                    return true;
                }
            }
        }

        if (args.length < 1) {
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "reset": {
                if (args.length < 2) {
                    //ENTER A NAME TO RESET WARDROBE OF
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    //PLAYER OFFLINE
                    return false;
                }
                if (args.length < 3) {
                    helper.resetWardrobe(target, WardrobeGui.EnumPage.PAGE_1);
                    helper.resetWardrobe(target, WardrobeGui.EnumPage.PAGE_2);
                }
            }
        }
        return false;

    }
}
