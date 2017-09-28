package mrriegel.jetpackupgrade.proxy;

import mrriegel.jetpackupgrade.Jetpack;
import mrriegel.jetpackupgrade.JetpackUpgrade;
import mrriegel.jetpackupgrade.config.ConfigHandler;
import mrriegel.jetpackupgrade.gui.GuiJetpack;
import mrriegel.jetpackupgrade.init.CraftingRecipes;
import mrriegel.jetpackupgrade.init.ModItems;
import mrriegel.jetpackupgrade.network.Message2Server;
import mrriegel.jetpackupgrade.network.MessageCapaSync;
import mrriegel.limelib.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.refreshConfig(event.getSuggestedConfigurationFile());
		Jetpack.register();
		ModItems.init();
		CraftingRecipes.init();

	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(JetpackUpgrade.instance, this);
		PacketHandler.registerMessage(Message2Server.class, Side.SERVER);
		PacketHandler.registerMessage(MessageCapaSync.class, Side.CLIENT);
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiJetpack();
	}

}
