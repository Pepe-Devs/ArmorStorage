package com.pepedevs.wardrobe;

import mc.ultimatecore.skills.armorequipevent.ArmorEquipEvent;
import mc.ultimatecore.skills.armorequipevent.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WardrobeSet {

    public static final WardrobeSet NULL = new WardrobeSet(new ItemStack[]{null, null, null, null}, State.LOCKED);

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private State state;

    public WardrobeSet(ItemStack[] item, State state) {
        this.helmet = item[0];
        this.chestplate = item[1];
        this.leggings = item[2];
        this.boots = item[3];
        this.state = state;
    }

    public static WardrobeSet fromString(String text) {
        return Utils.fromBase64(text);
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public void set(EnumArmor armor, ItemStack item) {
        if (armor == EnumArmor.HELMET) {
            this.helmet = item;
        } else if (armor == EnumArmor.CHESTPLATE) {
            this.chestplate = item;
        } else if (armor == EnumArmor.LEGGINGS) {
            this.leggings = item;
        } else if (armor == EnumArmor.BOOTS) {
            this.boots = item;
        }
    }

    public ItemStack get(EnumArmor armor) {
        if (armor == EnumArmor.HELMET) {
            return this.helmet;
        } else if (armor == EnumArmor.CHESTPLATE) {
            return this.chestplate;
        } else if (armor == EnumArmor.LEGGINGS) {
            return this.leggings;
        } else if (armor == EnumArmor.BOOTS) {
            return this.boots;
        }

        return null;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void apply(Player player) {
        ArmorEquipEvent e1 = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DISPENSER, ArmorType.HELMET, player.getInventory().getHelmet(), this.getHelmet());
        Bukkit.getPluginManager().callEvent(e1);
        player.getInventory().setHelmet(this.getHelmet());

        ArmorEquipEvent e2 = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DISPENSER, ArmorType.CHESTPLATE, player.getInventory().getChestplate(), this.getChestplate());
        Bukkit.getPluginManager().callEvent(e2);
        player.getInventory().setChestplate(this.getChestplate());

        ArmorEquipEvent e3 = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DISPENSER, ArmorType.LEGGINGS, player.getInventory().getLeggings(), this.getLeggings());
        Bukkit.getPluginManager().callEvent(e3);
        player.getInventory().setLeggings(this.getLeggings());

        ArmorEquipEvent e4 = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DISPENSER, ArmorType.BOOTS, player.getInventory().getBoots(), this.getBoots());
        Bukkit.getPluginManager().callEvent(e4);
        player.getInventory().setBoots(this.getBoots());
    }

    public boolean isComplete() {
        return this.helmet != null && this.chestplate != null && this.leggings != null && this.boots != null;
    }

    public boolean hasAny() {
        return this.helmet != null || this.chestplate != null || this.leggings != null || this.boots != null;
    }

    public boolean invalidate() {
        if (this.state == State.EMPTY && this.hasAny()) {
            this.state = State.READY;
            return true;
        } else if (this.state == State.READY && !this.hasAny()) {
            this.state = State.EMPTY;
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return Utils.toBase64(this);
    }

    public enum State {
        EQUIPPED,
        READY,
        EMPTY,
        LOCKED,
        ;
    }

    public enum EnumArmor {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        NONE,
        ;
    }

}
