package com.ancientcity.weapons.commands;

import com.ancientcity.weapons.AncientCityWeapons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command handler for Ancient City Weapons plugin.
 * Provides commands to give players the custom weapons.
 */
public class AncientWeaponsCommand implements CommandExecutor, TabCompleter {

    private final AncientCityWeapons plugin;

    public AncientWeaponsCommand(AncientCityWeapons plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players!")
                    .color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sendUsage(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String itemType = args[1].toLowerCase();

        if (!subCommand.equals("give")) {
            sendUsage(player);
            return true;
        }

        if (!player.hasPermission("ancientweapons.give")) {
            player.sendMessage(Component.text("You don't have permission to use this command!")
                    .color(NamedTextColor.RED));
            return true;
        }

        switch (itemType) {
            case "beam" -> giveWardenBeam(player);
            case "cage" -> giveBarrierCage(player);
            default -> sendUsage(player);
        }

        return true;
    }

    /**
     * Gives the player a Warden Beam item.
     *
     * @param player The player to give the item to
     */
    private void giveWardenBeam(Player player) {
        ItemStack wardenBeam = plugin.getItemManager().createWardenBeam();
        player.getInventory().addItem(wardenBeam);
        player.sendMessage(Component.text("You received a ")
                .color(NamedTextColor.GREEN)
                .append(Component.text("Warden Beam")
                        .color(NamedTextColor.DARK_AQUA))
                .append(Component.text("!")
                        .color(NamedTextColor.GREEN)));
    }

    /**
     * Gives the player a Barrier Cage item.
     *
     * @param player The player to give the item to
     */
    private void giveBarrierCage(Player player) {
        ItemStack barrierCage = plugin.getItemManager().createBarrierCage();
        player.getInventory().addItem(barrierCage);
        player.sendMessage(Component.text("You received a ")
                .color(NamedTextColor.GREEN)
                .append(Component.text("Barrier Cage")
                        .color(NamedTextColor.DARK_AQUA))
                .append(Component.text("!")
                        .color(NamedTextColor.GREEN)));
    }

    /**
     * Sends usage information to the player.
     *
     * @param player The player to send usage info to
     */
    private void sendUsage(Player player) {
        player.sendMessage(Component.text("━━━ Ancient City Weapons ━━━")
                .color(NamedTextColor.DARK_AQUA));
        player.sendMessage(Component.text("/ancientweapons give beam")
                .color(NamedTextColor.GOLD)
                .append(Component.text(" - Get the Warden Beam")
                        .color(NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/ancientweapons give cage")
                .color(NamedTextColor.GOLD)
                .append(Component.text(" - Get the Barrier Cage")
                        .color(NamedTextColor.GRAY)));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("give");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            completions.addAll(Arrays.asList("beam", "cage"));
        }

        // Filter based on current input
        String currentArg = args[args.length - 1].toLowerCase();
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(currentArg))
                .toList();
    }
}
