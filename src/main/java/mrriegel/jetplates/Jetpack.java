package mrriegel.jetplates;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.Validate;

import com.google.common.base.Strings;

import cofh.redstoneflux.api.IEnergyContainerItem;
import mrriegel.jetplates.network.Message2Server;
import mrriegel.jetplates.network.Message2Server.MessageAction;
import mrriegel.jetplates.network.MessageCapaSync;
import mrriegel.jetplates.network.MessageParticle;
import mrriegel.jetplates.proxy.CommonProxy;
import mrriegel.limelib.LimeLib;
import mrriegel.limelib.helper.InvHelper;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.helper.NBTStackHelper;
import mrriegel.limelib.network.PacketHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@EventBusSubscriber(modid = Jetplates.MODID)
public class Jetpack implements INBTSerializable<NBTTagCompound> {

	public ItemStack stack;
	public boolean installed, hover, active, set, hideGui;
	public double maxHSpeed, maxVSpeed, hoverSink, HSpeed = 1, VSpeed = 1;
	public GuiPos guiPos = GuiPos.BOTTOMRIGHT;
	public int tier = 0;

	private void armorTick(EntityPlayer player) {
		if (!installed)
			return;
		defaultStats();
		boolean canMove = false, sendHover = false;
		if (hover && player.world.isRemote) {
			if (player.onGround) {
				hover = false;
				sendHover = true;
			}
		}
		if (!player.world.isRemote) {
			if (player.ticksExisted % 10 == 0)
				sync((EntityPlayerMP) player);
			return;
		}
		//HOVER
		if (hover) {
			if (getFuel() > reduceFuel(2, true) && !player.capabilities.isFlying) {
				float thrust = VThrust(player);
				if (player.isSneaking()) {
					double val = -.2;
					if (player.motionY + .1 < val)
						player.moveRelative(0, thrust, 0, thrust);
					else
						player.motionY = val;
				} else {
					if (player.motionY + .1 < hoverSink)
						player.moveRelative(0, thrust * 1.2f, 0, thrust * 1.2f);
					else
						player.motionY = hoverSink;
				}
				canMove = true;
				player.fallDistance = 0f;
				PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "amount", 2), MessageAction.REDUCE));
			} else {
				hover = false;
				sendHover = true;
			}
		}
		boolean ac = active;
		if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && !player.capabilities.isFlying && getFuel() > reduceFuel(2, true) && Minecraft.getMinecraft().inGameHasFocus) {
			active = true;
			float thrust = VThrust(player);
			player.moveRelative(0, thrust, 0, thrust);
			//			move(player, 0, thrust * 2, 0);
			canMove = true;
			player.fallDistance = 0f;
			PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "amount", 2), MessageAction.REDUCE));
		} else
			active = false;
		//UP
		if (canMove && getFuel() > reduceFuel(1, true) && Minecraft.getMinecraft().inGameHasFocus) {
			boolean left = Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown();
			boolean right = Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
			boolean forward = Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown();
			boolean backward = Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown();
			float thrust = HThrust(player);
			float s = (left && right) || (!left && !right) ? 0 : left ? thrust : -thrust;
			float f = (forward && backward) || (!forward && !backward) ? 0 : forward ? thrust : -thrust;
			player.moveRelative(s, 0, f, thrust);
			//			if (forward)
			//				player.moveRelative(0, 0, thrust, thrust);
			//			else if (backward)
			//				player.moveRelative(0, 0, -thrust, thrust);
			//			else if (left)
			//				player.moveRelative(thrust, 0, 0, thrust);
			//			else if (right)
			//				player.moveRelative(-thrust, 0, 0, thrust);
			if (forward || right || left || backward)
				PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "amount", 1), MessageAction.REDUCE));
		}
		//ANTIFALL
		if (!active && !hover && player.fallDistance > 2.5f && tier > 1 && !player.isCreative()) {
			BlockPos ent = new BlockPos(player);
			BlockPos hard = null;
			int count = 0;
			while (count < 6) {
				ent = ent.down();
				IBlockState state = player.world.getBlockState(ent);
				if (state.getCollisionBoundingBox(player.world, ent) != null || ent.getY() <= 0) {
					hard = ent;
					break;
				}
				count++;
			}
			if (hard != null && new BlockPos(player).getDistance(hard.getX(), hard.getY(), hard.getZ()) <= Math.min(player.fallDistance, 24f) / 2) {
				hover = true;
				sendHover = true;
			}
		}
		if (sendHover || ac != active) {
			NBTTagCompound nbt = new NBTTagCompound();
			NBTHelper.set(nbt, "hover", hover);
			NBTHelper.set(nbt, "active", active);
			PacketHandler.sendToServer(new Message2Server(nbt, MessageAction.HOVER));
		}
	}

	private float VThrust(EntityPlayer player) {
		float thrust = (float) (maxVSpeed * VSpeed);
		if (player.motionY < 0)
			thrust *= 1.25f;
		if (player.isInWater())
			thrust *= .6f;
		return thrust;
	}

	private float HThrust(EntityPlayer player) {
		float thrust = (float) (maxHSpeed * HSpeed);
		if (!player.isSprinting())
			thrust *= .7f;
		if (player.isInWater())
			thrust *= .6f;
		return thrust;
	}

	private void defaultStats() {
		if (!set) {
			switch (tier) {
			case 1:
				maxVSpeed = .27;
				maxHSpeed = .20;
				hoverSink = -0.045;
				break;
			case 2:
				maxVSpeed = .32;
				maxHSpeed = .35;
				hoverSink = -0.025;
				break;
			case 3:
				maxVSpeed = .37;
				maxHSpeed = .50;
				hoverSink = 0;
				break;
			default:
				break;
			}
			set = true;
		}
	}

	public int reduceFuel(int amount, boolean simulate) {
		int defa = 100;
		return getEnergy().extractEnergy(amount * (hover ? defa : defa * (int) Math.pow(3, tier - 1)), simulate);
	}

	public int getFuel() {
		return getEnergy().getEnergyStored();
	}

	public int getMaxFuel() {
		return getEnergy().getMaxEnergyStored();
	}

	public IEnergyStorage getEnergy() {
		return stack.getCapability(CapabilityEnergy.ENERGY, null);
	}

	public List<String> getTooltip() {
		List<String> lis = new ArrayList<>(4);
		double percent = getEnergy().getEnergyStored() / (double) getEnergy().getMaxEnergyStored();
		String energy = (percent < .15 && (System.currentTimeMillis() / 160) % 2 == 0 ? TextFormatting.DARK_RED.toString() : "") + getEnergy().getEnergyStored();
		String maxEnergy = "" + getEnergy().getMaxEnergyStored();
		lis.add(TextFormatting.GOLD + "Energy: ");
		lis.add(TextFormatting.WHITE + energy + TextFormatting.RESET + "/" + maxEnergy);
		lis.add(TextFormatting.GOLD + "Tier: " + TextFormatting.RESET + Strings.repeat("I", tier));
		lis.add(TextFormatting.GOLD + "Hover: " + (hover ? TextFormatting.GREEN + "On" : TextFormatting.RED + "Off"));
		return lis;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTHelper.set(nbt, "installed", installed);
		NBTHelper.set(nbt, "hover", hover);
		NBTHelper.set(nbt, "active", active);
		NBTHelper.set(nbt, "HSpeed", HSpeed);
		NBTHelper.set(nbt, "VSpeed", VSpeed);
		NBTHelper.set(nbt, "guiIndex", guiPos);
		NBTHelper.set(nbt, "hideGui", hideGui);
		return writeToSyncNBT(nbt);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		installed = NBTHelper.get(nbt, "installed", Boolean.class);
		hover = NBTHelper.get(nbt, "hover", Boolean.class);
		active = NBTHelper.get(nbt, "active", Boolean.class);
		HSpeed = NBTHelper.get(nbt, "HSpeed", Double.class);
		VSpeed = NBTHelper.get(nbt, "VSpeed", Double.class);
		guiPos = NBTHelper.get(nbt, "guiIndex", GuiPos.class);
		hideGui = NBTHelper.get(nbt, "hideGui", Boolean.class);
		readFromSyncNBT(nbt);
	}

	public NBTTagCompound writeToSyncNBT(NBTTagCompound nbt) {
		NBTHelper.set(nbt, "tier", tier);
		return nbt;
	}

	public void readFromSyncNBT(NBTTagCompound nbt) {
		tier = NBTHelper.get(nbt, "tier", Integer.class);
	}

	@CapabilityInject(Jetpack.class)
	public static Capability<Jetpack> JETPACK = null;
	public static final ResourceLocation LOCATION = new ResourceLocation(Jetplates.MODID, "jetpack");
	public static boolean syncDirty = false;

	public static void register() {
		CapabilityManager.INSTANCE.register(Jetpack.class, new IStorage<Jetpack>() {

			@Override
			public NBTBase writeNBT(Capability<Jetpack> capability, Jetpack instance, EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<Jetpack> capability, Jetpack instance, EnumFacing side, NBTBase nbt) {
				if (nbt instanceof NBTTagCompound) {
					instance.deserializeNBT((NBTTagCompound) nbt);
				}
			}

		}, Jetpack::new);
	}

	public static Jetpack getJetpack(ItemStack stack) {
		return (stack.isEmpty() || !stack.hasCapability(JETPACK, null)) ? null : stack.getCapability(JETPACK, null);
	}

	public static Jetpack getJetpack(EntityPlayer player) {
		return getJetpack(player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex()));
	}

	public static void sync(EntityPlayerMP player) {
		PacketHandler.sendTo(new MessageCapaSync(player), player);
	}

	@SubscribeEvent
	public static void tick(PlayerTickEvent event) {
		if (event.phase == Phase.END) {
			Jetpack jp = getJetpack(event.player);
			if (jp != null)
				jp.armorTick(event.player);
		}
	}

	@SubscribeEvent
	public static void attach(AttachCapabilitiesEvent<ItemStack> event) {
		if (isChestplate(event.getObject())) {
			event.addCapability(LOCATION, new JetpackProvider(event.getObject()));
			ICapabilityProvider prov = event.getObject().getItem().initCapabilities(event.getObject(), new NBTTagCompound());
			boolean energy = prov != null && prov.hasCapability(CapabilityEnergy.ENERGY, null);
			if (!energy) {
				event.addCapability(new ResourceLocation(Jetplates.MODID, "jetpack_energy"), new EnergyProvider(event.getObject()));
			}
		}
	}

	@SubscribeEvent
	public static void join(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayerMP) {
			sync((EntityPlayerMP) event.getEntity());
		}
	}

	@SubscribeEvent
	public static void register(Register<IRecipe> event) {
		for (Item item : ForgeRegistries.ITEMS) {
			if (item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.CHEST) {
				ResourceLocation rl = new ResourceLocation(Jetplates.MODID, "craft_" + item);
				IRecipe recipe = new ShapelessOreRecipe(null, new ItemStack(item), new ItemStack(item), CommonProxy.upgrade) {
					@Override
					public ItemStack getCraftingResult(InventoryCrafting var1) {
						ItemStack res = InvHelper.extractItem(new InvWrapper(var1), s -> s.getItem() == getRecipeOutput().getItem(), 1, true);
						Jetpack jp = getJetpack(res);
						if (jp.installed)
							return ItemStack.EMPTY;
						jp.installed = true;
						jp.tier = InvHelper.extractItem(new InvWrapper(var1), s -> s.getItem() == CommonProxy.upgrade, 1, true).getItemDamage() + 1;
						return res;
					}
				};
				recipe.setRegistryName(rl);
				event.getRegistry().register(recipe);
				rl = new ResourceLocation(Jetplates.MODID, "uncraft_" + item);
				recipe = new ShapelessOreRecipe(null, ItemStack.EMPTY, item) {
					@Override
					public ItemStack getCraftingResult(InventoryCrafting var1) {
						ItemStack res = InvHelper.extractItem(new InvWrapper(var1), s -> s.getItem() == item, 1, true);
						Jetpack jp = getJetpack(res);
						if (!jp.installed)
							return ItemStack.EMPTY;
						return new ItemStack(CommonProxy.upgrade, 1, jp.tier - 1);
					}

					@Override
					public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
						int index = IntStream.range(0, inv.getSizeInventory()).filter(i -> inv.getStackInSlot(i).getItem() == item).findFirst().orElse(0);
						ItemStack res = inv.getStackInSlot(index).copy();
						Validate.isTrue(!res.isEmpty());
						Jetpack jp = getJetpack(res);
						jp.installed = false;
						NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
						ret.set(index, res);
						return ret;
					};

					@Override
					public ItemStack getRecipeOutput() {
						return ItemStack.EMPTY;
					}
				};
				recipe.setRegistryName(rl);
				event.getRegistry().register(recipe);

			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void tooltip(ItemTooltipEvent event) {
		Jetpack jp;
		if ((jp = getJetpack(event.getItemStack())) != null && jp.installed) {
			event.getToolTip().addAll(jp.getTooltip());
		}
	}

	@SubscribeEvent
	public static void tick(WorldTickEvent event) {
		if (event.phase == Phase.END && event.side.isServer() && event.world.getTotalWorldTime() % 5 == 0 && syncDirty) {
			for (EntityPlayer p : event.world.playerEntities)
				PacketHandler.sendTo(new MessageParticle(p.world), (EntityPlayerMP) p);
			syncDirty = false;
		}
	}

	public static boolean isChestplate(ItemStack stack) {
		return stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
	}

	static class JetpackProvider implements ICapabilitySerializable<NBTTagCompound> {

		Jetpack jp = JETPACK.getDefaultInstance();

		public JetpackProvider(ItemStack stack) {
			this.jp.stack = stack;
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == JETPACK;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return hasCapability(capability, facing) ? JETPACK.cast(jp) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) JETPACK.getStorage().writeNBT(JETPACK, jp, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			JETPACK.getStorage().readNBT(JETPACK, jp, null, nbt);
		}

	}

	static class EnergyProvider implements ICapabilityProvider {

		ItemStack stack;
		IEnergyStorage energy;

		public EnergyProvider(ItemStack stack) {
			super();
			this.stack = stack;
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			Jetpack jp = getJetpack(stack);
			boolean ret = jp != null && jp.installed && capability == CapabilityEnergy.ENERGY;
			if (ret && energy == null)
				energy = new IEnergyStorage() {

					final int capacity = 50000 * (int) Math.pow(4, jp.tier), transfer = 60 * (int) Math.pow(4, jp.tier);

					@Override
					public int receiveEnergy(int maxReceive, boolean simulate) {
						if (flux())
							return ((IEnergyContainerItem) stack.getItem()).receiveEnergy(stack, maxReceive, simulate);
						int energy = NBTStackHelper.get(stack, "Energy", int.class);
						int energyReceived = Math.min(capacity - energy, Math.min(transfer, maxReceive));
						if (!simulate) {
							energy += energyReceived;
							NBTStackHelper.set(stack, "Energy", energy);
						}
						return energyReceived;
					}

					@Override
					public int getMaxEnergyStored() {
						if (flux())
							return ((IEnergyContainerItem) stack.getItem()).getMaxEnergyStored(stack);
						return capacity;
					}

					@Override
					public int getEnergyStored() {
						if (flux())
							return ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack);
						return NBTStackHelper.getSafe(stack, "Energy", int.class).orElse(0);
					}

					@Override
					public int extractEnergy(int maxExtract, boolean simulate) {
						if (flux())
							return ((IEnergyContainerItem) stack.getItem()).extractEnergy(stack, maxExtract, simulate);
						if (!NBTStackHelper.hasTag(stack, "Energy"))
							return 0;
						int energy = NBTStackHelper.get(stack, "Energy", int.class);
						int energyExtracted = Math.min(energy, Math.min(transfer, maxExtract));
						if (!simulate) {
							energy -= energyExtracted;
							NBTStackHelper.set(stack, "Energy", energy);
						}
						return energyExtracted;
					}

					@Override
					public boolean canReceive() {
						return true;
					}

					@Override
					public boolean canExtract() {
						return true;
					}
				};
			return ret;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return hasCapability(capability, facing) ? CapabilityEnergy.ENERGY.cast(energy) : null;
		}

		private boolean flux() {
			return LimeLib.fluxLoaded && stack.getItem() instanceof IEnergyContainerItem;
		}

	}

	public static enum GuiPos {
		TOPLEFT, TOP, TOPRIGHT, MIDLEFT, MIDRIGHT, BOTTOMLEFT, BOTTOMRIGHT;
	}

}
