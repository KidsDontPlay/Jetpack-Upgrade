package mrriegel.jjetpacks.items;

import java.util.List;
import java.util.UUID;

import mrriegel.jjetpacks.CreativeTab;
import mrriegel.jjetpacks.config.ConfigHandler;
import mrriegel.jjetpacks.helper.NBTHelper;
import mrriegel.jjetpacks.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import vazkii.botania.api.state.enums.LuminizerVariant;

import com.google.common.collect.Multimap;

public abstract class ItemJetpackBase extends ItemArmor {

	public ItemJetpackBase() {
		super(ArmorMaterial.CHAIN, 0, EntityEquipmentSlot.CHEST);
		this.setCreativeTab(CreativeTab.tab1);
		this.setHasSubtypes(true);
		this.setRegistryName(getName());
		this.setUnlocalizedName(getRegistryName().toString());
	}

	public abstract String getName();

	public abstract int getNumber();

	public abstract void reduceFuel(ItemStack stack);

	public abstract float getMaxVerticalSpeed(ItemStack stack);

	public abstract float getMaxHorizontalSpeed(ItemStack stack);

	public abstract float getAcceleration(ItemStack stack);

	public abstract boolean enoughFuel(ItemStack stack);

	public abstract double getDamageReduce(ItemStack stack);

	public abstract double getToughness(ItemStack stack);

	public abstract boolean hover(ItemStack stack);
	
	public abstract void updateServer();

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
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);

		if (slot == this.armorType) {
			multimap.put(SharedMonsterAttributes.ARMOR.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), "Armor modifier", getDamageReduce(stack), 0));
			multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), "Armor toughness", getToughness(stack), 0));
		}

		return multimap;
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
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (world.isRemote && Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && enoughFuel(itemStack) && Minecraft.getMinecraft().inGameHasFocus) {
			boolean left = Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown();
			boolean right = Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
			boolean forward = Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown();
			boolean backward = Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown();
			if (player.motionY <= getMaxVerticalSpeed(itemStack)) {
				player.motionY += (player.motionY < 0 ? 1.5d : 1d) * getAcceleration(itemStack);
				reduceFuel(itemStack);
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
				reduceFuel(itemStack);
			player.fallDistance = -1;
		}
	}

}
