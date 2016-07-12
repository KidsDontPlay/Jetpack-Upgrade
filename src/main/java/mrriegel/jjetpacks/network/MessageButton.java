package mrriegel.jjetpacks.network;

import io.netty.buffer.ByteBuf;
import mrriegel.jjetpacks.helper.NBTHelper;
import mrriegel.jjetpacks.helper.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageButton implements IMessage {
	double hspeed, vspeed, acce;

	public MessageButton() {
	}

	public MessageButton(double hspeed, double vspeed, double acce) {
		super();
		this.hspeed = hspeed;
		this.vspeed = vspeed;
		this.acce = acce;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.hspeed = buf.readDouble();
		this.vspeed = buf.readDouble();
		this.acce = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(this.hspeed);
		buf.writeDouble(this.vspeed);
		buf.writeDouble(this.acce);
	}

	public static class Handler implements IMessageHandler<MessageButton, IMessage> {

		@Override
		public IMessage onMessage(final MessageButton message, final MessageContext ctx) {
			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {

				@Override
				public void run() {
					EntityPlayer p = ctx.getServerHandler().playerEntity;
					ItemStack jet = Util.getJetpack(p);
					NBTHelper.setDouble(jet, "hspeed", message.hspeed);
					NBTHelper.setDouble(jet, "vspeed", message.vspeed);
					NBTHelper.setDouble(jet, "acce", message.acce);
				}
			});
			return null;
		}

	}

}
