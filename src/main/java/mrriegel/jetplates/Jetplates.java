package mrriegel.jetplates;

import mrriegel.jetplates.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Jetplates.MODID, name = Jetplates.MODNAME, version = Jetplates.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", dependencies = "required-after:limelib@[1.7.8,)")
public class Jetplates {
	public static final String MODID = "jetplates";
	public static final String VERSION = "1.0.0";
	public static final String MODNAME = "Jetplates";

	@Instance(Jetplates.MODID)
	public static Jetplates instance;

	@SidedProxy(clientSide = "mrriegel.jetplates.proxy.ClientProxy", serverSide = "mrriegel.jetplates.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
