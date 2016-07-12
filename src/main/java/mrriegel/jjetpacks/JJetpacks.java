package mrriegel.jjetpacks;

import mrriegel.jjetpacks.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = JJetpacks.MODID, name = JJetpacks.MODNAME, version = JJetpacks.VERSION, dependencies = "required-after:Forge@[12.17.0.1957,);")
public class JJetpacks {
	public static final String MODID = "jjetpacks";
	public static final String VERSION = "1.0.0";
	public static final String MODNAME = "JJetpacks";

	@Instance(JJetpacks.MODID)
	public static JJetpacks instance;

	@SidedProxy(clientSide = "mrriegel.jjetpacks.proxy.ClientProxy", serverSide = "mrriegel.jjetpacks.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static boolean rftools, eio, botania, actually, blood;

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
