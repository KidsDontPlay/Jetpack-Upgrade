package mrriegel.jjetpacks.network;

import io.netty.buffer.ByteBuf;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageReduce implements IMessage {
	int amount;

	public MessageReduce() {
	}

	public MessageReduce(int amount) {
		this.amount = amount;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.amount = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.amount);
	}

	public static class Handler implements IMessageHandler<MessageReduce, IMessage> {

		@Override
		public IMessage onMessage(final MessageReduce message, final MessageContext ctx) {
			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {

				@Override
				public void run() {
					ctx.getServerHandler().playerEntity.fallDistance = -1;
					ItemStack jet = ctx.getServerHandler().playerEntity.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
					((ItemJetpackBase) jet.getItem()).reduceFuel(jet, message.amount, false);
				}
			});
			return null;
		}

	}

}
