package mrriegel.jjetpacks.network;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class MessageGUI extends AbstractMessage<MessageGUI> {

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		if (ItemJetpackBase.getJetpack(player) != null)
			player.openGui(JJetpacks.instance, 0, player.worldObj, 0, 0, 0);
	}

}
