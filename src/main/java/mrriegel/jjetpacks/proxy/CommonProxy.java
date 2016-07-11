package mrriegel.jjetpacks.proxy;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.config.ConfigHandler;
import mrriegel.jjetpacks.init.CraftingRecipes;
import mrriegel.jjetpacks.init.ModItems;
import mrriegel.jjetpacks.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy implements IGuiHandler {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.refreshConfig(event.getSuggestedConfigurationFile());
		if (Loader.isModLoaded("rftools"))
			JJetpacks.rftools = true;
		if (Loader.isModLoaded("EnderIO"))
			JJetpacks.eio = true;
		if (Loader.isModLoaded("Botania"))
			JJetpacks.botania = true;
		if (Loader.isModLoaded("actuallyadditions"))
			JJetpacks.actually = true;
		if (Loader.isModLoaded("Botania"))
			JJetpacks.botania = true;
		if (Loader.isModLoaded("BloodMagic"))
			JJetpacks.blood = true;
		if (Loader.isModLoaded("advgenerators"))
			JJetpacks.generators = true;
		if (Loader.isModLoaded("randomthings"))
			JJetpacks.random = true;
		if (Loader.isModLoaded("roots"))
			JJetpacks.roots = true;
		if (Loader.isModLoaded("forestry"))
			JJetpacks.forestry = true;
		PacketHandler.init();
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
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

}
