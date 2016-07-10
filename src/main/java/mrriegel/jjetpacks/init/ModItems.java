package mrriegel.jjetpacks.init;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.items.ItemJetpackActually;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.jjetpacks.items.ItemJetpackBotania;
import mrriegel.jjetpacks.items.ItemJetpackRFTools;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static final ItemJetpackBase rftools = new ItemJetpackRFTools();
	public static final ItemJetpackBase botania = new ItemJetpackBotania();
	public static final ItemJetpackBase actually = new ItemJetpackActually();

	public static void init() {
		if (JJetpacks.rftools)
			GameRegistry.register(rftools);
		if(JJetpacks.botania)
			GameRegistry.register(botania);
		if(JJetpacks.actually)
			GameRegistry.register(actually);
	}

	public static void initModels() {
		if (JJetpacks.rftools)
			rftools.initModel();
		if(JJetpacks.botania)
			botania.initModel();
		if(JJetpacks.actually)
			actually.initModel();
	}

}
