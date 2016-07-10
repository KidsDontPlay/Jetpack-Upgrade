package mrriegel.jjetpacks.network;

import io.netty.buffer.ByteBuf;
import mrriegel.jjetpacks.helper.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHover implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<MessageHover, IMessage> {

		@Override
		public IMessage onMessage(final MessageHover message, final MessageContext ctx) {
			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayer p = ctx.getServerHandler().playerEntity;
					ItemStack jet = p.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
					NBTHelper.setBoolean(jet, "hover", !NBTHelper.getBoolean(jet, "hover"));
				}
			});
			return null;
		}

	}

}
