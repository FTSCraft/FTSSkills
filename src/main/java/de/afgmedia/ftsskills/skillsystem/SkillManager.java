package de.afgmedia.ftsskills.skillsystem;

import de.afgmedia.ftsskills.data.DataManager;
import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.gui.CategoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SkillManager {

    private Skills plugin;

    private final DataManager dataManager;
    private final LevelManager levelManager;

    //every skills with name
    private final HashMap<String, Skill> skills = new HashMap<>();

    //Every skilluser with player
    private final HashMap<Player, SkillUser> users = new HashMap<>();

    //Every Categoryname with Category
    private final HashMap<String, Category> categories = new HashMap<>();

    private final HashMap<Category, CategoryGUI> categoryGuis = new HashMap<>();

    //For which item do I need for which skill at crafting, forging, enchanting
    private final HashMap<Material, Skill> crafting = new HashMap<>();
    private final HashMap<Material, Skill> forging = new HashMap<>();
    private final HashMap<Material, Skill> enchanting = new HashMap<>();
    private final HashMap<EntityType, Skill> mobLoot = new HashMap<>();
    private final HashMap<EntityType, Skill> breeding = new HashMap<>();
    private final HashMap<Material, Skill> blockLoot = new HashMap<>();
    private final HashMap<Material, Skill> fish = new HashMap<>();
    private final HashMap<String, Skill> customItems = new HashMap<>();

    public SkillManager(Skills plugin) {
        this.plugin = plugin;
        this.dataManager = new DataManager(plugin);
        this.levelManager = new LevelManager();
    }

    public boolean checkActivity(Object obj, Player p, Activity activity) {

        if (p.hasPermission("ftsskills.bypass")) {
            return true;
        }

        if (obj == Material.APPLE) {
            return true;
        }

        //Init skill variable
        Skill skill = null;

        //Get User
        SkillUser user = users.get(p);

        //get the right hashmap for the activity + skill

        //If the object is a material, check the skills who use materials
        if (obj instanceof Material mat) {

            skill = switch (activity) {
                case CRAFTING -> crafting.get(mat);
                case FORGING -> forging.get(mat);
                case ENCHANTING -> enchanting.get(mat);
                case BLOCK_LOOT -> blockLoot.get(mat);
                case FISH -> fish.get(mat);
                default -> skill;
            };

        } else if (obj instanceof EntityType type) {

            if (Values.DEBUG)
                System.out.println("entity");

            if (Values.DEBUG)
                for (EntityType entityType : mobLoot.keySet()) {
                    System.out.println(entityType.toString() + " " + mobLoot.get(entityType).getName());
                }

            skill = switch (activity) {
                case MOB_LOOT -> mobLoot.get(type);
                case BREED -> breeding.get(type);
                default -> skill;
            };

        } else if (obj instanceof String sign) {
            skill = customItems.get(sign);
        }

        //If the skill dosen't exist, it means everyone is able to craft it so it will return a true
        if (skill == null) {
            if (Values.DEBUG)
                System.out.println("null");
            return true;
        }

        //Check if the user got the skill and return true or false
        if (Values.DEBUG)
            System.out.println(skill.getName());
        return user.getSkills().contains(skill);

    }


    public boolean checkIfAbleToMakeHoney(Player p) {

        if (p.hasPermission("ftsskills.bypass")) {
            return true;
        }

        //Get User
        SkillUser user = users.get(p);

        for (Skill skill : user.getSkills()) {
            if (skill.isAbleToMakeHoney())
                return true;
        }

        return false;
    }


    public boolean checkIfAbleToCraftPotions(Player p) {

        if (p.hasPermission("ftsskills.bypass")) {
            return true;
        }

        //Get User
        SkillUser user = users.get(p);

        for (Skill skill : user.getSkills()) {
            if (skill.isAbleToCraftPotions())
                return true;
        }

        return false;

    }

    public void addSkill(Skill skill) {

        skills.put(skill.getName(), skill);

        for (Material material : skill.getCrafting()) {
            crafting.put(material, skill);
        }

        for (Material material : skill.getEnchanting()) {
            enchanting.put(material, skill);
        }

        for (Material material : skill.getForging()) {
            forging.put(material, skill);
        }

        for (Material material : skill.getFish()) {
            fish.put(material, skill);
        }

        for (Material material : skill.getBlockLoot()) {
            blockLoot.put(material, skill);
        }

        for (EntityType type : skill.getMobLoot()) {
            if (Values.DEBUG)
                System.out.println("mob: " + skill.getName() + " " + type);
            mobLoot.put(type, skill);
        }

        for (EntityType type : skill.getBreeding()) {
            breeding.put(type, skill);
        }

        for (String customItem : skill.getCustomItems()) {
            customItems.put(customItem, skill);
        }

    }

    public void addUser(SkillUser skillUser) {
        users.put(skillUser.getPlayer(), skillUser);
    }

    public void addExperience(Player player, int amount) {
        if (amount == 0)
            return;
        getUser(player).addExperience(amount);
    }

    public SkillUser getUser(Player p) {
        return users.get(p);
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public Skill getSkillByName(String name) {

        return skills.get(name);

    }

    public void removePlayer(Player p) {
        users.remove(p);
    }

    public void addCategory(Category category) {
        categories.put(category.getName(), category);
    }

    public Category getCategoryBySkill(Skill skill) {

        for (Category value : categories.values()) {
            if (value.getSkills().contains(skill))
                return value;
        }

        return null;

    }

    public enum Activity {

        CRAFTING, FORGING, ENCHANTING, BLOCK_LOOT, MOB_LOOT, BREED, FISH, BACKPACK, POTIONS, CUSTOM_CRAFT

    }

    public HashMap<String, Category> getCategories() {
        return categories;
    }

    public HashMap<Category, CategoryGUI> getCategoryGuis() {
        return categoryGuis;
    }

    public HashMap<String, Skill> getSkills() {
        return skills;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
