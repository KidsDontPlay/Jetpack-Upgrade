package mrriegel.jetpackupgrade.init;

import mrriegel.jetpackupgrade.items.ItemJetpackUpgrade;
import mrriegel.limelib.item.CommonItem;

public class ModItems {

	public static final CommonItem upgrade = new ItemJetpackUpgrade();

	public static void init() {
		upgrade.registerItem();
	}

	public static void initModels() {
		upgrade.initModel();
	}

}
