package me.msicraft.cropcontrol.Command;

import me.msicraft.cropcontrol.CropControl;
import me.msicraft.cropcontrol.aCommon.Util.CropRegisterUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private void sendPermissionMessage(CommandSender sender) {
        String permissionMessage = ""; //util.getPermissionErrorMessage();
        if (permissionMessage != null && !permissionMessage.equals("")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', permissionMessage));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("cropcontrol")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "/cropcontrol help");
            }
            if (args.length >= 1) {
                String var = args[0];
                if (var != null) {
                    switch (var) {
                        case "help":
                            if (!sender.hasPermission("cropcontrol.command.help")) {
                                sendPermissionMessage(sender);
                                return false;
                            }
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.YELLOW + "/cropcontrol help : " + ChatColor.WHITE + "Show the list of commands of the [CropControl] plugin");
                                sender.sendMessage(ChatColor.YELLOW + "/cropcontrol reload : " + ChatColor.WHITE + "Reload the plugin config files");
                            }
                            break;
                        case "reload":
                            if (args.length == 1) {
                                if (!sender.hasPermission("cropcontrol.command.reload")) {
                                    sendPermissionMessage(sender);
                                    return false;
                                }
                                CropControl.getPlugin().configFilesReload();
                                sender.sendMessage(CropControl.getPrefix() + ChatColor.GREEN + " Plugin config files reloaded");
                            }
                            break;
                        case "test":
                            if (args.length == 1) {
                                sender.sendMessage("Total: " + CropRegisterUtil.getLocationSet().size());
                            }
                    }
                }
            }
        }
        return false;
    }

}
