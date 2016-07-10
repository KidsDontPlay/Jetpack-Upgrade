package mrriegel.jjetpacks.init;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.jjetpacks.items.ItemJetpackRFTools;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static final ItemJetpackBase rftools = new ItemJetpackRFTools();

	public static void init() {
		if (JJetpacks.rftools)
			GameRegistry.register(rftools);
	}

	public static void initModels() {
		if (JJetpacks.rftools)
			rftools.initModel();
	}

}
