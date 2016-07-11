package mrriegel.jjetpacks.init;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.items.ItemJetpackActually;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.jjetpacks.items.ItemJetpackBlood;
import mrriegel.jjetpacks.items.ItemJetpackBotania;
import mrriegel.jjetpacks.items.ItemJetpackRFTools;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static ItemJetpackBase rftools;
	public static ItemJetpackBase botania;
	public static ItemJetpackBase actually;
	public static ItemJetpackBase blood;

	public static void init() {
		if (JJetpacks.rftools)
			GameRegistry.register(rftools = new ItemJetpackRFTools());
		if (JJetpacks.botania)
			GameRegistry.register(botania = new ItemJetpackBotania());
		if (JJetpacks.actually)
			GameRegistry.register(actually = new ItemJetpackActually());
		if (JJetpacks.blood)
			GameRegistry.register(blood = new ItemJetpackBlood());
	}

	public static void initModels() {
		if (JJetpacks.rftools)
			rftools.initModel();
		if (JJetpacks.botania)
			botania.initModel();
		if (JJetpacks.actually)
			actually.initModel();
		if (JJetpacks.blood)
			blood.initModel();
	}

}
