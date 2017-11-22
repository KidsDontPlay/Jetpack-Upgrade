package mrriegel.jetplates.proxy;

import mrriegel.jetplates.Jetpack;
import mrriegel.jetplates.config.ConfigHandler;
import mrriegel.jetplates.items.ItemJetpackUpgrade;
import mrriegel.jetplates.network.Message2Server;
import mrriegel.jetplates.network.MessageCapaSync;
import mrriegel.jetplates.network.MessageParticle;
import mrriegel.limelib.helper.RecipeHelper;
import mrriegel.limelib.item.CommonItem;
import mrriegel.limelib.network.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public static final CommonItem upgrade = new ItemJetpackUpgrade();

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.refreshConfig(event.getSuggestedConfigurationFile());
		Jetpack.register();
		upgrade.registerItem();
		RecipeHelper.addShapedRecipe(new ItemStack(CommonProxy.upgrade), "gdg", "dgd", "gdg", 'g', "ingotGold", 'd', "gemDiamond");
	}

	public void init(FMLInitializationEvent event) {
		PacketHandler.registerMessage(Message2Server.class, Side.SERVER);
		PacketHandler.registerMessage(MessageCapaSync.class, Side.CLIENT);
		PacketHandler.registerMessage(MessageParticle.class, Side.CLIENT);
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

}
