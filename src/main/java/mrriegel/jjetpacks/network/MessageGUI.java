package mrriegel.jjetpacks.network;

import io.netty.buffer.ByteBuf;
import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.helper.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGUI implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<MessageGUI, IMessage> {

		@Override
		public IMessage onMessage(final MessageGUI message, final MessageContext ctx) {
			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayer p = ctx.getServerHandler().playerEntity;
					if (Util.getJetpack(p) != null)
						p.openGui(JJetpacks.instance, 0, p.worldObj, 0, 0, 0);
				}
			});
			return null;
		}

	}

}
