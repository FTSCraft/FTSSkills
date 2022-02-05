package de.afgmedia.ftsskills.data;

public class Values {

    public static final String LINK = "https://forum.ftscraft.de/skills",
    COMMAND = "/ftsskills",
    PREFIX = "§7[§eFTS-Skills§7] ";
    public static final String MESSAGE_NEED_TO_SKILL = PREFIX + "§7Du musst diese Fähigkeit erst erlernen! Lese mehr darüber hier: §6" + Values.LINK,
    MESSAGE_NEED_TO_SKILL_BLOCK_LOOT  = PREFIX + "§7Um Loot aus diesem Block zu bekommen, musst du erst lernen wie das funktioniert! Lese mehr darüber hier: §6" + Values.LINK,
    MESSAGE_NEED_TO_SKILL_BREED = PREFIX + "§7Du musst erst lernen wie man diese Tiere... Liebe machen lässt! Lese mehr darüber hier: §6" + Values.LINK,
    MESSAGE_NEED_TO_SKILL_FISH = PREFIX + "§7Deine Leine ist gerissen! Der Fisch war wohl zu stark für dich. Lese mehr darüber hier: §6" + Values.LINK,
    MESSAGE_NEED_TO_SKILL_MOB_LOOT = PREFIX + "§7Du musst erst lernen wie du diesen toten Körper in seine Einzelteile zerlegst! Lese mehr darüber hier: §6" + Values.LINK,
    MESSAGE_NEED_TO_SKILL_POTIONS = PREFIX + "§7Du musst erst lernen wie man diesen Braustand bentutzt! Lese mehr darüber hier: §6" + Values.LINK,

    ITEM_UNLEARN_SKILL_NAME = "§6Einen Skill verlernen",
    ITEM_INFO_NAME = "§bInfo";

    public static final String MAIN_MENU_GUI_INVENTORY_NAME = "§4Hauptmenü" + "§7",
    CATEGORY_GUI_NAME = "§4Skilltree: %s",
    CHOOSE_GUI_NAME = "§4%s erlernen";

    public static final String YES_ITEM_NAME = "§2Ja",
    BACK_ITEM_NAME = "§4Zurück";

    public static final int LEVELS_TO_SKILLPOINT = 10;
    public static final int LEVEL_LOST_TO_UNLEARN_SKILL = 5;

    public static final int MAX_LEVEL = 80;
    public static final int START_SKILLPOINTS = 2;

    public static final boolean DEBUG = false;
}
