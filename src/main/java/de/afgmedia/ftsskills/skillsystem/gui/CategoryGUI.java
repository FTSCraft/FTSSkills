package de.afgmedia.ftsskills.skillsystem.gui;

import de.afgmedia.ftsskills.data.DataManager;
import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Category;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CategoryGUI {

    private Category category;

    private Inventory inventory;

    private FileConfiguration cfg;
    private File file;

    private SkillManager manager;

    private Skills plugin;

    public CategoryGUI(Skills plugin, File file) {
        this.plugin = plugin;
        this.file = file;


        manager = plugin.getManager();

        cfg = YamlConfiguration.loadConfiguration(file);

        this.category = plugin.getManager().getCategories().get(cfg.getString("category"));


        if(file.exists())
            load();
    }


    private void load() {

        ArrayList<String> strings = new ArrayList<>();

        //Add every line to the arraylist
        for (Object o : cfg.getList("gui")) {
            strings.add((String) o);
        }

        //hashmap in which we store the char + category
        HashMap<String, Skill> keys = new HashMap<>();

        for (String key : cfg.getConfigurationSection("key").getKeys(false)) {
            //Get the name of the category
            String skillName = cfg.getString("key." + key);
            //Put the char and the category in the hashmap

            keys.put(key, manager.getSkillByName(skillName));
        }

        inventory = Bukkit.createInventory(null, 9 * 6, Values.CATEGORY_GUI_NAME.replace("%s", category.getName()));

        int place = 0;

        for (String string : strings) {

            for (String s : string.split("")) {

                ItemStack is = null;

                if (keys.get(s) != null) {

                    Skill skill = keys.get(s);

                    is = skill.getItemStack().clone();

                    /*ItemMeta im = is.getItemMeta();

                    im.setDisplayName(category.getName());

                    im.setLore(Arrays.asList(category.getDescription().split("nn")));

                    is.setItemMeta(im);
                     */

                }

                if (s.equalsIgnoreCase("*") || is == null) {

                    is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(" ");
                    is.setItemMeta(im);

                }

                if(s.equalsIgnoreCase("X")) {

                    is = new ItemStack(Material.RED_STAINED_GLASS_PANE);

                    ItemMeta im = is.getItemMeta();

                    im.setDisplayName("§4Zurück");

                    is.setItemMeta(im);

                }

                inventory.setItem(place, is);

                place++;

            }

        }

        plugin.getManager().getCategoryGuis().put(category, this);

    }

    public Inventory getInventory() {
        return inventory;
    }
}
