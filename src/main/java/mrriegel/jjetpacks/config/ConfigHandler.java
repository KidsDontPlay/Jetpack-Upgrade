package mrriegel.jjetpacks.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;

	public static boolean rftools, eio, botania, actually, blood;

	public static void refreshConfig(File file) {
		config = new Configuration(file);
		config.load();

		rftools = config.getBoolean("rftools", Configuration.CATEGORY_GENERAL, true, "RFTools Jetpack");
		eio = config.getBoolean("eio", Configuration.CATEGORY_GENERAL, true, "Ender IO Jetpack");
		botania = config.getBoolean("botania", Configuration.CATEGORY_GENERAL, true, "Botania Jetpack");
		actually = config.getBoolean("actually", Configuration.CATEGORY_GENERAL, true, "Actually Additions Jetpack");
		blood = config.getBoolean("blood", Configuration.CATEGORY_GENERAL, true, "Blood Magic Jetpack");

		if (config.hasChanged()) {
			config.save();
		}
	}

}
