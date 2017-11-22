package mrriegel.jetplates.network;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import mrriegel.jetplates.Jetpack;
import mrriegel.jetplates.proxy.ClientProxy;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class MessageParticle extends AbstractMessage {

	public MessageParticle() {
	}

	public MessageParticle(World world) {
		Map<String, Boolean> map = world.playerEntities.stream().filter(p -> Jetpack.getJetpack(p) != null).collect(Collectors.toMap(p -> p.getName(), p -> Jetpack.getJetpack(p).active || Jetpack.getJetpack(p).hover));
		NBTHelper.setMap(nbt, "map", map);
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		Map<String, Boolean> map = NBTHelper.getMap(nbt, "map", String.class, Boolean.class);
		for (Entry<String, Boolean> e : map.entrySet()) {
			EntityPlayer p = player.world.getPlayerEntityByName(e.getKey());
			if (p != null)
				ClientProxy.activeJetpacks.put(p, e.getValue());
		}
	}

}
