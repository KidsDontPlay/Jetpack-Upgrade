package mrriegel.jjetpacks.network;

import io.netty.buffer.ByteBuf;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdate implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}
	
	public static class Handler implements IMessageHandler<MessageUpdate, IMessage>{

		@Override
		public IMessage onMessage(MessageUpdate message, final MessageContext ctx) {
			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {
				
				@Override
				public void run() {
					ctx.getServerHandler().playerEntity.fallDistance=-1;
					ItemStack jet=ctx.getServerHandler().playerEntity.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
					((ItemJetpackBase)jet.getItem()).reduceFuel(jet);
				}
			});
			return null;
		}
		
	}

}
