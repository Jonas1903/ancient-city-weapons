package com.ancientcity.weapons.commands;

import com.ancientcity.weapons.AncientCityWeapons;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
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
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
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
        player.sendMessage(ChatColor.GREEN + "You received a " + ChatColor.DARK_AQUA + "Warden Beam" + ChatColor.GREEN + "!");
    }

    /**
     * Gives the player a Barrier Cage item.
     *
     * @param player The player to give the item to
     */
    private void giveBarrierCage(Player player) {
        ItemStack barrierCage = plugin.getItemManager().createBarrierCage();
        player.getInventory().addItem(barrierCage);
        player.sendMessage(ChatColor.GREEN + "You received a " + ChatColor.DARK_AQUA + "Barrier Cage" + ChatColor.GREEN + "!");
    }

    /**
     * Sends usage information to the player.
     *
     * @param player The player to send usage info to
     */
    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.DARK_AQUA + "━━━ Ancient City Weapons ━━━");
        player.sendMessage(ChatColor.GOLD + "/ancientweapons give beam" + ChatColor.GRAY + " - Get the Warden Beam");
        player.sendMessage(ChatColor.GOLD + "/ancientweapons give cage" + ChatColor.GRAY + " - Get the Barrier Cage");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
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
