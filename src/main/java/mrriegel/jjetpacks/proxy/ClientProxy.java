package mrriegel.jjetpacks.proxy;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.handler.GuiHandler;
import mrriegel.jjetpacks.helper.NBTHelper;
import mrriegel.jjetpacks.init.ModItems;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.jjetpacks.network.MessageHover;
import mrriegel.jjetpacks.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
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
					ItemStack chest = p.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
					if (chest != null && chest.getItem() instanceof ItemJetpackBase && (NBTHelper.getBoolean(chest, "active") || NBTHelper.getBoolean(chest, "hover"))) {
						for (int i = 0; i < 3; i++)
							world.spawnParticle(EnumParticleTypes.FLAME, p.posX + (world.rand.nextDouble() - .5), p.posY, p.posZ + (world.rand.nextDouble() - .5), 0, -.5, 0, 0);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent e) {
		if (hover.isPressed() && Minecraft.getMinecraft().inGameHasFocus) {
			ItemStack chest = Minecraft.getMinecraft().thePlayer.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
			if (chest != null && chest.getItem() instanceof ItemJetpackBase) {
				PacketHandler.INSTANCE.sendToServer(new MessageHover());
			}
		}
	}

}
