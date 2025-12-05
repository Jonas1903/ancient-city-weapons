package com.ancientcity.weapons;

import com.ancientcity.weapons.commands.AncientWeaponsCommand;
import com.ancientcity.weapons.listeners.ItemListener;
import com.ancientcity.weapons.managers.BarrierCageManager;
import com.ancientcity.weapons.managers.CooldownManager;
import com.ancientcity.weapons.managers.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for Ancient City Weapons.
 * Adds themed weapons from the Ancient City with special abilities.
 */
public class AncientCityWeapons extends JavaPlugin {

    private static AncientCityWeapons instance;
    private CooldownManager cooldownManager;
    private ItemManager itemManager;
    private BarrierCageManager barrierCageManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize managers
        this.cooldownManager = new CooldownManager();
        this.itemManager = new ItemManager();
        this.barrierCageManager = new BarrierCageManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new ItemListener(this), this);

        // Register commands
        AncientWeaponsCommand commandExecutor = new AncientWeaponsCommand(this);
        if (getCommand("ancientweapons") != null) {
            getCommand("ancientweapons").setExecutor(commandExecutor);
            getCommand("ancientweapons").setTabCompleter(commandExecutor);
        }

        getLogger().info("Ancient City Weapons has been enabled!");
    }

    @Override
    public void onDisable() {
        // Clean up any remaining barrier cages
        if (barrierCageManager != null) {
            barrierCageManager.removeAllCages();
        }

        getLogger().info("Ancient City Weapons has been disabled!");
    }

    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance
     */
    public static AncientCityWeapons getInstance() {
        return instance;
    }

    /**
     * Gets the cooldown manager.
     *
     * @return The cooldown manager
     */
    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    /**
     * Gets the item manager.
     *
     * @return The item manager
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Gets the barrier cage manager.
     *
     * @return The barrier cage manager
     */
    public BarrierCageManager getBarrierCageManager() {
        return barrierCageManager;
    }
}
