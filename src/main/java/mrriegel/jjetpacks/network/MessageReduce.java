package mrriegel.jjetpacks.network;

import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class MessageReduce extends AbstractMessage<MessageReduce> {
	public MessageReduce() {
	}

	public MessageReduce(int amount, boolean hover) {
		nbt.setInteger("amount", amount);
		nbt.setBoolean("hover", hover);
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		player.fallDistance = -1;
		ItemStack jet = ItemJetpackBase.getJetpack(player);
		((ItemJetpackBase) jet.getItem()).reduceFuel(jet, nbt.getInteger("amount"), nbt.getBoolean("hover"), false);
	}

}
