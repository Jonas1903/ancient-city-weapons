package com.ancientcity.weapons.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the creation and identification of custom Ancient City weapons.
 */
public class ItemManager {

    // Custom item identifiers stored in persistent data
    public static final String WARDEN_BEAM_ID = "warden_beam";
    public static final String BARRIER_CAGE_ID = "barrier_cage";

    private final NamespacedKey itemIdKey;

    public ItemManager() {
        this.itemIdKey = new NamespacedKey("ancientcityweapons", "item_id");
    }

    /**
     * Creates the Warden Beam item (Disc Fragment).
     *
     * @return The Warden Beam ItemStack
     */
    public ItemStack createWardenBeam() {
        ItemStack item = new ItemStack(Material.DISC_FRAGMENT_5);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Set display name with Ancient City theme
            meta.displayName(Component.text("Warden Beam")
                    .color(NamedTextColor.DARK_AQUA)
                    .decoration(TextDecoration.ITALIC, false)
                    .decoration(TextDecoration.BOLD, true));

            // Set lore
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("A fragment imbued with the Warden's power.")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, true));
            lore.add(Component.empty());
            lore.add(Component.text("Right-click to fire a devastating beam")
                    .color(NamedTextColor.DARK_PURPLE)
                    .decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("Range: 4 blocks | Damage: 3 hearts")
                    .color(NamedTextColor.DARK_GRAY)
                    .decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("Cooldown: 1 minute 30 seconds")
                    .color(NamedTextColor.DARK_GRAY)
                    .decoration(TextDecoration.ITALIC, false));
            meta.lore(lore);

            // Set custom item identifier
            meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, WARDEN_BEAM_ID);

            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Creates the Barrier Cage item (Heavy Core).
     *
     * @return The Barrier Cage ItemStack
     */
    public ItemStack createBarrierCage() {
        ItemStack item = new ItemStack(Material.HEAVY_CORE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Set display name with Ancient City theme
            meta.displayName(Component.text("Barrier Cage")
                    .color(NamedTextColor.DARK_AQUA)
                    .decoration(TextDecoration.ITALIC, false)
                    .decoration(TextDecoration.BOLD, true));

            // Set lore
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("A core that harnesses protective energy.")
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, true));
            lore.add(Component.empty());
            lore.add(Component.text("Right-click to create a protective barrier")
                    .color(NamedTextColor.DARK_PURPLE)
                    .decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("Radius: 4 blocks | Duration: 10 seconds")
                    .color(NamedTextColor.DARK_GRAY)
                    .decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("Cooldown: 1 minute")
                    .color(NamedTextColor.DARK_GRAY)
                    .decoration(TextDecoration.ITALIC, false));
            meta.lore(lore);

            // Set custom item identifier
            meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, BARRIER_CAGE_ID);

            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Checks if an item is the Warden Beam.
     *
     * @param item The item to check
     * @return true if it's the Warden Beam, false otherwise
     */
    public boolean isWardenBeam(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        String id = meta.getPersistentDataContainer().get(itemIdKey, PersistentDataType.STRING);
        return WARDEN_BEAM_ID.equals(id);
    }

    /**
     * Checks if an item is the Barrier Cage.
     *
     * @param item The item to check
     * @return true if it's the Barrier Cage, false otherwise
     */
    public boolean isBarrierCage(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        String id = meta.getPersistentDataContainer().get(itemIdKey, PersistentDataType.STRING);
        return BARRIER_CAGE_ID.equals(id);
    }

    /**
     * Gets the NamespacedKey used for item identification.
     *
     * @return The NamespacedKey
     */
    public NamespacedKey getItemIdKey() {
        return itemIdKey;
    }
}
