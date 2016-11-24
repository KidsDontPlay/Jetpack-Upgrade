package mrriegel.jjetpacks.network;

import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.limelib.helper.NBTStackHelper;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class MessageButton extends AbstractMessage<MessageButton> {

	public MessageButton() {
	}

	public MessageButton(double hspeed, double vspeed, double acce) {
		nbt.setDouble("hspeed", hspeed);
		nbt.setDouble("vspeed", vspeed);
		nbt.setDouble("acce", acce);
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		ItemStack jet = ItemJetpackBase.getJetpack(player);
		NBTStackHelper.setDouble(jet, "hspeed", nbt.getDouble("hspeed"));
		NBTStackHelper.setDouble(jet, "vspeed", nbt.getDouble("vspeed"));
		NBTStackHelper.setDouble(jet, "acce", nbt.getDouble("acce"));
	}

}
