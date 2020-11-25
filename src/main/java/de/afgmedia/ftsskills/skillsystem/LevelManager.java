package de.afgmedia.ftsskills.skillsystem;

import de.afgmedia.ftsskills.main.Skills;

import java.util.HashMap;

public class LevelManager {

    HashMap<Integer, Double> levels = new HashMap<>();

    private Skills plugin;

    private void loadLevels(int maxlevel) {

        for (int i = 0; i < maxlevel; i++) {
            levels.put(i, getNeededXPForLevel(i));
        }

    }

    public double getNeededXPForLevel(int level) {

        if(level >= 0 && level <= 15) {

            //return Math.pow(level, 2) + 6 * level;

            return 2 * level + 7;

        }

        if(level >= 16 && level <= 30) {

            //return 2.5 * Math.pow(level, 2) - 40.5 * level + 360;

            return 5 * level - 38;

        }

        if(level > 30) {

            //return 4.5 * Math.pow(level, 2) - 162.5 * level + 2220;

            return 9 * level - 158;

        }

        return -1;

    }

}
