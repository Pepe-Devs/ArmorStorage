package com.pepedevs.wardrobe;

import com.pepedevs.corelib.database.sql.hikaricp.HikariCP;
import com.pepedevs.corelib.database.sql.hikaricp.HikariClientBuilder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class Wardrobe extends JavaPlugin {

    private MiniMessage MINI;

    private BukkitAudiences adevnture;
    private Component prefix;

    private static Wardrobe instance;

    private PluginConfig config;
    private HikariCP database;

    @Override
    public void onEnable() {
        instance = this;
        MINI = MiniMessage.builder()
                .transformations(TransformationRegistry.builder()
                        .add(TransformationType.COLOR)
                        .add(TransformationType.DECORATION)
                        .add(TransformationType.HOVER_EVENT)
                        .add(TransformationType.CLICK_EVENT)
                        .add(TransformationType.KEYBIND)
                        .add(TransformationType.TRANSLATABLE)
                        .add(TransformationType.INSERTION)
                        .add(TransformationType.FONT)
                        .add(TransformationType.GRADIENT)
                        .add(TransformationType.RAINBOW)
                        .build())
                .strict(false)
                .build();
        adevnture = BukkitAudiences.create(this);
        prefix = Component.text("[Wardrobe] ");

        if (!this.loadConfig()) {
            return;
        }

        this.getServer().getPluginManager().registerEvents(new WardrobeListener(), this);
        this.getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        this.getCommand("wardrobe").setExecutor(new WardrobeCommand(this));
        this.getCommand("wardrobe").setTabCompleter(new WardrobeCommand(this));

    }

    public boolean loadConfig() {
        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdir();

        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveResource("config.yml", false);
        }

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
        this.config = new PluginConfig();
        this.config.load(configuration);

        if (!this.initDatabase()) {
            this.getServer().getLogger().severe("[Wardrobe] Cannot connect to MYSQL! Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return false;
        }

        return true;
    }

    public boolean initDatabase() {
        if (this.database != null && this.database.isConnected()) {
            try {
                this.database.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        this.database = new HikariClientBuilder(
                    this.config.getDatabase().getHost(),
                    this.config.getDatabase().getPort(),
                    this.config.getDatabase().getDatabase(),
                    this.config.getDatabase().getUsername(),
                    this.config.getDatabase().getPassword(),
                    this.config.getDatabase().isReconnect(),
                    this.config.getDatabase().isSSL())
                .addProperty("cachePrepStmts", "true")
                .addProperty("prepStmtCacheSize", "250")
                .addProperty("prepStmtCacheSqlLimit", "2048")
                .addProperty("useServerPrepStmts", "true")
                .addProperty("useLocalSessionState", "true")
                .addProperty("rewriteBatchedStatements", "true")
                .addProperty("cacheResultSetMetadata", "true")
                .addProperty("cacheServerConfiguration", "true")
                .addProperty("elideSetAutoCommits", "true")
                .addProperty("maintainTimeStats", "false")
                .build();

        try {
            this.database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        this.database.executeAsync("CREATE TABLE IF NOT EXISTS `Wardrobe-Page-1` (`UUID` VARCHAR(36) UNIQUE PRIMARY KEY NOT NULL, " +
                "`NAME` VARCHAR(16) NOT NULL, `SLOT-1` TEXT, `SLOT-2` TEXT, `SLOT-3` TEXT, `SLOT-4` TEXT, " +
                "`SLOT-5` TEXT, `SLOT-6` TEXT, `SLOT-7` TEXT, `SLOT-8` TEXT, `SLOT-9` TEXT);");
        this.database.executeAsync("CREATE TABLE IF NOT EXISTS `Wardrobe-Page-2` (`UUID` VARCHAR(36) UNIQUE PRIMARY KEY NOT NULL, " +
                "`NAME` VARCHAR(16) NOT NULL, `SLOT-10` TEXT, `SLOT-11` TEXT, `SLOT-12` TEXT, `SLOT-13` TEXT, " +
                "`SLOT-14` TEXT, `SLOT-15` TEXT, `SLOT-16` TEXT, `SLOT-17` TEXT, `SLOT-18` TEXT);");

        return true;
    }

    public static Wardrobe wardrobe() {
        return instance;
    }

    public MiniMessage mini() {
        return MINI;
    }

    public BukkitAudiences adventure() {
        return adevnture;
    }

    public Component prefix() {
        return prefix;
    }

    public PluginConfig config() {
        return this.config;
    }

    public HikariCP database() {
        return this.database;
    }

}
