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

    private final List<Material> crafting;
    private final List<Material> forging;
    private final List<Material> enchanting;
    private final List<EntityType> mobLoot;
    private final List<EntityType> breeding;
    private final List<Material> blockLoot;
    private final List<Material> fish;
    private final List<String> customItems;
    private final List<String> permissions;

    private final String name;
    private final List<String> needed;

    private final Material material;
    private final String description;

    private final ItemStack itemStack;

    private boolean backpacks, potions, composter, honey, scrolls;

    public Skill(List<Material> crafting, List<Material> forging, List<Material> enchanting, List<EntityType> mobLoot, List<EntityType> breeding, List<Material> blockLoot, List<Material> fish, List<String> customItems, List<String> permissions, String name, Material material, String description, List<String> needed) {
        this.crafting = crafting;
        this.forging = forging;
        this.enchanting = enchanting;
        this.mobLoot = mobLoot;
        this.breeding = breeding;
        this.blockLoot = blockLoot;
        this.fish = fish;
        this.customItems = customItems;
        this.permissions = permissions;
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

    public List<Material> getCrafting() {
        return crafting;
    }

    public List<Material> getForging() {
        return forging;
    }

    public List<Material> getEnchanting() {
        return enchanting;
    }

    public List<EntityType> getMobLoot() {
        return mobLoot;
    }

    public List<EntityType> getBreeding() {
        return breeding;
    }

    public List<Material> getBlockLoot() {
        return blockLoot;
    }

    public List<String> getCustomItems() {
        return customItems;
    }

    public List<Material> getFish() {
        return fish;
    }

    public String getName() {
        return name;
    }

    public List<String> getNeeded() {
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

    public List<String> getPermissions() {
        return permissions;
    }
}
