package de.afgmedia.ftsskills.skillsystem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Skill {

    private final ArrayList<Material> crafting;
    private final ArrayList<Material> forging;
    private final ArrayList<Material> enchanting;
    private final ArrayList<EntityType> mobLoot;
    private final ArrayList<EntityType> breeding;
    private final ArrayList<Material> blockLoot;
    private final ArrayList<Material> fish;

    private final String name;
    private final ArrayList<String> needed;

    private final Material material;
    private final String description;

    private final ItemStack itemStack;

    private boolean backpacks, potions, composter, honey, scrolls;

    public Skill(ArrayList<Material> crafting, ArrayList<Material> forging, ArrayList<Material> enchanting, ArrayList<EntityType> mobLoot, ArrayList<EntityType> breeding, ArrayList<Material> blockLoot, ArrayList<Material> fish, String name, Material material, String description, ArrayList<String> needed) {
        this.crafting = crafting;
        this.forging = forging;
        this.enchanting = enchanting;
        this.mobLoot = mobLoot;
        this.breeding = breeding;
        this.blockLoot = blockLoot;
        this.fish = fish;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.material = material;
        this.description = description;
        this.needed = needed;

        itemStack = new ItemStack(material, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setDisplayName(this.name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(Arrays.asList(description.split("&nn")));
        itemStack.setItemMeta(meta);
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ArrayList<Material> getCrafting() {
        return crafting;
    }

    public ArrayList<Material> getForging() {
        return forging;
    }

    public ArrayList<Material> getEnchanting() {
        return enchanting;
    }

    public ArrayList<EntityType> getMobLoot() {
        return mobLoot;
    }

    public ArrayList<EntityType> getBreeding() {
        return breeding;
    }

    public ArrayList<Material> getBlockLoot() {
        return blockLoot;
    }

    public ArrayList<Material> getFish() {
        return fish;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getNeeded() {
        return needed;
    }

    public void setBackpacks(boolean backpacks) {
        this.backpacks = backpacks;
    }

    public void setComposter(boolean composter) {
        this.composter = composter;
    }

    public void setHoney(boolean honey) {
        this.honey = honey;
    }

    public void setScrolls(boolean scrolls) {
        this.scrolls = scrolls;
    }

    public boolean isAbleToMakeHoney() {
        return honey;
    }

    public boolean isAbleToUseComposter() {
        return composter;
    }

    public void setPotions(boolean potions) {
        this.potions = potions;
    }

    public boolean isAbleToCraftBackpacks() {
        return backpacks;
    }

    public boolean isAbleToCraftPotions() {
        return potions;
    }

    public boolean isAbleToCraftScrolls() {
        return scrolls;
    }
}
