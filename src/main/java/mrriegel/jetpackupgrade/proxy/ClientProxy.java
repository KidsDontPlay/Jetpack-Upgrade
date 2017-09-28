package mrriegel.jetpackupgrade.proxy;

import org.lwjgl.input.Keyboard;

import mrriegel.jetpackupgrade.Jetpack;
import mrriegel.jetpackupgrade.JetpackUpgrade;
import mrriegel.jetpackupgrade.gui.GuiJetpack;
import mrriegel.jetpackupgrade.init.ModItems;
import mrriegel.jetpackupgrade.network.Message2Server;
import mrriegel.jetpackupgrade.network.Message2Server.MessageAction;
import mrriegel.limelib.gui.GuiDrawer;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientProxy extends CommonProxy {

	public static final KeyBinding hover = new KeyBinding("Hover", Keyboard.KEY_C, JetpackUpgrade.MODNAME);
	public static final KeyBinding gui = new KeyBinding("GUI", Keyboard.KEY_G, JetpackUpgrade.MODNAME);
	private static Minecraft mc;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ModItems.initModels();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		mc = Minecraft.getMinecraft();
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
		World world = mc.world;
		if (world != null) {
			for (EntityPlayer p : world.playerEntities) {
				if (p.getDistance(mc.player) < 32) {
					Jetpack jp = Jetpack.getJetpack(mc.player);
					Vec3d vec = mc.player.getPositionVector();
					vec = getPointUsingAnglesRange(vec, p.rotationYaw - 180f, 0, .55f);
					if (jp != null && (jp.active || jp.hover)) {
						for (int i = 0; i < 3; i++)
							world.spawnParticle(EnumParticleTypes.FLAME, vec.x + (world.rand.nextDouble() - .5), vec.y, vec.z + (world.rand.nextDouble() - .5), 0, -.5, 0, 0);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent event) {
		if (hover.isPressed() && Minecraft.getMinecraft().inGameHasFocus) {
			Jetpack jp;
			if ((jp = Jetpack.getJetpack(mc.player)) != null) {
				jp.hover ^= true;
				PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "hover", jp.hover), MessageAction.HOVER));
			}
		}
		if (gui.isPressed() && Minecraft.getMinecraft().inGameHasFocus) {
			if (Jetpack.getJetpack(mc.player) != null) {
				GuiDrawer.openGui(new GuiJetpack());
			}
		}
	}

	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.TEXT) {
			Jetpack jp = Jetpack.getJetpack(mc.player);
			if (jp != null)
				mc.fontRenderer.drawString("Energy: " + jp.getFuel(), 10, 130, 0xff000000);
		}
	}

	public Vec3d getPointUsingAnglesRange(Vec3d start, float yaw, float pitch, float range) {
		double coordX = start.x + -MathHelper.sin(yaw / 180.0F * (float) Math.PI) * MathHelper.cos(pitch / 180.0F * (float) Math.PI) * range;
		double coordY = start.y + -MathHelper.sin(pitch / 180.0F * (float) Math.PI) * range;
		double coordZ = start.z + MathHelper.cos(yaw / 180.0F * (float) Math.PI) * MathHelper.cos(pitch / 180.0F * (float) Math.PI) * range;
		return new Vec3d(coordX, coordY, coordZ);
	}

}
