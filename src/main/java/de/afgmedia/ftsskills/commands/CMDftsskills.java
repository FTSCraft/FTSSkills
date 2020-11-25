package de.afgmedia.ftsskills.commands;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillUser;
import de.afgmedia.ftsskills.skillsystem.gui.GuiType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDftsskills implements CommandExecutor {

    private Skills plugin;

    public CMDftsskills(Skills plugin) {
        this.plugin = plugin;
        plugin.getCommand("ftsskills").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if(!(cs instanceof Player)) {

            return true;
        }

        Player p = (Player) cs;

        SkillUser user = plugin.getManager().getUser(p);

        if(args.length == 1 || args.length == 2) {
            if(args[0].equalsIgnoreCase("verlernen")) {

                for (int i = 0; i < 10; i++) {
                    p.sendMessage(" ");
                }

                if(args.length == 1) {
                    p.sendMessage(Values.PREFIX + "Welchen Skill willst du verlernen?");

                    for (Skill skill : user.getSkills()) {

                        TextComponent message = new TextComponent("-" + skill.getName());
                        message.setColor(ChatColor.AQUA);
                        message.setBold(true);
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftsskills verlernen " + skill.getName().replace(" ", "_").replace("§", "&")));

                        p.spigot().sendMessage(message);

                    }

                    p.sendMessage(Values.PREFIX + "Vergiss nicht, wenn du einen Skill verlernen möchtest, verlierst du 5 Level, verlierst den jeweiligen Skill und bekommst dafür einen Skillpunkt!");
                } else {

                    String skillName = args[1];

                    Skill skill = plugin.getManager().getSkillByName(skillName.replace("_", " ").replace("&", "§"));

                    if(skill != null) {

                        if(user.unlearnSkill(skill)) {

                            p.sendMessage(Values.PREFIX + "Du hast nun den Skill " + skill.getName() + " §7verlernt und 5 Level geopfert und dafür einen Skillpunkt wiedererlernt!");

                        } else {

                            p.sendMessage(Values.PREFIX + "Da ist was schiefgelaufen! Schau nach, ob du " + Values.LEVEL_LOST_TO_UNLEARN_SKILL + " Level hast!");

                        }

                    }

                }
            } else if(args[0].equalsIgnoreCase("info")) {

                for (int i = 0; i < 10; i++) {
                    p.sendMessage(" ");
                }

                p.sendMessage(Values.PREFIX + "Level: §e" + user.getLevel());
                if(user.getHighestLevel() != user.getLevel()) {
                    p.sendMessage(Values.PREFIX + "Höchstes Level: §e" + user.getHighestLevel());
                    p.sendMessage(Values.PREFIX + "Wenn dein Level nicht so hoch ist wie dein höchstes, kannst du, bis du es wieder erriecht hast, keine Skillpunkte bekommen!");
                }
                p.sendMessage(Values.PREFIX + "XP bis zum nächsten LevelUp: §e" + (plugin.getManager().getLevelManager().getNeededXPForLevel(user.getLevel()) - user.getExperience()));
                p.sendMessage(Values.PREFIX + "Skillpoints: §e" + user.getSkillPoints());
                p.sendMessage(Values.PREFIX + "Skills: ");
                for (Skill skill : user.getSkills()) {
                    p.sendMessage("§e- " + skill.getName());
                }

            }

            return true;

        }

        if(p == null)
            System.out.println("p == null");
        if(plugin == null)
            System.out.println("pl == null");
        if(plugin.getMainMenuGUI() == null)
            System.out.println("maingui == null");
        if(plugin.getMainMenuGUI().getInventory() == null)
            System.out.println("inv == null");

        p.openInventory(plugin.getMainMenuGUI().getInventory());


        user.setInventoryType(GuiType.MAIN_MENU);

        return false;
    }
}
