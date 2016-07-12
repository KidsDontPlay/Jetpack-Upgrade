package mrriegel.jjetpacks.network;

import mrriegel.jjetpacks.JJetpacks;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(JJetpacks.MODID);

	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(MessageReduce.Handler.class, MessageReduce.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessageHover.Handler.class, MessageHover.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessageGUI.Handler.class, MessageGUI.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessageButton.Handler.class, MessageButton.class, id++, Side.SERVER);
	}
}
