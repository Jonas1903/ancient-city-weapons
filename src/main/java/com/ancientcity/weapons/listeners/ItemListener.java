package com.ancientcity.weapons.listeners;

import com.ancientcity.weapons.AncientCityWeapons;
import com.ancientcity.weapons.managers.BarrierCageManager;
import com.ancientcity.weapons.managers.CooldownManager;
import com.ancientcity.weapons.managers.ItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.UUID;

/**
 * Handles player interactions with Ancient City weapons.
 */
public class ItemListener implements Listener {

    private static final double WARDEN_BEAM_RANGE = 4.0;
    private static final double WARDEN_BEAM_WIDTH = 1.0;
    private static final double WARDEN_BEAM_DAMAGE = 6.0; // 3 hearts = 6 HP

    private final AncientCityWeapons plugin;

    public ItemListener(AncientCityWeapons plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Only handle right-click actions
        if (!event.getAction().isRightClick()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        ItemManager itemManager = plugin.getItemManager();
        CooldownManager cooldownManager = plugin.getCooldownManager();

        // Check for Warden Beam
        if (itemManager.isWardenBeam(item)) {
            event.setCancelled(true);
            handleWardenBeam(player, cooldownManager);
        }
        // Check for Barrier Cage
        else if (itemManager.isBarrierCage(item)) {
            event.setCancelled(true);
            handleBarrierCage(player, cooldownManager);
        }
    }

    /**
     * Handles the Warden Beam ability activation.
     *
     * @param player The player using the ability
     * @param cooldownManager The cooldown manager
     */
    private void handleWardenBeam(Player player, CooldownManager cooldownManager) {
        UUID playerUuid = player.getUniqueId();

        // Check cooldown
        if (cooldownManager.isOnWardenBeamCooldown(playerUuid)) {
            int remaining = cooldownManager.getWardenBeamRemainingTime(playerUuid);
            String formattedTime = cooldownManager.formatTime(remaining);
            player.sendMessage(Component.text("⚡ Warden Beam is on cooldown! ")
                    .color(NamedTextColor.RED)
                    .append(Component.text("(" + formattedTime + " remaining)")
                            .color(NamedTextColor.GRAY)));
            return;
        }

        // Fire the beam
        fireWardenBeam(player);

        // Set cooldown
        cooldownManager.setWardenBeamCooldown(playerUuid);
        player.sendMessage(Component.text("⚡ Warden Beam fired!")
                .color(NamedTextColor.DARK_AQUA));
    }

    /**
     * Fires the Warden Beam, dealing damage to entities in its path.
     *
     * @param player The player firing the beam
     */
    private void fireWardenBeam(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection().normalize();
        World world = player.getWorld();

        // Calculate the end point of the beam for entity detection
        Location beamEnd = eyeLocation.clone().add(direction.clone().multiply(WARDEN_BEAM_RANGE));
        Location beamCenter = eyeLocation.clone().add(direction.clone().multiply(WARDEN_BEAM_RANGE / 2));
        
        // Get all entities in the beam area once (more efficient)
        double searchRadius = WARDEN_BEAM_RANGE / 2 + WARDEN_BEAM_WIDTH;
        Collection<Entity> potentialTargets = world.getNearbyEntities(beamCenter, searchRadius, searchRadius, searchRadius);
        
        // Track damaged entities to avoid hitting them multiple times
        java.util.Set<Entity> damagedEntities = new java.util.HashSet<>();

        // Create beam visual and check for entities
        for (double d = 0; d <= WARDEN_BEAM_RANGE; d += 0.25) {
            Location point = eyeLocation.clone().add(direction.clone().multiply(d));

            // Spawn particles for the beam visual (dark blue/soul themed)
            world.spawnParticle(Particle.SOUL_FIRE_FLAME, point, 2, 0.1, 0.1, 0.1, 0.01);
            world.spawnParticle(Particle.SCULK_SOUL, point, 1, 0.05, 0.05, 0.05, 0.02);

            // Check cached entities against this beam point
            for (Entity entity : potentialTargets) {
                if (damagedEntities.contains(entity)) continue;
                if (!(entity instanceof LivingEntity livingEntity) || entity == player) continue;
                
                // Check if entity is within beam width at this point
                if (entity.getLocation().distanceSquared(point) <= (WARDEN_BEAM_WIDTH / 2) * (WARDEN_BEAM_WIDTH / 2) + 1) {
                    // Deal damage
                    livingEntity.damage(WARDEN_BEAM_DAMAGE, player);
                    damagedEntities.add(entity);
                    
                    // Visual effect on hit
                    world.spawnParticle(Particle.SCULK_SOUL, entity.getLocation().add(0, 1, 0), 10, 0.3, 0.5, 0.3, 0.1);
                }
            }
        }

        // Play a sound effect
        player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5f, 1.5f);
    }

    /**
     * Handles the Barrier Cage ability activation.
     *
     * @param player The player using the ability
     * @param cooldownManager The cooldown manager
     */
    private void handleBarrierCage(Player player, CooldownManager cooldownManager) {
        UUID playerUuid = player.getUniqueId();

        // Check cooldown
        if (cooldownManager.isOnBarrierCageCooldown(playerUuid)) {
            int remaining = cooldownManager.getBarrierCageRemainingTime(playerUuid);
            String formattedTime = cooldownManager.formatTime(remaining);
            player.sendMessage(Component.text("🛡 Barrier Cage is on cooldown! ")
                    .color(NamedTextColor.RED)
                    .append(Component.text("(" + formattedTime + " remaining)")
                            .color(NamedTextColor.GRAY)));
            return;
        }

        // Create the barrier cage
        BarrierCageManager cageManager = plugin.getBarrierCageManager();
        cageManager.createCage(player);

        // Set cooldown
        cooldownManager.setBarrierCageCooldown(playerUuid);
        player.sendMessage(Component.text("🛡 Barrier Cage activated! ")
                .color(NamedTextColor.DARK_AQUA)
                .append(Component.text("(10 seconds)")
                        .color(NamedTextColor.GRAY)));
    }
}
