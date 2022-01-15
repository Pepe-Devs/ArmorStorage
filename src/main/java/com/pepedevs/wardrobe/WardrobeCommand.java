package com.pepedevs.wardrobe;

import com.pepedevs.corelib.adventure.MiniMessageUtils;
import com.pepedevs.corelib.utils.xseries.XSound;
import com.pepedevs.wardrobe.gui.WardrobeGui;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WardrobeCommand implements CommandExecutor, TabCompleter {

    private final Wardrobe plugin;
    private final List<String> arguments = new ArrayList<>();

    public WardrobeCommand(Wardrobe plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Audience audience = this.plugin.adventure().sender(sender);
            if (args.length == 0) {
                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Please choose a work to do!", NamedTextColor.RED)));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                        this.plugin.loadConfig();
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Success reloaded config!")));
                    });
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        WardrobeGui.openGui(target, WardrobeGui.EnumPage.PAGE_1);
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                    }
                }
                return true;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("open")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        WardrobeGui.openGui(target, WardrobeGui.EnumPage.PAGE_1);
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                    }
                    return true;
                    // Suggestion command to /wardrobe reset command
                } else if (args[0].equalsIgnoreCase("reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Please choose a work to do!", NamedTextColor.RED)));
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                    }
                    return true;
                    // Unknown command
                } else {
                    audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        // Suggestion command to /wardrobe reset command
                        if (args[2].equalsIgnoreCase("page") || args[2].equalsIgnoreCase("slot")) {
                            audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Please choose a number!", NamedTextColor.RED)));
                            return true;
                            // Reset all of player Wardrobe
                        } else if (args[2].equalsIgnoreCase("all")) {
                            if (DatabaseUtils.resetAllPlayerWardrobe(target)) {
                                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN)
                                        .append(Component.text("Success reset ", NamedTextColor.GREEN))
                                        .append(Component.text(args[2], NamedTextColor.GOLD))
                                        .append(Component.text(" of ", NamedTextColor.GREEN))
                                        .append(Component.text(args[1], NamedTextColor.GOLD))
                                        .append(Component.text("'s", NamedTextColor.GREEN))
                                        .append(Component.text(" Wardrobe!", NamedTextColor.GREEN)));
                            } else {
                                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Something wrong when execute this command!", NamedTextColor.RED)));
                            }
                            return true;
                        } else {
                            audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                            return true;
                        }
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                        return true;
                    }
                } else {
                    audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                    return true;
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        // Reset page of player Wardrobe
                        if (args[2].equalsIgnoreCase("page")) {
                            if (args[3].equalsIgnoreCase("1") || args[3].equalsIgnoreCase("2")) {
                                if (DatabaseUtils.resetPagePlayerWardrobe(target, args[3].equalsIgnoreCase("1") ? WardrobeGui.EnumPage.PAGE_1 : WardrobeGui.EnumPage.PAGE_2)) {
                                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN)
                                            .append(Component.text("Success reset ", NamedTextColor.GREEN))
                                            .append(Component.text(args[2], NamedTextColor.GOLD))
                                            .append(Component.text(" ", NamedTextColor.GREEN))
                                            .append(Component.text(args[3], NamedTextColor.GOLD))
                                            .append(Component.text(" of ", NamedTextColor.GREEN))
                                            .append(Component.text(args[1], NamedTextColor.GOLD))
                                            .append(Component.text("'s", NamedTextColor.GREEN))
                                            .append(Component.text(" Wardrobe!", NamedTextColor.GREEN)));
                                } else {
                                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Something wrong when execute this command!", NamedTextColor.RED)));
                                }
                            } else {
                                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Unknown page!", NamedTextColor.RED)));
                            }
                            return true;
                            // Reset slot of player Wardrobe
                        } else if (args[2].equalsIgnoreCase("slot")) {
                            String number = args[3];
                            if (Integer.parseInt(number) >= 1 && Integer.parseInt(number) <= 18) {
                                if (DatabaseUtils.resetSlotPlayerWardrobe(target, number)) {
                                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN)
                                            .append(Component.text("Success reset ", NamedTextColor.GREEN))
                                            .append(Component.text(args[2], NamedTextColor.GOLD))
                                            .append(Component.text(" ", NamedTextColor.GREEN))
                                            .append(Component.text(args[3], NamedTextColor.GOLD))
                                            .append(Component.text(" of ", NamedTextColor.GREEN))
                                            .append(Component.text(args[1], NamedTextColor.GOLD))
                                            .append(Component.text("'s", NamedTextColor.GREEN))
                                            .append(Component.text(" Wardrobe!", NamedTextColor.GREEN)));
                                } else {
                                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Something wrong when execute this command!", NamedTextColor.RED)));
                                }
                            } else {
                                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Unknown slot!", NamedTextColor.RED)));
                            }
                            return true;
                        } else {
                            audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                            return true;
                        }
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                        return true;
                    }
                } else {
                    audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                    return true;
                }
            }
            return false;
        }

        // Open command
        Player player = (Player) sender;
        Audience audience = this.plugin.adventure().player(player);
        if (args.length == 0) {
            WardrobeGui.openGui(player, WardrobeGui.EnumPage.PAGE_1);
            return true;
        } else if (player.hasPermission(this.plugin.config().getAdminPermission())) {
            if (args.length == 1) {
                // Reload command
                if (args[0].equalsIgnoreCase("reload")) {
                    this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                        this.plugin.loadConfig();
                        player.sendMessage(ChatColor.GREEN + "[Wardrobe] Success reloaded config!");
                        XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player.getLocation(), 2F, 6F);
                    });
                    return true;
                    // Open command
                } else if (args[0].equalsIgnoreCase("open")) {
                    WardrobeGui.openGui(player, WardrobeGui.EnumPage.PAGE_1);
                    return true;
                    // Suggestion command to /wardrobe reset command
                } else if (args[0].equalsIgnoreCase("reset")) {
                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Please enter a player name!", NamedTextColor.RED)));
                    XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                    return true;
                    // Unknown command
                } else {
                    audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                    return true;
                }
            } else if (args.length == 2) {
                // Open for player
                if (args[0].equalsIgnoreCase("open")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        WardrobeGui.openGui(target, WardrobeGui.EnumPage.PAGE_1);
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                        XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                    }
                    return true;
                    // Suggestion command to /wardrobe reset command
                } else if (args[0].equalsIgnoreCase("reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Please choose a work to do!", NamedTextColor.RED)));
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                        XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                    }
                    return true;
                    // Unknown command
                } else {
                    audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        // Suggestion command to /wardrobe reset command
                        if (args[2].equalsIgnoreCase("page") || args[2].equalsIgnoreCase("slot")) {
                            audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Please choose a number!", NamedTextColor.RED)));
                            XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                            return true;
                            // Reset all of player Wardrobe
                        } else if (args[2].equalsIgnoreCase("all")) {
                            if (DatabaseUtils.resetAllPlayerWardrobe(target)) {
                                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN)
                                        .append(Component.text("Success reset ", NamedTextColor.GREEN))
                                        .append(Component.text(args[2], NamedTextColor.GOLD))
                                        .append(Component.text(" of ", NamedTextColor.GREEN))
                                        .append(Component.text(args[1], NamedTextColor.GOLD))
                                        .append(Component.text("'s", NamedTextColor.GREEN))
                                        .append(Component.text(" Wardrobe!", NamedTextColor.GREEN)));
                                XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player.getLocation(), 2F, 6F);
                            } else {
                                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Something wrong when execute this command!", NamedTextColor.RED)));
                            }
                            return true;
                        } else {
                            audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                            return true;
                        }
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                        XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                        return true;
                    }
                } else {
                    audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                    return true;
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        // Reset page of player Wardrobe
                        if (args[2].equalsIgnoreCase("page")) {
                            if (args[3].equalsIgnoreCase("1") || args[3].equalsIgnoreCase("2")) {
                                if (DatabaseUtils.resetPagePlayerWardrobe(target, args[3].equalsIgnoreCase("1") ? WardrobeGui.EnumPage.PAGE_1 : WardrobeGui.EnumPage.PAGE_2)) {
                                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN)
                                            .append(Component.text("Success reset ", NamedTextColor.GREEN))
                                            .append(Component.text(args[2], NamedTextColor.GOLD))
                                            .append(Component.text(" ", NamedTextColor.GREEN))
                                            .append(Component.text(args[3], NamedTextColor.GOLD))
                                            .append(Component.text(" of ", NamedTextColor.GREEN))
                                            .append(Component.text(args[1], NamedTextColor.GOLD))
                                            .append(Component.text("'s", NamedTextColor.GREEN))
                                            .append(Component.text(" Wardrobe!", NamedTextColor.GREEN)));
                                    XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player.getLocation(), 2F, 6F);
                                } else {
                                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Something wrong when execute this command!", NamedTextColor.RED)));
                                }
                            } else {
                                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Unknown page!", NamedTextColor.RED)));
                                XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                            }
                            return true;
                            // Reset slot of player Wardrobe
                        } else if (args[2].equalsIgnoreCase("slot")) {
                            String Number = args[3];
                            if (Integer.parseInt(Number) >= 1 && Integer.parseInt(Number) <= 18) {
                                if (DatabaseUtils.resetSlotPlayerWardrobe(target, Number)) {
                                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN)
                                            .append(Component.text("Success reset ", NamedTextColor.GREEN))
                                            .append(Component.text(args[2], NamedTextColor.GOLD))
                                            .append(Component.text(" ", NamedTextColor.GREEN))
                                            .append(Component.text(args[3], NamedTextColor.GOLD))
                                            .append(Component.text(" of ", NamedTextColor.GREEN))
                                            .append(Component.text(args[1], NamedTextColor.GOLD))
                                            .append(Component.text("'s", NamedTextColor.GREEN))
                                            .append(Component.text(" Wardrobe!", NamedTextColor.GREEN)));
                                    XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player.getLocation(), 2F, 6F);
                                    return true;
                                } else {
                                    audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Something wrong when execute this command!", NamedTextColor.RED)));
                                }
                            } else {
                                audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("Unknown slot!", NamedTextColor.RED)));
                                XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                                return true;
                            }
                        } else {
                            audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                            return true;
                        }
                    } else {
                        audience.sendMessage(this.plugin.prefix().color(NamedTextColor.GREEN).append(Component.text("That player is not online!", NamedTextColor.RED)));
                        XSound.ENTITY_VILLAGER_NO.play(player.getLocation(), 1.0F, 1.0F);
                        return true;
                    }
                } else {
                    audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                    return true;
                }
            } else {
                audience.sendMessage(Component.text("Unknown command. Type \"/help\" for help."));
                return true;
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("open")) {
                WardrobeGui.openGui(player, WardrobeGui.EnumPage.PAGE_1);
            } else {
                audience.sendMessage(MiniMessageUtils.translate(this.plugin.mini(), this.plugin.config().getPermissionDeniedMessage()));
            }
            return true;
        } else {
            audience.sendMessage(MiniMessageUtils.translate(this.plugin.mini(), this.plugin.config().getPermissionDeniedMessage()));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            List<String> result = new ArrayList<>();
            if (args.length == 1) {
                arguments.clear();
                result.clear();
                arguments.add("reload");
                arguments.add("open");
                arguments.add("reset");
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                        result.add(a);
                }
                return result;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    result.clear();
                    return result;
                } else if (args[0].equalsIgnoreCase("open") || args[0].equalsIgnoreCase("reset")) {
                    arguments.clear();
                    result.clear();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String player1 = player.getName().toString();
                        arguments.add(player1);
                    }
                    for (String a : arguments) {
                        if (a.toLowerCase().startsWith(args[1]))
                            result.add(a);
                    }
                    return result;
                } else {
                    result.clear();
                    return result;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("open")) {
                    result.clear();
                    return result;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    arguments.clear();
                    result.clear();
                    arguments.add("page");
                    arguments.add("all");
                    arguments.add("slot");
                    for (String a : arguments) {
                        if (a.toLowerCase().startsWith(args[2].toLowerCase()))
                            result.add(a);
                    }
                    return result;
                } else {
                    result.clear();
                    return result;
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("open")) {
                    result.clear();
                    return result;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (args[2].equalsIgnoreCase("page")) {
                        arguments.clear();
                        result.clear();
                        arguments.add("1");
                        arguments.add("2");
                        for (String a : arguments) {
                            if (a.toLowerCase().startsWith(args[3].toLowerCase()))
                                result.add(a);
                        }
                        return result;
                    } else if (args[2].equalsIgnoreCase("slot")) {
                        arguments.clear();
                        result.clear();
                        for (int i = 1; i <= 18 ; i++) {
                            arguments.add(String.valueOf(i));
                        }
                        arguments.add("1");
                        arguments.add("2");
                        arguments.add("3");
                        arguments.add("4");
                        arguments.add("5");
                        arguments.add("6");
                        arguments.add("7");
                        arguments.add("8");
                        arguments.add("9");
                        arguments.add("10");
                        arguments.add("11");
                        arguments.add("12");
                        arguments.add("13");
                        arguments.add("14");
                        arguments.add("15");
                        arguments.add("16");
                        arguments.add("17");
                        arguments.add("18");
                        for (String a : arguments) {
                            if (a.toLowerCase().startsWith(args[3].toLowerCase()))
                                result.add(a);
                        }
                        return result;
                    } else {
                        result.clear();
                        return result;
                    }
                } else {
                    result.clear();
                    return result;
                }
            } else if (args.length > 4) {
                result.clear();
                return result;
            }
        }
        Player p = (Player) sender;
        List<String> result = new ArrayList<String>();
        if (p.hasPermission("CustomCrafting.Admin")) {
            if (args.length == 1) {
                arguments.clear();
                result.clear();
                arguments.add("reload");
                arguments.add("open");
                arguments.add("reset");
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                        result.add(a);
                }
                return result;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    result.clear();
                    return result;
                } else if (args[0].equalsIgnoreCase("open") || args[0].equalsIgnoreCase("reset")) {
                    arguments.clear();
                    result.clear();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String player1 = player.getName().toString();
                        arguments.add(player1);
                    }
                    for (String a : arguments) {
                        if (a.toLowerCase().startsWith(args[1]))
                            result.add(a);
                    }
                    return result;
                } else {
                    result.clear();
                    return result;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("open")) {
                    result.clear();
                    return result;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    arguments.clear();
                    result.clear();
                    arguments.add("page");
                    arguments.add("all");
                    arguments.add("slot");
                    for (String a : arguments) {
                        if (a.toLowerCase().startsWith(args[2].toLowerCase()))
                            result.add(a);
                    }
                    return result;
                } else {
                    result.clear();
                    return result;
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("open")) {
                    result.clear();
                    return result;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (args[2].equalsIgnoreCase("page")) {
                        arguments.clear();
                        result.clear();
                        arguments.add("1");
                        arguments.add("2");
                        for (String a : arguments) {
                            if (a.toLowerCase().startsWith(args[3].toLowerCase()))
                                result.add(a);
                        }
                        return result;
                    } else if (args[2].equalsIgnoreCase("slot")) {
                        arguments.clear();
                        result.clear();
                        arguments.add("1");
                        arguments.add("2");
                        arguments.add("3");
                        arguments.add("4");
                        arguments.add("5");
                        arguments.add("6");
                        arguments.add("7");
                        arguments.add("8");
                        arguments.add("9");
                        arguments.add("10");
                        arguments.add("11");
                        arguments.add("12");
                        arguments.add("13");
                        arguments.add("14");
                        arguments.add("15");
                        arguments.add("16");
                        arguments.add("17");
                        arguments.add("18");
                        for (String a : arguments) {
                            if (a.toLowerCase().startsWith(args[3].toLowerCase()))
                                result.add(a);
                        }
                        return result;
                    } else {
                        result.clear();
                        return result;
                    }
                } else {
                    result.clear();
                    return result;
                }
            } else if (args.length > 4) {
                result.clear();
                return result;
            }
        }
        return null;
    }

}
