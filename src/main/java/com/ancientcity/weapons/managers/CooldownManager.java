package com.ancientcity.weapons.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages cooldowns for Ancient City weapon abilities.
 * Uses player UUIDs to track cooldown expiration times.
 */
public class CooldownManager {

    // Cooldown times in seconds
    public static final int WARDEN_BEAM_COOLDOWN = 90; // 1 minute 30 seconds
    public static final int BARRIER_CAGE_COOLDOWN = 60; // 1 minute

    // Maps to store cooldown expiration times (in milliseconds)
    private final Map<UUID, Long> wardenBeamCooldowns;
    private final Map<UUID, Long> barrierCageCooldowns;

    public CooldownManager() {
        this.wardenBeamCooldowns = new HashMap<>();
        this.barrierCageCooldowns = new HashMap<>();
    }

    /**
     * Checks if a player is on cooldown for the Warden Beam.
     *
     * @param playerUuid The player's UUID
     * @return true if on cooldown, false otherwise
     */
    public boolean isOnWardenBeamCooldown(UUID playerUuid) {
        if (!wardenBeamCooldowns.containsKey(playerUuid)) {
            return false;
        }
        return System.currentTimeMillis() < wardenBeamCooldowns.get(playerUuid);
    }

    /**
     * Gets the remaining cooldown time for the Warden Beam in seconds.
     *
     * @param playerUuid The player's UUID
     * @return Remaining time in seconds, or 0 if not on cooldown
     */
    public int getWardenBeamRemainingTime(UUID playerUuid) {
        if (!isOnWardenBeamCooldown(playerUuid)) {
            return 0;
        }
        long remaining = wardenBeamCooldowns.get(playerUuid) - System.currentTimeMillis();
        return (int) Math.ceil(remaining / 1000.0);
    }

    /**
     * Sets the Warden Beam cooldown for a player.
     *
     * @param playerUuid The player's UUID
     */
    public void setWardenBeamCooldown(UUID playerUuid) {
        long expirationTime = System.currentTimeMillis() + (WARDEN_BEAM_COOLDOWN * 1000L);
        wardenBeamCooldowns.put(playerUuid, expirationTime);
    }

    /**
     * Checks if a player is on cooldown for the Barrier Cage.
     *
     * @param playerUuid The player's UUID
     * @return true if on cooldown, false otherwise
     */
    public boolean isOnBarrierCageCooldown(UUID playerUuid) {
        if (!barrierCageCooldowns.containsKey(playerUuid)) {
            return false;
        }
        return System.currentTimeMillis() < barrierCageCooldowns.get(playerUuid);
    }

    /**
     * Gets the remaining cooldown time for the Barrier Cage in seconds.
     *
     * @param playerUuid The player's UUID
     * @return Remaining time in seconds, or 0 if not on cooldown
     */
    public int getBarrierCageRemainingTime(UUID playerUuid) {
        if (!isOnBarrierCageCooldown(playerUuid)) {
            return 0;
        }
        long remaining = barrierCageCooldowns.get(playerUuid) - System.currentTimeMillis();
        return (int) Math.ceil(remaining / 1000.0);
    }

    /**
     * Sets the Barrier Cage cooldown for a player.
     *
     * @param playerUuid The player's UUID
     */
    public void setBarrierCageCooldown(UUID playerUuid) {
        long expirationTime = System.currentTimeMillis() + (BARRIER_CAGE_COOLDOWN * 1000L);
        barrierCageCooldowns.put(playerUuid, expirationTime);
    }

    /**
     * Formats remaining time into a human-readable string.
     *
     * @param seconds The remaining time in seconds
     * @return Formatted time string (e.g., "1 minute 30 seconds")
     */
    public String formatTime(int seconds) {
        if (seconds <= 0) {
            return "0 seconds";
        }

        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (minutes > 0) {
            sb.append(minutes).append(minutes == 1 ? " minute" : " minutes");
            if (remainingSeconds > 0) {
                sb.append(" ");
            }
        }
        if (remainingSeconds > 0) {
            sb.append(remainingSeconds).append(remainingSeconds == 1 ? " second" : " seconds");
        }

        return sb.toString();
    }

    /**
     * Clears a player's cooldowns (useful when player leaves).
     *
     * @param playerUuid The player's UUID
     */
    public void clearCooldowns(UUID playerUuid) {
        wardenBeamCooldowns.remove(playerUuid);
        barrierCageCooldowns.remove(playerUuid);
    }
}
