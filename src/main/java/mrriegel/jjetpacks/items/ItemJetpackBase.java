package mrriegel.jjetpacks.items;

import java.util.List;
import java.util.UUID;

import mcjty.rftools.blocks.ModBlocks;
import mrriegel.jjetpacks.CreativeTab;
import mrriegel.jjetpacks.config.ConfigHandler;
import mrriegel.jjetpacks.helper.NBTHelper;
import mrriegel.jjetpacks.network.MessageReduce;
import mrriegel.jjetpacks.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import vazkii.botania.api.state.enums.LuminizerVariant;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class ItemJetpackBase extends ItemArmor implements ISpecialArmor {

	public ItemJetpackBase() {
		super(ArmorMaterial.CHAIN, 0, EntityEquipmentSlot.CHEST);
		this.setCreativeTab(CreativeTab.tab1);
		this.setHasSubtypes(true);
		this.setRegistryName(getName());
		this.setUnlocalizedName(getRegistryName().toString());
	}

	public abstract String getName();

	public abstract int getNumber();

	public abstract int reduceFuel(ItemStack stack, int amount, boolean simulate);

	public abstract float getMaxVerticalSpeed(ItemStack stack);

	public abstract float getMaxHorizontalSpeed(ItemStack stack);

	public abstract float getAcceleration(ItemStack stack);

	public abstract int getDamageReduce(ItemStack stack);

	public abstract double getToughness(ItemStack stack);

	public abstract boolean hover(ItemStack stack);

	public abstract int getFuel(ItemStack stack);

	public abstract int getMaxFuel(ItemStack stack);

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < getNumber(); i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}

	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier> create();
		if (slot == this.armorType) {
			multimap.put(SharedMonsterAttributes.ARMOR.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), "Armor modifier", (double) getDamageReduce(stack), 0));
			multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), "Armor toughness", getToughness(stack), 0));
		}
		return multimap;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.values()[stack.getItemDamage()];
	}

	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getFuel(stack) != getMaxFuel(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1d - ((double) getFuel(stack) / (double) getMaxFuel(stack));
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (world.isRemote && Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && getFuel(itemStack) > reduceFuel(itemStack, 3, true) && Minecraft.getMinecraft().inGameHasFocus) {
			boolean left = Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown();
			boolean right = Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
			boolean forward = Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown();
			boolean backward = Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown();
			if (player.motionY <= getMaxVerticalSpeed(itemStack)) {
				player.motionY += (player.motionY < 0 ? 1.25d : 1d) * getAcceleration(itemStack);
				PacketHandler.INSTANCE.sendToServer(new MessageReduce(2));
			}

			float thrust = getMaxHorizontalSpeed(itemStack);
			if (forward)
				player.moveRelative(0, thrust, thrust);
			else if (backward)
				player.moveRelative(0, -thrust, thrust);
			else if (left)
				player.moveRelative(thrust, 0, thrust);
			else if (right)
				player.moveRelative(-thrust, 0, thrust);
			if (forward || right || left || backward)
				PacketHandler.INSTANCE.sendToServer(new MessageReduce(1));
			player.fallDistance = -1;
		}
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		if (source.isUnblockable())
			return new ArmorProperties(0, 1, 0);
		int maxAbsorbed = getFuelPerDamage() > 0 ? 25 * (getFuel(armor) / getFuelPerDamage()) : 0;
		return new ArmorProperties(0, 0.85 * (getDamage(armor) / 20d), maxAbsorbed);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return getDamageReduce(armor);
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		reduceFuel(stack, damage * getFuelPerDamage(), false);
	}

	int getFuelPerDamage() {
		return 10;
	}

}
