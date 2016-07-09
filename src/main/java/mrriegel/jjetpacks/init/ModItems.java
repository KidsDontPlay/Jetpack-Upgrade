package mrriegel.jjetpacks.init;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.items.ItemJetpackRFTools;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
	
	public static final Item rftools=new ItemJetpackRFTools();

	public static void init() {
		if(JJetpacks.rftools)
			GameRegistry.register(rftools);
	}

}
