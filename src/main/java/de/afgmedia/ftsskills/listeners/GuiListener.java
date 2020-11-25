package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Category;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillUser;
import de.afgmedia.ftsskills.skillsystem.gui.CategoryGUI;
import de.afgmedia.ftsskills.skillsystem.gui.ChooseGUI;
import de.afgmedia.ftsskills.skillsystem.gui.GuiType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class GuiListener implements Listener {

    private Skills plugin;

    public GuiListener(Skills plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player p = (Player) event.getWhoClicked();

        SkillUser u = plugin.getManager().getUser(p);

        if(event.getInventory().getType() == InventoryType.BREWING) {
            if(!plugin.getManager().checkIfAbleToCraftPotions(p)) {
                if(event.getCurrentItem().getType() == Material.POTION) {
                    PotionMeta potionMeta = (PotionMeta) event.getCurrentItem().getItemMeta();
                    if(potionMeta == null)
                        return;
                    if (potionMeta.getBasePotionData().getType() == PotionType.WATER) {
                        event.setCancelled(true);
                        p.sendMessage(Values.MESSAGE_NEED_TO_SKILL_POTIONS);
                    }
                }
            }
        }

        if (u.getInventoryType() != null) {

            event.setCancelled(true);

            ItemStack is = event.getCurrentItem();
            if(is == null)
                return;
            if(!is.hasItemMeta())
                return;
            String name = is.getItemMeta().getDisplayName();

            switch (u.getInventoryType()) {

                case MAIN_MENU:

                    if(plugin.getManager().getCategories().get(name) != null) {

                        Category category = plugin.getManager().getCategories().get(name);

                        p.openInventory(plugin.getManager().getCategoryGuis().get(category).getInventory());

                        u.setInventoryType(GuiType.CATEGORY);

                    }

                    if(name.equalsIgnoreCase(Values.ITEM_INFO_NAME)) {

                        Bukkit.getServer().dispatchCommand(p, "ftsskills info");

                    }

                    if(name.equalsIgnoreCase(Values.ITEM_UNLEARN_SKILL_NAME)) {

                        if(u.getSkills().size() == 0) {

                            p.sendMessage(Values.PREFIX + "Du hast noch keine Skills erlernt. Was willst du denn bitte verlernen?");

                            break;
                        }

                        Bukkit.getServer().dispatchCommand(p, "ftsskills verlernen");
                        p.closeInventory();

                    }

                    break;

                case CHOOSE:

                    ItemStack skillItem = event.getInventory().getItem(4);
                    String skillName = skillItem.getItemMeta().getDisplayName();

                    Skill skill = plugin.getManager().getSkillByName(skillName);

                    if(event.getSlot() == 0) {

                        if (u.getSkillPoints() >= 1) {

                            u.addSkill(skill);

                            p.sendMessage(Values.PREFIX + "Du hast nun " + skillName + " §7erlernt!");

                            p.closeInventory();

                            return;

                        } else
                            p.sendMessage(Values.PREFIX + "Dafür ist dein Level nicht hoch genug! Farm mal wieder ein wenig mehr");

                    } else if(event.getSlot() == 8) {

                        Category category = plugin.getManager().getCategoryBySkill(skill);

                        p.openInventory(plugin.getManager().getCategoryGuis().get(category).getInventory());

                        u.setInventoryType(GuiType.CATEGORY);

                    }

                    break;

                case CATEGORY:

                    if(name.equalsIgnoreCase("§4Zurück")) {

                        p.openInventory(plugin.getMainMenuGUI().getInventory());

                        SkillUser user = plugin.getManager().getUser(p);

                        user.setInventoryType(GuiType.MAIN_MENU);

                        return;

                    }

                    skill = plugin.getManager().getSkillByName(name);

                    if(skill == null) {
                        break;
                    }

                    if(u.getSkills().contains(skill)) {

                        p.sendMessage(Values.PREFIX + "Du hast diesen Skill bereits erlernt!");

                        return;

                    }

                    if(skill.getNeeded().size() >= 1) {

                        boolean gotEverythingPlayerNeeds = true;

                        for (String neededSkillName : skill.getNeeded()) {
                            Skill neededSkill = plugin.getManager().getSkillByName(neededSkillName);
                            if (!u.getSkills().contains(neededSkill)) {
                                if(gotEverythingPlayerNeeds) {
                                    p.sendMessage(Values.PREFIX + "Du musst davor noch folgendes lernen: ");
                                    gotEverythingPlayerNeeds = false;
                                }

                                p.sendMessage("§7- " + neededSkill.getName());

                            }
                        }

                        if(!gotEverythingPlayerNeeds)
                            return;

                    }

                    ChooseGUI gui = new ChooseGUI(p, skill);

                    p.openInventory(gui.getGui());

                    u.setInventoryType(GuiType.CHOOSE);


                    break;

            }


        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        Player p = (Player) event.getPlayer();

        SkillUser user = plugin.getManager().getUser(p);

        if(user.getInventoryType() != null)
            user.setInventoryType(null);

    }

}
