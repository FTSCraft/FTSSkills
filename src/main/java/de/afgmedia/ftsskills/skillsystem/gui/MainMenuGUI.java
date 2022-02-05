package de.afgmedia.ftsskills.skillsystem.gui;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Category;
import de.afgmedia.ftsskills.skillsystem.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.plaf.SplitPaneUI;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainMenuGUI {

    private Skills plugin;

    private Inventory inv;

    private File file;
    private FileConfiguration cfg;

    private SkillManager manager;

    private Inventory gui;

    public MainMenuGUI(Skills plugin, File file) {
        this.plugin = plugin;
        this.file = file;

        manager = plugin.getManager();

        cfg = YamlConfiguration.loadConfiguration(file);

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
        HashMap<String, Category> keys = new HashMap<>();

        for (String key : cfg.getConfigurationSection("key").getKeys(false)) {
            //Get the name of the category
            String categoryName = cfg.getString("key."+key);
            //Put the char and the category in the hashmap

            keys.put(key, manager.getCategories().get(categoryName));

         }

        gui = Bukkit.createInventory(null, 9*6, Values.MAIN_MENU_GUI_INVENTORY_NAME);

        int place = 0;

        for (String string : strings) {

            for (String s : string.split("")) {

                ItemStack is = null;

                if(keys.get(s) != null) {

                    Category category = keys.get(s);

                    is = new ItemStack(category.getMat());

                    ItemMeta im = is.getItemMeta();

                    im.setDisplayName(category.getName());

                    im.setLore(Arrays.asList(category.getDescription().split("&nn")));

                    is.setItemMeta(im);

                }

                if(s.equalsIgnoreCase("Y")) {

                    is = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(Values.ITEM_INFO_NAME);
                    im.setLore(Arrays.asList("§eDu möchtest mehr über deine Stats erfahren? Klick hier!", "§eAlternativ kannst du auch §c/skills info §eeingeben"));
                    is.setItemMeta(im);

                }

                if(s.equalsIgnoreCase("Z")) {

                    is = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(Values.ITEM_UNLEARN_SKILL_NAME);
                    im.setLore(Arrays.asList("§eDu verlierst einen Skill und 500 Taler", "§eDafür bekommst du einen neuen Skillpunk!", "§eFür Reisende kostenlos!"));
                    is.setItemMeta(im);

                }

                if(s.equalsIgnoreCase("*") || is == null) {

                    is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(" ");
                    is.setItemMeta(im);

                }

                gui.setItem(place, is);

                place++;

            }

        }



    }

    public Inventory getInventory() {
        return gui;
    }
}
