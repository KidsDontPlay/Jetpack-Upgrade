package mrriegel.jetpackupgrade.network;

import mrriegel.jetpackupgrade.Jetpack;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class Message2Server extends AbstractMessage {

	public Message2Server() {
	}

	public Message2Server(NBTTagCompound nbt, MessageAction action) {
		this.nbt = NBTHelper.set(nbt, "action", action);
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		Jetpack jp = Jetpack.getJetpack(player);
		if (jp == null)
			return;
		switch (NBTHelper.get(nbt, "action", MessageAction.class)) {
		case GUI:
			jp.acce = NBTHelper.get(nbt, "acce", Double.class);
			jp.HSpeed = NBTHelper.get(nbt, "hspeed", Double.class);
			jp.VSpeed = NBTHelper.get(nbt, "vspeed", Double.class);
			break;
		case HOVER:
			jp.hover = NBTHelper.get(nbt, "hover", boolean.class);
			break;
		case REDUCE:
			jp.reduceFuel(NBTHelper.get(nbt, "amount", Integer.class), false);
			break;
		}
	}

	public enum MessageAction {
		HOVER, REDUCE, GUI;
	}

}
