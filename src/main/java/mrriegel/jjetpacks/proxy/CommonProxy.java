package mrriegel.jjetpacks.proxy;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.config.ConfigHandler;
import mrriegel.jjetpacks.gui.ContainerJetpack;
import mrriegel.jjetpacks.gui.GuiJetpack;
import mrriegel.jjetpacks.init.CraftingRecipes;
import mrriegel.jjetpacks.init.ModItems;
import mrriegel.jjetpacks.network.MessageButton;
import mrriegel.jjetpacks.network.MessageGUI;
import mrriegel.jjetpacks.network.MessageHover;
import mrriegel.jjetpacks.network.MessageReduce;
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
		PacketHandler.registerMessage(MessageButton.class, Side.SERVER);
		PacketHandler.registerMessage(MessageGUI.class, Side.SERVER);
		PacketHandler.registerMessage(MessageHover.class, Side.SERVER);
		PacketHandler.registerMessage(MessageReduce.class, Side.SERVER);
		ModItems.init();
	}

	public void init(FMLInitializationEvent event) {
		CraftingRecipes.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(JJetpacks.instance, this);
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerJetpack(player.inventory);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiJetpack(new ContainerJetpack(player.inventory), player.inventory);
	}

}
