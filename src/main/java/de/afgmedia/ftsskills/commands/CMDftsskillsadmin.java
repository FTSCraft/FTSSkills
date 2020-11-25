package de.afgmedia.ftsskills.commands;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillUser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.plaf.InsetsUIResource;

public class CMDftsskillsadmin implements CommandExecutor {

    private Skills plugin;

    // /ftsskillsadmin target value wert
    // values:    werte:
    // level      int
    // xp         double
    // skillpoints int
    // addskill string
    // removeskill skill

    public CMDftsskillsadmin(Skills plugin) {
        this.plugin = plugin;
        plugin.getCommand("ftsskillsadmin").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if (!cs.hasPermission("ftsskills.admin")) {

            cs.sendMessage(Values.PREFIX + "Dafür hast du keine Rechte!");

            return true;

        }

        if (args.length == 3 || args.length == 2) {

            String tName = args[0];
            String value = args[1];
            String wert = null;

            if (args.length == 3)
                wert = args[2];

            Player target = Bukkit.getPlayer(tName);
            if (target == null) {
                cs.sendMessage(Values.PREFIX + "Dieser Spieler ist nicht online!");
                return true;
            }
            SkillUser user = plugin.getManager().getUser(target);

            switch (value) {
                case "level":

                    if (wert == null) {
                        cs.sendMessage(Values.PREFIX + "Bitte benutze den Command so: \n" +
                                "/ftsskillsadmin " + target.getName() + " level <+/- ANZAHL>");
                        return true;
                    }

                    int level = getValue(user.getLevel(), wert);
                    System.out.println(level);

                    if (level == -1) {
                        cs.sendMessage(Values.PREFIX + "Bitte benutze den Command so: \n" +
                                "/ftsskillsadmin " + target.getName() + " level <+/- ANZAHL>");
                        return true;
                    }

                    user.setLevel(level, false);
                    cs.sendMessage(Values.PREFIX + "Der Spieler hat nun §e" + user.getLevel() + "§7 Level!");
                    target.sendMessage(Values.PREFIX + "Deine Level wurden von §e" + cs.getName() + " §7manipuliert. Du hast nun §e" + user.getLevel() + "§7 Level!");

                    break;
                case "xp":

                    if (wert == null) {
                        cs.sendMessage(Values.PREFIX + "Bitte benutze den Command so: \n" +
                                "/ftsskillsadmin " + target.getName() + " xp <Anzahl>");
                    }

                    double val;

                    try {
                        val = Double.parseDouble(wert);
                    } catch (NumberFormatException e) {
                        cs.sendMessage(Values.PREFIX + "Bitte benutze den Command so: \n" +
                                "/ftsskillsadmin " + target.getName() + " xp <Anzahl>");
                        return true;
                    }

                    double xpForUp = plugin.getManager().getLevelManager().getNeededXPForLevel(user.getLevel());

                    if (val >= xpForUp) {
                        cs.sendMessage(Values.PREFIX + "Das würde das Level beeinflussen! Wenn du das machen willst, benutze bitte den dementsprechenden Befehl.");
                        return true;
                    }

                    user.setExperience(val);
                    //make a check so player can see
                    user.addExperience(0);

                    cs.sendMessage(Values.PREFIX + "Du hast die XP von dem Spieler auf §e" + val + " §7gesetzt!");
                    target.sendMessage(Values.PREFIX + "Deine XP wurden von §e" + cs.getName() + " §7auf §e" + val + "§7 gesetzt!");

                    break;
                case "skillpoints":

                    if (wert == null) {
                        cs.sendMessage(Values.PREFIX + "Bitte benutze den Command so: \n" +
                                "/ftsskillsadmin " + target.getName() + " skillpoints <+/- ANZAHL>");
                        return true;
                    }

                    int skillpoints = getValue(user.getSkillPoints(), wert);

                    if (skillpoints == -1) {
                        cs.sendMessage(Values.PREFIX + "Bitte benutze den Command so: \n" +
                                "/ftsskillsadmin " + target.getName() + " skillpoints <+/- ANZAHL>");
                        return true;
                    }

                    user.setSkillPoints(skillpoints);

                    cs.sendMessage(Values.PREFIX + "Der Spieler hat nun §e" + skillpoints + " §7Skillpoints!");
                    target.sendMessage(Values.PREFIX + "Deine Skillpoints wurden von §e" + cs.getName() + " §7manipuliert. Du hast nun §e " + skillpoints + "§7 Skillpoints!");

                    break;
                case "addskill":
                    if (wert == null) {
                        listSkills("a", cs, target);
                        break;
                    } else {

                        String skillName = translateSkillName(wert, true);
                        Skill skill = plugin.getManager().getSkillByName(skillName);

                        if (user.getSkills().contains(skill)) {
                            cs.sendMessage(Values.PREFIX + "Dieser Spieler hat diesen Skill bereits erlernt!");
                            return true;
                        }

                        user.getSkills().add(skill);
                        target.sendMessage(Values.PREFIX + "Dir wurde der Skill §e" + skillName + " §7von §e" + cs.getName() + " §7beigebracht!");
                        cs.sendMessage(Values.PREFIX + "Du hast dem Spieler §e" + target.getName() + " §7den Skill " + skillName + " §7beigebracht.");
                        break;
                    }
                case "removeskill":

                    if (wert == null) {
                        listSkills("r", cs, target);
                        break;
                    } else {

                        String skillName = translateSkillName(wert, true);
                        Skill skill = plugin.getManager().getSkillByName(skillName);

                        if (!user.getSkills().contains(skill)) {
                            cs.sendMessage(Values.PREFIX + "Dieser Spieler hat diesen Skill nicht!");
                            return true;
                        }

                        user.getSkills().remove(skill);
                        target.sendMessage(Values.PREFIX + "Dir wurde der Skill §e" + skillName + " §7von " + cs.getName() + " §7aberkannt!");
                        cs.sendMessage(Values.PREFIX + "Du hast dem Spieler §e" + target.getName() + " §7den Skill " + skillName + " §7abgezogen!");

                        break;
                    }
                case "info":
                    cs.sendMessage(Values.PREFIX + "Infos über: §e" + target.getName());
                    cs.sendMessage(Values.PREFIX + "Level: §e" + user.getLevel());
                    if (user.getHighestLevel() != user.getLevel()) {
                        cs.sendMessage(Values.PREFIX + "Höchstes Level: §e" + user.getHighestLevel());
                        cs.sendMessage(Values.PREFIX + "Wenn dein Level nicht so hoch ist wie dein höchstes, kannst du, bis du es wieder erriecht hast, keine Skillpunkte bekommen!");
                    }
                    cs.sendMessage(Values.PREFIX + "XP bis zum nächsten LevelUp: §e" + (plugin.getManager().getLevelManager().getNeededXPForLevel(user.getLevel()) - user.getExperience()));
                    cs.sendMessage(Values.PREFIX + "Skillpoints: §e" + user.getSkillPoints());
                    cs.sendMessage(Values.PREFIX + "Skills: ");
                    for (Skill skill : user.getSkills()) {
                        cs.sendMessage("§e- " + skill.getName());
                    }
                    break;
                default:
                    cs.sendMessage(getHelp());
                    break;
            }

        } else cs.sendMessage(getHelp());

        return false;
    }

    /**
     * @param value  r = remove a = add
     * @param cs     commandsender
     * @param target targetet player
     */
    private void listSkills(String value, CommandSender cs, Player target) {

        if (!(cs instanceof Player)) {
            cs.sendMessage("§cDiesen Command musst du ingame ausführen.");
            return;
        }

        SkillUser user = plugin.getManager().getUser(target);

        for (Skill skill : plugin.getManager().getSkills().values()) {

            if (value.equalsIgnoreCase("r")) {

                if (user.getSkills().contains(skill)) {

                    TextComponent textComponent = new TextComponent(skill.getName());
                    textComponent.setBold(true);
                    String translatedSkill = translateSkillName(skill.getName(), false);
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftsskillsadmin " + target.getName() + " removeskill " + translatedSkill));

                    cs.spigot().sendMessage(textComponent);

                }

            } else if (value.equalsIgnoreCase("a")) {

                if (!user.getSkills().contains(skill)) {

                    TextComponent textComponent = new TextComponent(skill.getName());
                    textComponent.setBold(true);
                    String translatedSkill = translateSkillName(skill.getName(), false);
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftsskillsadmin " + target.getName() + " addskill " + translatedSkill));

                    cs.spigot().sendMessage(textComponent);

                }

            }

        }

    }

    private String translateSkillName(String skillName, boolean from) {

        String newName;

        if (!from)
            newName = skillName.replace(" ", "_").replace("§", "&");
        else
            newName = skillName.replace("_", " ").replace("&", "§");

        return newName;

    }

    private int getValue(int wert, String changer) {
        System.out.println(wert);
        //if true = add if false = sub
        boolean add;

        if (changer.startsWith("+")) {
            add = true;
        } else if (changer.startsWith("-")) {
            add = false;
        } else {
            int val;
            try {
                val = Integer.parseInt(changer);
            } catch (NumberFormatException e) {
                return -1;
            }
            return val;
        }

        //if changer is only + or - just add or sub 1
        if (changer.length() == 1) {

            if (add)
                return wert + 1;
            else return wert - 1;

        }

        changer = changer.substring(1);
        int changerInt;

        try {
            changerInt = Integer.parseInt(changer);
        } catch (NumberFormatException e) {
            return -1;
        }

        if (add)
            return wert + changerInt;
        else return wert - changerInt;

    }

    private String getHelp() {

        return "§c---§e/ftsskillsadmin§c---\n" +
                "§c/.. <Spieler> level <+/-ANZAHL> §eBearbeite das Level eines Spielers\n" +
                "§c/.. <Spieler> xp <Anzahl> §eSetze die XP für einen Spieler\n" +
                "§c/.. <Spieler> skillpoints <+/-ANZAHL> §eBearbeite die Anzahl an Skillpoints für einen Spieler" +
                "§c/.. <Spieler> addskill §eFüge einem Spieler einen Skill hinzu\n" +
                "§c/.. <Spieler> removeskill §eEntferne einem Spieler einen Skill\n" +
                "§c/.. <Spieler> info §eErhalte Informationen zu den Stats eines Spielers\n" +
                "§cBei <+/-Anzahl> könnt ihr zB +20 eingeben oder - um einen Punkt zu entfernen";

    }

}
