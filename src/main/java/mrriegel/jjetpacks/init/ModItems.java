package mrriegel.jjetpacks.init;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.config.ConfigHandler;
import mrriegel.jjetpacks.items.ItemArmorUpgrade;
import mrriegel.jjetpacks.items.ItemBelt;
import mrriegel.jjetpacks.items.ItemJetpackActually;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.jjetpacks.items.ItemJetpackBlood;
import mrriegel.jjetpacks.items.ItemJetpackBotania;
import mrriegel.jjetpacks.items.ItemJetpackEIO;
import mrriegel.jjetpacks.items.ItemJetpackRFTools;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static Item icon = null;

	public static Item belt;
	public static Item armor;

	public static ItemJetpackBase rftools;
	public static ItemJetpackBase botania;
	public static ItemJetpackBase actually;
	public static ItemJetpackBase blood;
	public static ItemJetpackBase eio;

	public static void init() {
		GameRegistry.register(belt = new ItemBelt());
		GameRegistry.register(armor = new ItemArmorUpgrade());

		if (JJetpacks.rftools && ConfigHandler.rftools) {
			GameRegistry.register(rftools = new ItemJetpackRFTools());
			if (icon == null)
				icon = rftools;
		}
		if (JJetpacks.botania && ConfigHandler.botania) {
			GameRegistry.register(botania = new ItemJetpackBotania());
			if (icon == null)
				icon = botania;
		}
		if (JJetpacks.actually && ConfigHandler.actually) {
			GameRegistry.register(actually = new ItemJetpackActually());
			if (icon == null)
				icon = actually;
		}
		if (JJetpacks.blood && ConfigHandler.blood) {
			GameRegistry.register(blood = new ItemJetpackBlood());
			if (icon == null)
				icon = blood;
		}
		if (JJetpacks.eio && ConfigHandler.eio) {
			GameRegistry.register(eio = new ItemJetpackEIO());
			if (icon == null)
				icon = eio;
		}

	}

	public static void initModels() {
		ModelLoader.setCustomModelResourceLocation(belt, 0, new ModelResourceLocation(belt.getRegistryName(), "inventory"));
		for (int i = 0; i < 3; i++)
			ModelLoader.setCustomModelResourceLocation(armor, i, new ModelResourceLocation(armor.getRegistryName().toString() + "_" + i, "inventory"));
		if (JJetpacks.rftools && ConfigHandler.rftools)
			rftools.initModel();
		if (JJetpacks.botania && ConfigHandler.botania)
			botania.initModel();
		if (JJetpacks.actually && ConfigHandler.actually)
			actually.initModel();
		if (JJetpacks.blood && ConfigHandler.blood)
			blood.initModel();
		if (JJetpacks.eio && ConfigHandler.eio)
			eio.initModel();
	}

}
