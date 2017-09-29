package mrriegel.jetpackupgrade;

import cofh.redstoneflux.api.IEnergyContainerItem;
import mrriegel.jetpackupgrade.init.ModItems;
import mrriegel.jetpackupgrade.network.Message2Server;
import mrriegel.jetpackupgrade.network.Message2Server.MessageAction;
import mrriegel.jetpackupgrade.network.MessageCapaSync;
import mrriegel.limelib.LimeLib;
import mrriegel.limelib.helper.InvHelper;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.helper.NBTStackHelper;
import mrriegel.limelib.network.PacketHandler;
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
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@EventBusSubscriber(modid = JetpackUpgrade.MODID)
public class Jetpack implements INBTSerializable<NBTTagCompound> {

	public ItemStack stack;
	public boolean installed, hover, active, hasEnergyBefore, needSync = true;
	public double maxHSpeed, maxVSpeed, maxAcce, hoverSink, HSpeed = 1, VSpeed = 1, acce = 1;
	public int guiIndex = 6, tier = 0;

	private void armorTick(EntityPlayer player) {
		if (!installed)
			return;
		if (maxHSpeed == 0)
			switch (tier) {
			case 1:
				maxVSpeed = .25;
				maxHSpeed = .10;
				maxAcce = .12;
				hoverSink = -0.05;
				break;
			case 2:
				maxVSpeed = .35;
				maxHSpeed = .30;
				maxAcce = .21;
				hoverSink = -0.03;
				break;
			case 3:
				maxVSpeed = .45;
				maxHSpeed = .50;
				maxAcce = .3;
				hoverSink = 0;
				break;
			default:
				break;
			}
		if (hover && player.world.isRemote) {
			//			BlockPos under = new BlockPos(player).down();
			//			AxisAlignedBB aabb = player.world.getBlockState(under).getCollisionBoundingBox(player.world, under);
			if (/*aabb != null*/player.onGround) {
				hover = false;
				needSync = true;
				PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "hover", hover), MessageAction.HOVER));
			}
		}
		if (!player.world.isRemote) {
			if (player.ticksExisted % 10 == 0 || needSync)
				sync((EntityPlayerMP) player);
			return;
		}
		boolean canMove = false;
		if (hover) {
			if (getFuel() > reduceFuel(2, true)) {
				if (player.isSneaking()) {
					double val = -.2;
					if (player.motionY + .1 < val)
						player.motionY += (player.motionY < 0 ? 1.25d : 1d) * maxAcce * acce;
					else
						player.motionY = val;
				} else {
					if (player.motionY + .1 < hoverSink)
						player.motionY += (player.motionY < 0 ? 1.25d : 1d) * maxAcce * acce;
					else
						player.motionY = hoverSink;
				}
				canMove = true;
				player.fallDistance = 0f;
				PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "amount", 2), MessageAction.REDUCE));
			} else
				PacketHandler.sendToServer(new Message2Server(new NBTTagCompound(), MessageAction.HOVER));
		}
		if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && getFuel() > reduceFuel(2, true) && Minecraft.getMinecraft().inGameHasFocus) {
			active = true;
			if (player.motionY + .1 < maxVSpeed * VSpeed) {
				player.motionY += (player.motionY < 0 ? 1.25d : 1d) * maxAcce * acce;
				canMove = true;
				player.fallDistance = 0f;
				player.fall(distance, damageMultiplier);
				PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "amount", 2), MessageAction.REDUCE));
			}
		} else
			active = false;

		if (canMove && getFuel() > reduceFuel(1, true) && Minecraft.getMinecraft().inGameHasFocus) {
			boolean left = Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown();
			boolean right = Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
			boolean forward = Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown();
			boolean backward = Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown();
			float thrust = (float) (maxHSpeed * HSpeed);
			if (forward)
				player.moveRelative(0, 0, thrust, thrust);
			else if (backward)
				player.moveRelative(0, 0, -thrust, thrust);
			else if (left)
				player.moveRelative(thrust, 0, 0, thrust);
			else if (right)
				player.moveRelative(-thrust, 0, 0, thrust);
			if (forward || right || left || backward)
				PacketHandler.sendToServer(new Message2Server(NBTHelper.set(new NBTTagCompound(), "amount", 1), MessageAction.REDUCE));
		}

	}

	public int reduceFuel(int amount, boolean simulate) {
		int defa = 100;
		return getEnergy().extractEnergy(amount * (hover ? defa : defa * (int) Math.pow(3, tier - 1)), simulate);
	}

	public int getFuel() {
		return getEnergy().getEnergyStored();
	}

	public IEnergyStorage getEnergy() {
		return stack.getCapability(CapabilityEnergy.ENERGY, null);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTHelper.set(nbt, "installed", installed);
		NBTHelper.set(nbt, "hover", hover);
		//		NBTHelper.set(nbt, "active", active);
		NBTHelper.set(nbt, "hasEnergyBefore", hasEnergyBefore);
		//		NBTHelper.set(nbt, "maxHSpeed", maxHSpeed);
		//		NBTHelper.set(nbt, "maxVSpeed", maxVSpeed);
		//		NBTHelper.set(nbt, "maxAcce", maxAcce);
		NBTHelper.set(nbt, "HSpeed", HSpeed);
		NBTHelper.set(nbt, "VSpeed", VSpeed);
		NBTHelper.set(nbt, "acce", acce);
		NBTHelper.set(nbt, "guiIndex", guiIndex);
		NBTHelper.set(nbt, "tier", tier);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		installed = NBTHelper.get(nbt, "installed", Boolean.class);
		hover = NBTHelper.get(nbt, "hover", Boolean.class);
		//		active = NBTHelper.get(nbt, "active", Boolean.class);
		hasEnergyBefore = NBTHelper.get(nbt, "hasEnergyBefore", Boolean.class);
		//		maxHSpeed = NBTHelper.get(nbt, "maxHSpeed", Double.class);
		//		maxVSpeed = NBTHelper.get(nbt, "maxVSpeed", Double.class);
		//		maxAcce = NBTHelper.get(nbt, "maxAcce", Double.class);
		HSpeed = NBTHelper.get(nbt, "HSpeed", Double.class);
		VSpeed = NBTHelper.get(nbt, "VSpeed", Double.class);
		acce = NBTHelper.get(nbt, "acce", Double.class);
		guiIndex = NBTHelper.get(nbt, "guiIndex", Integer.class);
		tier = NBTHelper.get(nbt, "tier", Integer.class);
	}

	@CapabilityInject(Jetpack.class)
	public static Capability<Jetpack> JETPACK = null;
	public static final ResourceLocation LOCATION = new ResourceLocation(JetpackUpgrade.MODID, "jetpack");

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
			Jetpack jp = event.getCapabilities().get(LOCATION).getCapability(JETPACK, null);
			ICapabilityProvider prov = event.getObject().getItem().initCapabilities(event.getObject(), new NBTTagCompound());
			jp.hasEnergyBefore = prov != null && prov.hasCapability(CapabilityEnergy.ENERGY, null);
			if (!jp.hasEnergyBefore) {
				event.addCapability(new ResourceLocation(JetpackUpgrade.MODID, "jetpack_energy"), new EnergyProvider(event.getObject()));
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
				ResourceLocation rl = new ResourceLocation(JetpackUpgrade.MODID, "craft_" + item);
				IRecipe recipe = new ShapelessOreRecipe(null, new ItemStack(item), new ItemStack(item), ModItems.upgrade) {
					@Override
					public ItemStack getCraftingResult(InventoryCrafting var1) {
						ItemStack res = InvHelper.extractItem(new InvWrapper(var1), s -> s.getItem() == getRecipeOutput().getItem(), 1, true);
						Jetpack jp = getJetpack(res);
						if (jp.installed)
							return ItemStack.EMPTY;
						jp.installed = true;
						jp.tier = InvHelper.extractItem(new InvWrapper(var1), s -> s.getItem() == ModItems.upgrade, 1, true).getItemDamage() + 1;
						return res;
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
			event.getToolTip().add("Energy: " + jp.getFuel());
			event.getToolTip().add("Tier: " + jp.tier);
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
			energy = new EnergyStorage(0, 0);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			Jetpack jp = getJetpack(stack);
			boolean ret = jp != null && jp.installed && capability == CapabilityEnergy.ENERGY;
			if (ret && energy.getMaxEnergyStored() == 0)
				energy = new IEnergyStorage() {

					int capacity = 50000 * (int) Math.pow(4, jp.tier), transfer = 30 * (int) Math.pow(4, jp.tier);

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

		public NBTTagInt serializeNBT() {
			return (NBTTagInt) CapabilityEnergy.ENERGY.getStorage().writeNBT(CapabilityEnergy.ENERGY, energy, null);
		}

		public void deserializeNBT(NBTTagInt nbt) {
			CapabilityEnergy.ENERGY.getStorage().readNBT(CapabilityEnergy.ENERGY, energy, null, nbt);
		}

		private boolean flux() {
			return LimeLib.fluxLoaded && stack.getItem() instanceof IEnergyContainerItem;
		}

	}

}
