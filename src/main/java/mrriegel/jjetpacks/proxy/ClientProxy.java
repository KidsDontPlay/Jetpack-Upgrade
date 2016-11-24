package mrriegel.jjetpacks.proxy;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.init.ModItems;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.jjetpacks.network.MessageGUI;
import mrriegel.jjetpacks.network.MessageHover;
import mrriegel.limelib.helper.NBTStackHelper;
import mrriegel.limelib.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {

	public static final KeyBinding hover = new KeyBinding("Hover", Keyboard.KEY_C, JJetpacks.MODNAME);
	public static final KeyBinding gui = new KeyBinding("GUI", Keyboard.KEY_G, JJetpacks.MODNAME);

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ModItems.initModels();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(this);
		ClientRegistry.registerKeyBinding(hover);
		ClientRegistry.registerKeyBinding(gui);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@SubscribeEvent
	public void tick(ClientTickEvent e) {
		World world = Minecraft.getMinecraft().theWorld;
		if (world != null) {
			for (EntityPlayer p : world.playerEntities) {
				if (p.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) < 32) {
					ItemStack chest = ItemJetpackBase.getJetpack(Minecraft.getMinecraft().thePlayer);
					Vec3d vec = p.getPositionVector();
					vec = getPointUsingAnglesRange(vec, p.rotationYaw - 180f, 0, .55f);
					if (chest != null && chest.getItem() instanceof ItemJetpackBase && (NBTStackHelper.getBoolean(chest, "active") || NBTStackHelper.getBoolean(chest, "hover"))) {
						for (int i = 0; i < 3; i++)
							world.spawnParticle(EnumParticleTypes.FLAME, vec.xCoord + (world.rand.nextDouble() - .5), vec.yCoord, vec.zCoord + (world.rand.nextDouble() - .5), 0, -.5, 0, 0);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent e) {
		if (hover.isPressed() && Minecraft.getMinecraft().inGameHasFocus) {
			ItemStack chest = ItemJetpackBase.getJetpack(Minecraft.getMinecraft().thePlayer);
			if (chest != null && chest.getItem() instanceof ItemJetpackBase) {
				PacketHandler.sendToServer(new MessageHover());
			}
		}
		if (gui.isPressed() && Minecraft.getMinecraft().inGameHasFocus) {
			ItemStack chest = ItemJetpackBase.getJetpack(Minecraft.getMinecraft().thePlayer);
			if (chest != null && chest.getItem() instanceof ItemJetpackBase) {
				PacketHandler.sendToServer(new MessageGUI());
			}
		}
	}

	public Vec3d getPointUsingAnglesRange(Vec3d start, float yaw, float pitch, float range) {
		double coordX = start.xCoord + -MathHelper.sin(yaw / 180.0F * (float) Math.PI) * MathHelper.cos(pitch / 180.0F * (float) Math.PI) * range;
		double coordY = start.yCoord + -MathHelper.sin(pitch / 180.0F * (float) Math.PI) * range;
		double coordZ = start.zCoord + MathHelper.cos(yaw / 180.0F * (float) Math.PI) * MathHelper.cos(pitch / 180.0F * (float) Math.PI) * range;
		return new Vec3d(coordX, coordY, coordZ);
	}

}
