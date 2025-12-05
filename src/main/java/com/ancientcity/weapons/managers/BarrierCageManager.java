package com.ancientcity.weapons.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ancientcity.weapons.AncientCityWeapons;

import java.util.*;

/**
 * Manages the creation and removal of barrier cages.
 * Handles scheduled cleanup of barrier blocks after the duration expires.
 */
public class BarrierCageManager {

    private static final int DEFAULT_CAGE_RADIUS = 4;
    private static final int DEFAULT_CAGE_DURATION_SECONDS = 10;

    private final AncientCityWeapons plugin;
    // Map of player UUID to their active cage block locations
    private final Map<UUID, Set<Location>> activeCages;

    public BarrierCageManager(AncientCityWeapons plugin) {
        this.plugin = plugin;
        this.activeCages = new HashMap<>();
    }

    /**
     * Gets the cage radius from config.
     *
     * @return Cage radius in blocks
     */
    public int getCageRadius() {
        return plugin.getConfig().getInt("barrier-cage.radius", DEFAULT_CAGE_RADIUS);
    }

    /**
     * Gets the cage duration from config.
     *
     * @return Cage duration in seconds
     */
    public int getCageDuration() {
        return plugin.getConfig().getInt("barrier-cage.duration", DEFAULT_CAGE_DURATION_SECONDS);
    }

    /**
     * Creates a barrier cage around the player.
     *
     * @param player The player to create the cage around
     */
    public void createCage(Player player) {
        UUID playerUuid = player.getUniqueId();
        int cageRadius = getCageRadius();
        int cageDuration = getCageDuration();

        // Remove any existing cage for this player
        if (activeCages.containsKey(playerUuid)) {
            removeCage(playerUuid);
        }

        Location center = player.getLocation().getBlock().getLocation();
        World world = center.getWorld();
        if (world == null) return;

        Set<Location> cageBlocks = new HashSet<>();

        // Create a hollow sphere of barrier blocks
        for (int x = -cageRadius; x <= cageRadius; x++) {
            for (int y = -cageRadius; y <= cageRadius; y++) {
                for (int z = -cageRadius; z <= cageRadius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    
                    // Only place blocks on the outer shell of the sphere
                    if (distance >= cageRadius - 0.5 && distance <= cageRadius + 0.5) {
                        Location blockLoc = center.clone().add(x, y, z);
                        Block block = blockLoc.getBlock();
                        
                        // Only replace air blocks to avoid destroying terrain
                        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) {
                            block.setType(Material.BARRIER);
                            cageBlocks.add(blockLoc);
                        }
                    }
                }
            }
        }

        // Store the cage blocks
        activeCages.put(playerUuid, cageBlocks);

        // Spawn particles around the cage for visual effect
        spawnCageParticles(center, world, cageRadius);

        // Schedule removal of the cage after duration expires
        new BukkitRunnable() {
            @Override
            public void run() {
                removeCage(playerUuid);
            }
        }.runTaskLater(plugin, cageDuration * 20L);
    }

    /**
     * Spawns particles around the cage to visualize it.
     *
     * @param center The center of the cage
     * @param world The world to spawn particles in
     * @param radius The radius of the cage
     */
    private void spawnCageParticles(Location center, World world, int radius) {
        // Spawn soul fire particles around the cage perimeter
        for (int i = 0; i < 360; i += 15) {
            double radians = Math.toRadians(i);
            double x = center.getX() + 0.5 + radius * Math.cos(radians);
            double z = center.getZ() + 0.5 + radius * Math.sin(radians);
            
            world.spawnParticle(Particle.SOUL_FIRE_FLAME, x, center.getY() + 1, z, 3, 0.1, 0.5, 0.1, 0.02);
        }
    }

    /**
     * Removes a player's barrier cage.
     *
     * @param playerUuid The UUID of the player whose cage to remove
     */
    public void removeCage(UUID playerUuid) {
        Set<Location> cageBlocks = activeCages.remove(playerUuid);
        if (cageBlocks == null) return;

        for (Location loc : cageBlocks) {
            Block block = loc.getBlock();
            // Only remove if it's still a barrier block (to avoid removing other blocks)
            if (block.getType() == Material.BARRIER) {
                block.setType(Material.AIR);
            }
        }

        // Spawn particles when cage disappears
        if (!cageBlocks.isEmpty()) {
            Location firstLoc = cageBlocks.iterator().next();
            World world = firstLoc.getWorld();
            if (world != null) {
                for (Location loc : cageBlocks) {
                    world.spawnParticle(Particle.SCULK_SOUL, loc.clone().add(0.5, 0.5, 0.5), 1, 0.2, 0.2, 0.2, 0.05);
                }
            }
        }
    }

    /**
     * Removes all active barrier cages.
     * Called when the plugin is disabled to clean up.
     */
    public void removeAllCages() {
        // Collect UUIDs to avoid ConcurrentModificationException
        UUID[] playerUuids = activeCages.keySet().toArray(new UUID[0]);
        for (UUID playerUuid : playerUuids) {
            removeCage(playerUuid);
        }
    }

    /**
     * Checks if a player has an active cage.
     *
     * @param playerUuid The player's UUID
     * @return true if the player has an active cage
     */
    public boolean hasActiveCage(UUID playerUuid) {
        return activeCages.containsKey(playerUuid);
    }
}
