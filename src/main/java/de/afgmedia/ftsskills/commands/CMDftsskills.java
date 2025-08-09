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

    private final Skills plugin;

    public CMDftsskills(Skills plugin) {
        this.plugin = plugin;
        plugin.getCommand("ftsskills").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if (!(cs instanceof Player p)) {
            return true;
        }

        SkillUser user = plugin.getManager().getUser(p);

        if (args.length == 1 || args.length == 2) {
            if (args[0].equalsIgnoreCase("verlernen")) {

                for (int i = 0; i < 10; i++) {
                    p.sendMessage(" ");
                }

                if (args.length == 1) {
                    sendSkillRemoveList(p, user);
                } else {
                    String skillArg = args[1];
                    removeSkill(skillArg, user, p);
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                sendSkillInfo(p, user);
            }
            return true;
        }

        p.openInventory(plugin.getMainMenuGUI().getInventory());

        user.setInventoryType(GuiType.MAIN_MENU);

        return false;
    }

    private void sendSkillRemoveList(Player p, SkillUser user) {
        p.sendMessage(Values.PREFIX + "Welchen Skill willst du verlernen?");

        for (Skill skill : user.getSkills()) {

            TextComponent message = new TextComponent("-" + skill.getName());
            message.setColor(ChatColor.AQUA);
            message.setBold(true);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftsskills verlernen " + skill.getName().replace(" ", "_").replace("§", "&")));

            p.spigot().sendMessage(message);

        }

        p.sendMessage(Values.PREFIX + "Vergiss nicht, wenn du einen Skill verlernen möchtest, verlierst du" + Values.SKILL_UNLEARN_COST + " Taler (bis auf für Reisende), verlierst den jeweiligen Skill und bekommst dafür einen Skillpunkt!");
    }

    private void removeSkill(String skillName, SkillUser user, Player p) {
        Skill skill = plugin.getManager().getSkillByName(skillName.replace("_", " ").replace("&", "§"));
        if (skill != null) {

            if (user.unlearnSkill(skill)) {
                p.sendMessage(Values.PREFIX + "Du hast nun den Skill " + skill.getName() + " §7verlernt und " + Values.SKILL_UNLEARN_COST + " Taler ausgegeben und dafür einen Skillpunkt erlangt! (bis auf du bist Reisender, dann hast du nichts ausgegeben)");
            } else {
                p.sendMessage(Values.PREFIX + "Da ist was schiefgelaufen! Schau nach, ob du " + Values.SKILL_UNLEARN_COST + " Gold hast!");
            }

        }
    }

    private void sendSkillInfo(Player p, SkillUser user) {
        for (int i = 0; i < 10; i++) {
            p.sendMessage(" ");
        }

        p.sendMessage(Values.PREFIX + "Level: §e" + user.getLevel());
        if (user.getHighestLevel() != user.getLevel()) {
            p.sendMessage(Values.PREFIX + "Höchstes Level: §e" + user.getHighestLevel());
            p.sendMessage(Values.PREFIX + "Wenn dein Level nicht so hoch ist wie dein höchstes, kannst du, bis du es wieder erriecht hast, keine Skillpunkte bekommen!");
        }
        if (user.getLevel() < Values.MAX_LEVEL) {
            p.sendMessage(Values.PREFIX + "XP bis zum nächsten LevelUp: §e" + (plugin.getManager().getLevelManager().getNeededXPForLevel(user.getLevel()) - user.getExperience()));
        } else
            p.sendMessage(Values.PREFIX + "XP bis zum nächsten LevelUp: §eMAX");
        p.sendMessage(Values.PREFIX + "Skillpoints: §e" + user.getSkillPoints());
        p.sendMessage(Values.PREFIX + "Skills: ");
        for (Skill skill : user.getSkills()) {
            p.sendMessage("§e- " + skill.getName());
        }
    }
}
