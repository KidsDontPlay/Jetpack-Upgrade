package mrriegel.jetplates.proxy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.lwjgl.input.Keyboard;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import mrriegel.jetplates.Jetpack;
import mrriegel.jetplates.Jetplates;
import mrriegel.jetplates.gui.GuiJetpack;
import mrriegel.jetplates.network.Message2Server;
import mrriegel.jetplates.network.Message2Server.MessageAction;
import mrriegel.limelib.gui.GuiDrawer;
import mrriegel.limelib.gui.element.GuiElement;
import mrriegel.limelib.helper.ColorHelper;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientProxy extends CommonProxy {

	public static final KeyBinding hover = new KeyBinding("Hover", Keyboard.KEY_C, Jetplates.MODNAME);
	public static final KeyBinding gui = new KeyBinding("GUI", Keyboard.KEY_G, Jetplates.MODNAME);
	public static Object2BooleanMap<EntityPlayer> activeJetpacks = new Object2BooleanOpenHashMap<EntityPlayer>();
	private static Minecraft mc;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		CommonProxy.upgrade.initModel();
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
				if (p.getDistance(mc.player) < 32 && activeJetpacks.getBoolean(p)) {
					Vec3d vec = mc.player.getPositionVector();
					vec = getPointUsingAnglesRange(vec, p.rotationYaw - 180f, 0, .55f);
					for (int i = 0; i < 3; i++)
						world.spawnParticle(EnumParticleTypes.FLAME, vec.x + (world.rand.nextDouble() - .5), vec.y, vec.z + (world.rand.nextDouble() - .5), 0, -.5, 0, 0);
				}
			}
		}
	}

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent event) {
		if (mc.inGameHasFocus)
			if (hover.isPressed()) {
				Jetpack jp;
				if ((jp = Jetpack.getJetpack(mc.player)) != null) {
					jp.hover ^= true;
					PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "hover", jp.hover), MessageAction.HOVER));
				}
			} else if (gui.isPressed()) {
				if (Jetpack.getJetpack(mc.player) != null) {
					GuiDrawer.openGui(new GuiJetpack());
				}
			}
	}

	@SubscribeEvent
	public void renderTooltip(RenderTooltipEvent.PostText event) {
		ItemStack hoveredStack = event.getStack();
		if (hoveredStack.isEmpty())
			return;
		if (mc.currentScreen == null)
			return;
		Jetpack jp;
		if ((jp = Jetpack.getJetpack(hoveredStack)) == null || jp.getEnergy() == null || !jp.installed)
			return;
		int toolTipY = event.getY();
		int toolTipX = event.getX();
		int toolTipW = event.getWidth();
		int toolTipH = event.getHeight();
		GuiDrawer drawer = new GuiDrawer(0, 0, 0, 0, 0);
		int width = toolTipW + 8;
		int color = 0xffee2233;
		double percent = Math.min(1., (jp.getFuel() / (double) jp.getMaxFuel()));
		drawer.drawColoredRectangle(toolTipX - 4, toolTipY + toolTipH + 4, width, 4, 0xff222222);
		//		drawer.drawColoredRectangle(toolTipX - 4, toolTipY + toolTipH + 4, (int) (width * (jp.getFuel() / (double) jp.getMaxFuel())), 4, color);
		drawer.drawColoredRectangle(toolTipX - 4, toolTipY + toolTipH + 5, (int) (width * percent), 1, color);
		drawer.drawColoredRectangle(toolTipX - 4, toolTipY + toolTipH + 6, (int) (width * percent), 1, ColorHelper.brighter(color, .15));
		drawer.drawColoredRectangle(toolTipX - 4, toolTipY + toolTipH + 7, (int) (width * percent), 1, ColorHelper.brighter(color, .3));
		drawer.drawFrame(toolTipX - 4, toolTipY + toolTipH + 4, toolTipW + 7, 4, 1, 0xff000000);
	}

	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.TEXT) {
			Jetpack jp = Jetpack.getJetpack(mc.player);
			if (jp != null && jp.installed && !jp.hideGui) {
				List<String> lis = jp.getTooltip();
				GuiField field = null;
				switch (jp.guiPos) {
				case TOPLEFT:
					field = new GuiField(0, 7, 7, lis);
					break;
				case TOP:
					field = new GuiField(0, 0, 7, lis);
					field.x = event.getResolution().getScaledWidth() / 2 - field.width / 2;
					break;
				case TOPRIGHT:
					field = new GuiField(0, 0, 7, lis);
					field.x = event.getResolution().getScaledWidth() - 7 - field.width;
					break;
				case MIDLEFT:
					field = new GuiField(0, 7, 0, lis);
					field.y = event.getResolution().getScaledHeight() / 2 - field.height / 2;
					break;
				case MIDRIGHT:
					field = new GuiField(0, 0, 0, lis);
					field.x = event.getResolution().getScaledWidth() - 7 - field.width;
					field.y = event.getResolution().getScaledHeight() / 2 - field.height / 2;
					break;
				case BOTTOMLEFT:
					field = new GuiField(0, 7, 0, lis);
					field.y = event.getResolution().getScaledHeight() - 7 - field.height;
					break;
				case BOTTOMRIGHT:
					field = new GuiField(0, 7, 0, lis);
					field.y = event.getResolution().getScaledHeight() - 7 - field.height;
					field.x = event.getResolution().getScaledWidth() - 7 - field.width;
					break;
				}
				if (field != null)
					field.draw(0, 0);
			}
		}
	}

	public Vec3d getPointUsingAnglesRange(Vec3d start, float yaw, float pitch, float range) {
		double coordX = start.x + -MathHelper.sin(yaw / 180.0F * (float) Math.PI) * MathHelper.cos(pitch / 180.0F * (float) Math.PI) * range;
		double coordY = start.y + -MathHelper.sin(pitch / 180.0F * (float) Math.PI) * range;
		double coordZ = start.z + MathHelper.cos(yaw / 180.0F * (float) Math.PI) * MathHelper.cos(pitch / 180.0F * (float) Math.PI) * range;
		return new Vec3d(coordX, coordY, coordZ);
	}

	static class GuiField extends GuiElement {
		final List<String> strings;

		public GuiField(int id, int x, int y, List<String> strings) {
			super(id, x, y, 0, 0);
			this.strings = Optional.ofNullable(strings).orElse(Arrays.asList(TextFormatting.DARK_RED + "ERROR"));
			int longest = strings.stream().mapToInt(s -> mc.fontRenderer.getStringWidth(s)).max().getAsInt();
			width = longest + 4;
			height = (mc.fontRenderer.FONT_HEIGHT + 2) * strings.size() + 2;
		}

		@Override
		public void draw(int mouseX, int mouseY) {
			drawer.drawColoredRectangle(x, y, width, height, 0x461da1f2);
			drawer.drawFrame(x, y, width, height, 1, 0x46000000);
			int w = 2 + y;
			for (String s : strings) {
				mc.fontRenderer.drawString(s, x + 2, w, 0xffeeeeee, true);
				w += mc.fontRenderer.FONT_HEIGHT + 2;
			}
		}

	}

}
