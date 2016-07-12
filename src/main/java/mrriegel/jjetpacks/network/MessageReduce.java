package mrriegel.jjetpacks.network;

import io.netty.buffer.ByteBuf;
import mrriegel.jjetpacks.helper.Util;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageReduce implements IMessage {
	int amount;
	boolean hover;

	public MessageReduce() {
	}

	public MessageReduce(int amount, boolean hover) {
		this.amount = amount;
		this.hover = hover;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.amount = buf.readInt();
		this.hover = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.amount);
		buf.writeBoolean(this.hover);
	}

	public static class Handler implements IMessageHandler<MessageReduce, IMessage> {

		@Override
		public IMessage onMessage(final MessageReduce message, final MessageContext ctx) {
			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {

				@Override
				public void run() {
					EntityPlayer p = ctx.getServerHandler().playerEntity;
					p.fallDistance = -1;
					ItemStack jet = Util.getJetpack(p);
					((ItemJetpackBase) jet.getItem()).reduceFuel(jet, message.amount, message.hover, false);
				}
			});
			return null;
		}

	}

}
