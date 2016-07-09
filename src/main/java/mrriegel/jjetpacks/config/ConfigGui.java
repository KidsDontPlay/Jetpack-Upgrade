package mrriegel.jjetpacks.config;

import mrriegel.jjetpacks.JJetpacks;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGui extends GuiConfig {
	public ConfigGui(GuiScreen parent) {
		super(parent, new ConfigElement(
				ConfigHandler.config
						.getCategory(Configuration.CATEGORY_GENERAL))
				.getChildElements(), JJetpacks.MODID, false, false,
				GuiConfig
						.getAbridgedConfigPath(ConfigHandler.config.toString()));
	}

}
