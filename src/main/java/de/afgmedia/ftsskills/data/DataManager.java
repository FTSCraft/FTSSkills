package de.afgmedia.ftsskills.data;

import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Category;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillUser;
import de.afgmedia.ftsskills.skillsystem.gui.CategoryGUI;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DataManager {

    private Skills plugin;
    private File folder;

    private boolean debug = Values.DEBUG;

    public DataManager(Skills plugin) {
        this.plugin = plugin;
        this.folder = plugin.getDataFolder();

        if (!folder.exists())
            folder.mkdirs();

    }

    public void savePlayerData(SkillUser user) {

        File playerFolder = new File(folder + "//playerData//");

        if (!playerFolder.exists())
            playerFolder.mkdirs();

        File file = new File(playerFolder + "//" + user.getPlayer().getUniqueId().toString() + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("experience", user.getExperience());
        cfg.set("level", user.getLevel());

        cfg.set("highestLevel", user.getHighestLevel());

        ArrayList<String> skills = new ArrayList<>();

        for (Skill skill : user.getSkills()) {
            skills.add(skill.getName());
        }

        cfg.set("skills", skills);
        cfg.set("skillpoints", user.getSkillPoints());

        cfg.set("recievedFree", user.hasRecievedFree());

        try {

            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadPlayerData(Player player) {

        UUID uuid = player.getUniqueId();

        File playerFolder = new File(folder + "//playerData//");

        if (!playerFolder.exists())
            playerFolder.mkdirs();

        File file = new File(playerFolder + "//" + uuid.toString() + ".yml");

        if (file.exists()) {

            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            double experience = cfg.getDouble("experience");
            int level = cfg.getInt("level");
            int skillpoints = cfg.getInt("skillpoints");
            int highestLevel = cfg.getInt("highestLevel");
            boolean recievedFree = false;

            if(cfg.contains("recievedFree")) {
                recievedFree = cfg.getBoolean("recievedFree");
            }

            List list = cfg.getList("skills");

            ArrayList<Skill> skills = new ArrayList<>();

            int countUnknownSkills = 0;

            for (Object o : list) {

                if (plugin.getManager().getSkillByName((String) o) == null) {
                    countUnknownSkills++;
                    continue;
                }

                skills.add(plugin.getManager().getSkillByName((String) o));
            }

            if (level > Values.MAX_LEVEL) {
                countUnknownSkills = 0;
                level = Values.MAX_LEVEL;
                skillpoints = 10;
                skills.clear();
                experience = 0;
                highestLevel = Values.MAX_LEVEL;
                recievedFree = true;

                player.sendMessage("§l§cWeil du über Level 80 warst, wurden all deine Skills entfernt und du hast statt dessen 10 Skillpunkte erhalten.");
            }

            if (countUnknownSkills > 0) {
                player.sendMessage("§cDa du anscheinend einen Skill besaßt, den es nicht mehr gibt, haben wir die Skillpoints gutgeschrieben.");
                if ((skillpoints + countUnknownSkills + skills.size()) >= 10) {
                    level = Values.MAX_LEVEL;
                    skillpoints = 10;
                    skills.clear();
                    recievedFree = true;
                    experience = 0;
                    highestLevel = Values.MAX_LEVEL;
                } else skillpoints = skillpoints + countUnknownSkills;
            }

            if((skillpoints + skills.size() - 2) * 10 > level) {
                int newSkillpoints = level / 10;
                skills.clear();
                newSkillpoints += 2;
                recievedFree = true;

                skillpoints = newSkillpoints;

                player.sendMessage("§l§cDu hattest ein paaaaaar zu viele Skills für deine Level, es sollte nun angepasst sein! (Du müsstest dir deine Skillpoints noch mal neu verteilen)");
            }

            if(!recievedFree) {
                skillpoints = skillpoints + 2;
            }

            SkillUser user = new SkillUser(player, plugin);

            user.setExperience(experience);
            user.setLevel(level);
            user.setSkills(skills);
            user.setSkillPoints(skillpoints);
            user.setHighestLevel(level);
            user.setRecievedFree(true);

        } else {
            SkillUser user = new SkillUser(player, plugin);
            user.setRecievedFree(true);
            user.setSkillPoints(2);
        }
    }

    public void loadCategories() {

        File categoriesFolder = new File(folder + "//categories//");

        if (!categoriesFolder.exists()) {
            categoriesFolder.mkdirs();
        }

        for (File file : categoriesFolder.listFiles()) {

            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            String name = cfg.getString("name");

            Material mat = Material.getMaterial(cfg.getString("material"));

            String description = cfg.getString("description");

            ArrayList<Skill> skills = new ArrayList<>();

            for (Object o : cfg.getList("skills")) {
                skills.add(plugin.getManager().getSkillByName((String) o));
            }

            Category category = new Category(skills, name, mat, description);

            plugin.getManager().addCategory(category);

        }

    }

    public void loadSkills() {

        //Get the right folder
        File skillsFolder = new File(folder + "//skills//");

        //If the folder dosent exist, create it
        if (!skillsFolder.exists())
            skillsFolder.mkdir();

        //Go through every file
        for (File file : skillsFolder.listFiles()) {

            //If it isnt a yml, skip it
            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            //Make it an FileConfig
            FileConfiguration skillFile = YamlConfiguration.loadConfiguration(file);

            //Get the lists out of the yml
            List<String> craftingList = skillFile.getStringList("crafting");
            List<String> enchantingList = skillFile.getStringList("enchanting");
            List<String> forgingList = skillFile.getStringList("forging");
            List<String> breedingList = skillFile.getStringList("breeding");
            List<String> mobLootList = skillFile.getStringList("mobLoot");
            List<String> blockLootList = skillFile.getStringList("blockLoot");
            List<String> fishList = skillFile.getStringList("fishLoot");
            List<String> permissions = skillFile.getStringList("permissions");

            List<String> neededList = skillFile.getStringList("needed");

            //Create the lists we're gonna use
            ArrayList<Material> crafting = new ArrayList<Material>();
            ArrayList<Material> enchanting = new ArrayList<Material>();
            ArrayList<Material> forging = new ArrayList<Material>();
            ArrayList<EntityType> breeding = new ArrayList<>();
            ArrayList<EntityType> mobLoot = new ArrayList<>();
            ArrayList<Material> blockLoot = new ArrayList<>();
            ArrayList<Material> fish = new ArrayList<>();
            List<String> customItems = skillFile.getStringList("customItems");

            for (String s : blockLootList) {
                Material mat = Material.getMaterial(s);
                blockLoot.add(mat);
                if (Values.DEBUG)
                    System.out.println("bll: " + mat);
            }

            for (String s : fishList) {
                Material mat = Material.getMaterial(s);
                fish.add(mat);
                if (Values.DEBUG)
                    System.out.println("fish: " + mat);
            }

            for (String s : mobLootList) {
                EntityType type = EntityType.valueOf(s);
                mobLoot.add(type);
                if (Values.DEBUG)
                    System.out.println("mob: " + type);
            }

            for (String s : craftingList) {
                Material m = Material.getMaterial(s);
                if (m != null) {
                    crafting.add(m);
                }
                if (Values.DEBUG)
                    System.out.println("craft: " + m);
            }

            for (String s : breedingList) {
                EntityType type = EntityType.valueOf(s);
                breeding.add(type);
                if (Values.DEBUG)
                    System.out.println("breed: " + type);
            }


            for (String s : enchantingList) {
                Material m = Material.getMaterial(s);
                if (m != null)
                    enchanting.add(m);
                if (Values.DEBUG)
                    System.out.println("ench: " + m);
            }

            for (String s : forgingList) {
                Material m = Material.getMaterial(s);
                if (m != null)
                    forging.add(m);
                if (Values.DEBUG)
                    System.out.println("forge: " + m);
            }


            String name = skillFile.getString("name");
            if (!skillFile.contains("material")) {
                return;
            }

            Material mat = Material.getMaterial(skillFile.getString("material"));
            String lore = skillFile.getString("lore");

            Skill skill = new Skill(crafting, forging, enchanting, mobLoot, breeding, blockLoot, fish, customItems, permissions, name, mat, lore, neededList);

            if (skillFile.contains("backpack"))
                skill.setBackpacks(true);
            if (skillFile.contains("potions"))
                skill.setPotions(true);
            if(skillFile.contains("honey"))
                skill.setHoney(true);
            if(skillFile.contains("composter"))
                skill.setComposter(true);
            if(skillFile.contains("scrolls"))
                skill.setScrolls(true);

            plugin.getManager().addSkill(skill);

        }

    }

    public void loadCategoryGuis() {

        File guiFolder = new File(folder + "//gui//category//");

        if (!guiFolder.exists())
            guiFolder.mkdirs();

        for (File file : guiFolder.listFiles()) {

            CategoryGUI gui = new CategoryGUI(plugin, file);

        }

    }

}
