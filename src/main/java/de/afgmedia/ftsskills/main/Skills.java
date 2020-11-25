package de.afgmedia.ftsskills.main;

import de.afgmedia.ftsskills.commands.CMDftsskills;
import de.afgmedia.ftsskills.commands.CMDftsskillsadmin;
import de.afgmedia.ftsskills.commands.CMDinspizieren;
import de.afgmedia.ftsskills.listeners.*;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillManager;
import de.afgmedia.ftsskills.skillsystem.gui.MainMenuGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Skills extends JavaPlugin {

    private SkillManager manager;

    //Sound.UI_TOAST_CHALLENGE_COMPLETE - LevelUp

    private MainMenuGUI mainMenuGUI;

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {

    }

    private void init() {
        this.manager = new SkillManager(this);
        manager.getDataManager().loadSkills();
        manager.getDataManager().loadCategories();

        manager.getDataManager().loadCategoryGuis();
        mainMenuGUI = new MainMenuGUI(this, new File(getDataFolder() + "//gui//main.yml"));

        new CraftListener(this);
        new EnchantListener(this);
        new ForgeListener(this);
        new BreedListener(this);
        new FishListener(this);
        new BreakListener(this);
        new KillListener(this);
        new JoinListener(this);
        new GuiListener(this);
        new ExperienceListener(this);
        new FurnaceExtractListener(this);
        new QuitListener(this);

        new CMDftsskills(this);
        new CMDftsskillsadmin(this);
        new CMDinspizieren(this);

    }

    public SkillManager getManager() {
        return manager;
    }

    public MainMenuGUI getMainMenuGUI() {
        return mainMenuGUI;
    }


}
