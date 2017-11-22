package mrriegel.jetplates.network;

import mrriegel.jetplates.Jetpack;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class MessageCapaSync extends AbstractMessage {

	public MessageCapaSync() {
	}

	public MessageCapaSync(EntityPlayer player) {
		Jetpack jp = Jetpack.getJetpack(player);
		if (jp == null) {
			shouldSend = false;
			return;
		}
		//		nbt = jp.serializeNBT();
		nbt = jp.writeToSyncNBT(nbt);
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		Jetpack jp = Jetpack.getJetpack(player);
		if (jp == null)
			return;
		//		jp.deserializeNBT(nbt);
		jp.readFromSyncNBT(nbt);
	}

}
