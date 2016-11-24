package mrriegel.jjetpacks.network;

import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.limelib.helper.NBTStackHelper;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHover extends AbstractMessage<MessageHover> {

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		ItemStack jet = ItemJetpackBase.getJetpack(player);
		NBTStackHelper.setBoolean(jet, "hover", !NBTStackHelper.getBoolean(jet, "hover"));
	}

}
