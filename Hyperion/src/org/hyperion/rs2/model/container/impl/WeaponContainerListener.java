package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;
import org.hyperion.rs2.model.container.Equipment;

public class WeaponContainerListener implements ContainerListener {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates the listener.
     *
     * @param player The player.
     */
    public WeaponContainerListener(Player player) {
        this.player = player;
    }

    @Override
    public void itemChanged(Container container, int slot) {
        if (slot == Equipment.SLOT_WEAPON) {
            sendWeapon();
        }
    }

    @Override
    public void itemsChanged(Container container, int[] slots) {
        for (final int slot : slots) {
            if (slot == Equipment.SLOT_WEAPON) {
                sendWeapon();
                return;
            }
        }
    }

    @Override
    public void itemsChanged(Container container) {
        sendWeapon();
    }

    /**
     * Sends weapon information.
     */
    private void sendWeapon() {
        final Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
        int id = -1;
        String name = null;
        if (weapon == null) {
            name = "Unarmed";
        } else {
            name = weapon.getDefinition().getName();
            id = weapon.getId();
        }
        final String genericName = filterWeaponName(name).trim();
        sendWeapon(id, name, genericName);
        sendSpecialBar(id);
    }

    /**
     * Sends weapon information.
     *
     * @param id The id.
     * @param name The name.
     * @param genericName The filtered name.
     */
    private void sendWeapon(int id, String name, String genericName) {
        if (name.equals("Unarmed")) {
            player.getActionSender().sendTab(73, 92);
            player.getActionSender().sendInterfaceString("Unarmed", 92, 0);
        } else if (name.equals("Abyssal whip")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 93);
            player.getActionSender().sendInterfaceString(name, 93, 0);
        } else if (name.equals("Granite maul") || name.equals("Tzhaar-ket-om") || name.equals("Torags hammers")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 76);
            player.getActionSender().sendInterfaceString(name, 76, 0);
        } else if (name.equals("Veracs flail") || name.endsWith("mace")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 88);
            player.getActionSender().sendInterfaceString(name, 88, 0);
        } else if (name.endsWith("crossbow") || name.endsWith(" c'bow")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 79);
            player.getActionSender().sendInterfaceString(name, 79, 0);
        } else if (name.endsWith("bow") || name.endsWith("bow full") || name.equals("Seercull")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 77);
            player.getActionSender().sendInterfaceString(name, 77, 0);
        } else if (name.startsWith("Staff") || name.endsWith("staff") || name.equals("Toktz-mej-tal")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 90);
            player.getActionSender().sendInterfaceString(name, 90, 0);
        } else if (name.endsWith("dart") || name.endsWith("knife") || name.endsWith("javelin") || name.endsWith("thrownaxe") || name.equals("Toktz-xil-ul")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 91);
            player.getActionSender().sendInterfaceString(name, 91, 0);
        } else if (name.endsWith("dagger") || name.endsWith("dagger(s)") || name.endsWith("dagger(+)") || name.endsWith("dagger(p)")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 89);
            player.getActionSender().sendInterfaceString(name, 89, 0);
        } else if (name.endsWith("pickaxe")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 83);
            player.getActionSender().sendInterfaceString(name, 83, 0);
        } else if (name.endsWith("axe") || name.endsWith("battleaxe")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 75);
            player.getActionSender().sendInterfaceString(name, 75, 0);
        } else if (name.endsWith("halberd")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 84);
            player.getActionSender().sendInterfaceString(name, 84, 0);
        } else if (name.endsWith("spear") || name.equals("Guthans warspear")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 85);
            player.getActionSender().sendInterfaceString(name, 85, 0);
        } else if (name.endsWith("claws")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 78);
            player.getActionSender().sendInterfaceString(name, 78, 0);
        } else if (name.endsWith("2h sword") || name.endsWith("godsword") || name.equals("Saradomin sword")) {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 81);
            player.getActionSender().sendInterfaceString(name, 81, 0);
        } else {
            player.getActionSender().sendTab(player.isHD() ? 87 : 73, 82);
            player.getActionSender().sendInterfaceString(name, 82, 0);
        }
    }

    /**
     * Sends weapon special bar.
     *
     * @param weaponId The weapon id.
     */
    private void sendSpecialBar(int weaponId) {
        if (weaponId == 4151) {
            player.getActionSender().sendInterfaceConfig(93, 10, false);
        } else if (weaponId == 1215 || weaponId == 1231 || weaponId == 5680
                || weaponId == 5698 || weaponId == 8872 || weaponId == 8874
                || weaponId == 8876 || weaponId == 8878) {
            player.getActionSender().sendInterfaceConfig(89, 12, false);
        } else if (weaponId == 35 || weaponId == 1305 || weaponId == 4587
                || weaponId == 6746 || weaponId == 11037) {
            player.getActionSender().sendInterfaceConfig(82, 12, false);
        } else if (weaponId == 7158 || weaponId == 11694 || weaponId == 11696
                || weaponId == 11698 || weaponId == 11700 || weaponId == 11730) {
            player.getActionSender().sendInterfaceConfig(81, 12, false);
        } else if (weaponId == 859 || weaponId == 861 || weaponId == 6724
                || weaponId == 10284 || weaponId == 859 || weaponId == 11235) {
            player.getActionSender().sendInterfaceConfig(77, 13, false);
        } else if (weaponId == 8880) {
            player.getActionSender().sendInterfaceConfig(79, 10, false);
        } else if (weaponId == 3101) {
            player.getActionSender().sendInterfaceConfig(78, 12, false);
        } else if (weaponId == 1434 || weaponId == 11061 || weaponId == 10887) {
            player.getActionSender().sendInterfaceConfig(88, 12, false);
        } else if (weaponId == 1377 || weaponId == 6739) {
            player.getActionSender().sendInterfaceConfig(75, 12, false);
        } else if (weaponId == 4153) {
            player.getActionSender().sendInterfaceConfig(76, 10, false);
        } else if (weaponId == 3204) {
            player.getActionSender().sendInterfaceConfig(84, 10, false);
        }
        player.getActionSender().sendConfigTwo(300, player.getSpecialAmount());
    }

    /**
     * Filters a weapon name.
     *
     * @param name The original name.
     * @return The filtered name.
     */
    private String filterWeaponName(String name) {
        final String[] filtered = new String[]{"Iron", "Steel", "Scythe",
            "Black", "Mithril", "Adamant", "Rune", "Granite", "Dragon",
            "Crystal", "Bronze"};
        for (final String filter : filtered) {
            name = name.replaceAll(filter, "");
        }
        return name;
    }
}
