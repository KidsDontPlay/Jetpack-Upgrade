package mrriegel.jjetpacks.init;

import mrriegel.jjetpacks.items.ItemArmorUpgrade;
import mrriegel.jjetpacks.items.ItemBelt;
import mrriegel.jjetpacks.items.ItemJetpack1;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.limelib.item.CommonItem;

public class ModItems {

	public static CommonItem belt = new ItemBelt();
	public static CommonItem armor = new ItemArmorUpgrade();

	public static ItemJetpackBase actually = new ItemJetpack1();

	public static void init() {
		belt.registerItem();
		armor.registerItem();
		actually.registerItem();

	}

	public static void initModels() {
		belt.initModel();
		armor.initModel();
		actually.initModel();
	}

}
