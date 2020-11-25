package de.afgmedia.ftsskills.skillsystem.gui;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.skillsystem.Skill;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChooseGUI {

    private Inventory gui;
    private Player player;
    private Skill skill;

    public ChooseGUI(Player player, Skill skill) {
        this.player = player;
        this.skill = skill;

        this.gui = Bukkit.createInventory(null, 9*1, Values.CHOOSE_GUI_NAME.replace("%s", skill.getName()));

        ItemStack yesItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemStack noItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);

        ItemMeta yesItemMeta = yesItem.getItemMeta();
        ItemMeta noItemMeta = noItem.getItemMeta();

        yesItemMeta.setDisplayName(Values.YES_ITEM_NAME);
        noItemMeta.setDisplayName(Values.BACK_ITEM_NAME);

        yesItem.setItemMeta(yesItemMeta);
        noItem.setItemMeta(noItemMeta);

        ItemStack skillItem = skill.getItemStack().clone();

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerItemMeta = filler.getItemMeta();
        fillerItemMeta.setDisplayName(" ");
        filler.setItemMeta(fillerItemMeta);

        gui.setItem(0, yesItem);
        gui.setItem(4, skillItem);
        gui.setItem(8, noItem);

        for (int i = 0; i < 8; i++) {

            if(gui.getItem(i) == null) {

                gui.setItem(i, filler.clone());

            }

        }



    }

    public Inventory getGui() {
        return gui;
    }
}
