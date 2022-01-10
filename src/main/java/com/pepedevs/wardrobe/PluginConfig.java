package com.pepedevs.wardrobe;

import com.pepedevs.corelib.utils.configuration.Loadable;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableCollectionEntry;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginConfig implements Loadable {

    @LoadableEntry(key = "title")
    private String title;

    @LoadableEntry(key = "admin-permission")
    private String adminPermission;

    @LoadableCollectionEntry(subsection = "slot-permission")
    private Map<String, Slot> slots;

    @LoadableCollectionEntry(subsection = "locked-slot")
    private Map<String, SlotItem> lockedSlot;

    @LoadableCollectionEntry(subsection = "available-slot")
    private Map<String, SlotItem> availableSlot;

    @LoadableCollectionEntry(subsection = "buttons")
    private Map<String, Button> buttons;

    @LoadableEntry(key = "wardrobe-message.permission-denied")
    private String permissionDeniedMessage;

    @LoadableEntry(key = "wardrobe-message.no-space")
    private String noSpaceMessage;

    @LoadableEntry(key = "wardrobe-message.modify-armor-denied")
    private String modifyArmorDeniedMessage;

    private final Database database;

    public PluginConfig() {
        this.database = new Database();
        this.slots = new HashMap<>();
        this.lockedSlot = new HashMap<>();
        this.availableSlot = new HashMap<>();
        this.buttons = new HashMap<>();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        this.loadEntries(section);
        this.database.load(section.getConfigurationSection("database"));
        return this;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAdminPermission() {
        return this.adminPermission;
    }

    public Database getDatabase() {
        return this.database;
    }

    public Slot getSlot(int i) {
        return this.slots.get("slot-" + i);
    }

    public SlotItem getLockedSlot(String slot) {
        return this.lockedSlot.get(slot);
    }

    public SlotItem getAvailableSlot(String slot) {
        return this.availableSlot.get(slot);
    }

    public Button getButton(String button) {
        return this.buttons.get(button);
    }

    public String getPermissionDeniedMessage() {
        return this.permissionDeniedMessage;
    }

    public String getNoSpaceMessage() {
        return this.noSpaceMessage;
    }

    public String getModifyArmorDeniedMessage() {
        return this.modifyArmorDeniedMessage;
    }

    public static class Slot implements Loadable {

        @LoadableEntry(key = "permission")
        private String permission;

        @LoadableEntry(key = "require-prefix")
        private String requiredPrefix;

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.permission != null & this.requiredPrefix != null;
        }

        public String getPermission() {
            return permission;
        }

        public String getRequiredPrefix() {
            return requiredPrefix;
        }

    }

    public static class SlotItem implements Loadable {

        public static final String HELMET_SLOT = "helmet-slot";
        public static final String CHESTPLATE_SLOT = "chestplate-slot";
        public static final String LEGGINGS_SLOT = "leggings-slot";
        public static final String BOOTS_SLOT = "boots-slot";
        public static final String LOCKED_BUTTON = "locked-button";
        public static final String EMPTY_SLOT_BUTTON = "empty-slot-button";
        public static final String READY_BUTTON = "ready-button";
        public static final String EQUIPPED_BUTTON = "equipped-button";

        @LoadableEntry(key = "name")
        private String name;

        @LoadableEntry(key = "lore")
        private List<String> lore;

        public SlotItem() {
            this.lore = new ArrayList<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.name != null;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }

    }

    public static class Button implements Loadable {

        public static final String GO_BACK_BUTTON = "go-back-button";
        public static final String CLOSE_BUTTON = "close-button";
        public static final String NEXT_PAGE_BUTTON = "next-page-button";
        public static final String PREVIOUS_PAGE_BUTTON = "previous-page-button";

        @LoadableEntry(key = "enable")
        private boolean enable;

        @LoadableEntry(key = "command")
        private String command;

        @LoadableEntry(key = "slot")
        private int slot;

        @LoadableEntry(key = "name")
        private String name;

        @LoadableEntry(key = "lore")
        private List<String> lore;

        public Button() {
            this.lore = new ArrayList<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.name != null;
        }

        public boolean isEnable() {
            return enable;
        }

        public String getCommand() {
            return command;
        }

        public int getSlot() {
            return slot;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }

    }

    public static class AllowItem implements Loadable {

        public static final String HELMET = "helmet-slot";
        public static final String CHESTPLATE = "chestplate-slot";
        public static final String LEGGINGS = "leggings-slot";
        public static final String BOOTS = "boots-slot";

        @LoadableEntry(key = "name")
        private String name;

        @LoadableEntry(key = "lore")
        private String lore;

        @LoadableEntry(key = "type")
        private List<String> type;

        @LoadableEntry(key = "specific-check-lore")
        private String specificLore;

        public AllowItem() {
            this.type = new ArrayList<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public String getName() {
            return name;
        }

        public String getLore() {
            return lore;
        }

        public List<String> getType() {
            return type;
        }

        public String getSpecificLore() {
            return specificLore;
        }

    }

    public static class Database implements Loadable {

        @LoadableEntry(key = "host")
        private String host;

        @LoadableEntry(key = "port")
        private int port;

        @LoadableEntry(key = "database")
        private String database;

        @LoadableEntry(key = "username")
        private String username;

        @LoadableEntry(key = "password")
        private String password;

        @LoadableEntry(key = "reconnect")
        private boolean reconnect;

        @LoadableEntry(key = "ssl")
        private boolean ssl;

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getDatabase() {
            return database;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public boolean isReconnect() {
            return reconnect;
        }

        public boolean isSSL() {
            return ssl;
        }

    }

}
