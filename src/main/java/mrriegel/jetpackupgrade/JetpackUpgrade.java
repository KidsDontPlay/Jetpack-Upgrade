package mrriegel.jetpackupgrade;

import mrriegel.jetpackupgrade.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = JetpackUpgrade.MODID, name = JetpackUpgrade.MODNAME, version = JetpackUpgrade.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", dependencies = "required-after:limelib@[1.7.8,)")
public class JetpackUpgrade {
	public static final String MODID = "jetpackupgrade";
	public static final String VERSION = "1.0.0";
	public static final String MODNAME = "JetpackUpgrade";

	@Instance(JetpackUpgrade.MODID)
	public static JetpackUpgrade instance;

	@SidedProxy(clientSide = "mrriegel.jetpackupgrade.proxy.ClientProxy", serverSide = "mrriegel.jetpackupgrade.proxy.CommonProxy")
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
