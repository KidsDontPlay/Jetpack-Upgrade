package mrriegel.jetplates.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	private static Configuration config;

	public static void refreshConfig(File file) {
		//no config yet
		if ("".isEmpty())
			return;
		config = new Configuration(file);

		if (config.hasChanged())
			config.save();

	}

}
