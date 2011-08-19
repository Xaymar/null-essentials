package me.michaeldirks.plugins.nullessentials;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import me.michaeldirks.plugins.nullessentials.PlayerList.PlayerList;
import me.michaeldirks.plugins.nullessentials.slotmanager.*;
import me.michaeldirks.plugins.nullessentials.util.*;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;

public class NullEssentials extends JavaPlugin {

    public static NullEssentials plugin = null;
    public static Server server = null;
    public static Configuration config = null;
    public static Logger log = null;
    //Outgoing messages
    public static String prefixStd = "[NE]";
    public static String prefixMsg = "&6[NE]";
    //Permissions
    public static PermissionHandler pHandler;
    //Part: Slot Manager
    public static boolean enableSlotManager = false;
    public static SlotManager partSlotManager = new SlotManager();
    //Part: Playerlist
    public static boolean enablePlayerList = false;
    public static PlayerList partPlayerList = new PlayerList();

    @Override
    public void onEnable() {
        plugin = this;
        server = this.getServer();
        log = server.getLogger();
        config = this.getConfiguration();

        setupPermissions();

        //Read Configuration
        readConfig();

        partSlotManager.onEnable();
        partPlayerList.onEnable();

        log.log(Level.INFO, prefixStd + this + " is now enabled!");
    }

    @Override
    public void onDisable() {
        partSlotManager.onDisable();
        partPlayerList.onDisable();

        log.log(Level.INFO, prefixStd + this + " is now disabled!...");
    }

    //Configuration Handling
    public void readConfig() {
        File configFile = new File(NullEssentials.plugin.getDataFolder() + "/config.yml");
        if (!configFile.exists()) {
            try {
                JarFile pluginJar = pluginJar = new JarFile(NullEssentials.plugin.getFile());
                ZipEntry jarConfig = pluginJar.getEntry("config.yml");
                FileOutputStream fileOut = new FileOutputStream(NullEssentials.plugin.getDataFolder() + "/config.yml");
                InputStream fileIn = pluginJar.getInputStream(jarConfig);
                while (fileIn.available() > 0) {
                    fileOut.write(fileIn.read());
                }
                fileIn.close();
                fileOut.close();
                pluginJar.close();
                fileIn = null;
                fileOut = null;
                pluginJar = null;
            } catch (IOException ex) {
                Logger.getLogger(NullEssentials.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        config.load();

        enableSlotManager = config.getBoolean("parts.slotmanager", false);
        enablePlayerList = config.getBoolean("parts.playerlist", false);

        partSlotManager.readConfig(config);
        partPlayerList.readConfig(config);

        config.save();
    }

    public void saveConfig() {
        config.save();
    }

    //Command Handling
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " (parts|slotmanager) <arguments...>";

        args = util.reparseArgs(args);
        
        if (args.length == 0) {
            util.sendMessage(cs, helpText);
        } else {
            String[] newArgs = new String[args.length - 1];
            for (int pos = 1; pos < args.length; pos++) {
                newArgs[pos - 1] = args[pos];
            }
            if (args[0].equalsIgnoreCase("parts")) {
                return nullPartsCommand(cs, cmnd, alias, newArgs);
            } else if (args[0].equalsIgnoreCase("slotmanager")) {
                return partSlotManager.onCommand(cs, cmnd, alias, newArgs);
            } else {
                util.sendMessage(cs, helpText);
            }
        }
        return false;
    }

    private boolean nullPartsCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " parts <enable|disable|reload> <arguments...>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (util.hasPermission(cs, "ne.parts")) {
            if (args.length == 0) {
                String parts = prefixMsg + "Parts: ";
                parts += (enableSlotManager == true ? "&2" : "&4") + "Slot Manager" + "&6, ";
                parts += (enablePlayerList == true ? "&2" : "&4") + "Player List" + "&6, ";
                util.sendMessage(cs, parts);
                return true;
            } else {
                String[] newArgs = new String[args.length - 1];
                for (int pos = 1; pos < args.length; pos++) {
                    newArgs[pos - 1] = args[pos];
                }
                if (args[0].equalsIgnoreCase("enable")) {
                    return nullPartsEnableCommand(cs, cmnd, alias, newArgs);
                } else if (args[0].equalsIgnoreCase("disable")) {
                    return nullPartsDisableCommand(cs, cmnd, alias, newArgs);
                } else if (args[0].equalsIgnoreCase("reload")) {
                    return nullPartsReloadCommand(cs, cmnd, alias, newArgs);
                } else {
                    util.sendMessage(cs, helpText);
                }
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean nullPartsEnableCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " parts enable (part)";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (util.hasPermission(cs, "ne.parts.state")) {
            if (args.length == 0) {
                util.sendMessage(cs, helpText);
            } else {
                if (args[0].equalsIgnoreCase("slotmanager")) {
                    if (util.hasPermission(cs, "ne.slotmanager.state")) {
                        enableSlotManager = true;
                        partSlotManager.onEnable();
                        util.sendMessage(cs, prefixMsg + "Slot Manager enabled.");
                        return true;
                    } else {
                        util.sendMessage(cs, permText);
                    }
                } else {
                    util.sendMessage(cs, helpText);
                }
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean nullPartsDisableCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " parts disable (part)";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (util.hasPermission(cs, "ne.parts.state")) {
            if (args.length == 0) {
                util.sendMessage(cs, helpText);
            } else {
                if (args[0].equalsIgnoreCase("slotmanager")) {
                    if (util.hasPermission(cs, "ne.slotmanager.state")) {
                        partSlotManager.onDisable();
                        enableSlotManager = false;
                        util.sendMessage(cs, prefixMsg + "Slot Manager disabled.");
                        return true;
                    } else {
                        util.sendMessage(cs, permText);
                    }
                } else {
                    util.sendMessage(cs, helpText);
                }
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean nullPartsReloadCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (util.hasPermission(cs, "ne.parts.reload")) {
            util.sendMessage(cs, prefixMsg + "Disabling parts...");
            partSlotManager.onDisable();
            partPlayerList.onDisable();
            util.sendMessage(cs, prefixMsg + "Reloading configuration...");
            readConfig();
            util.sendMessage(cs, prefixMsg + "Enabling parts...");
            partSlotManager.onEnable();
            partSlotManager.onDisable();
            util.sendMessage(cs, prefixMsg + "Done!");
            return true;
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private void setupPermissions() {
        if (pHandler != null) {
            return;
        } else {
            Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
            if (permissionsPlugin == null) {
                log.log(Level.INFO, prefixStd + "Using SuperPermissions");
                return;
            } else {
                pHandler = ((Permissions) permissionsPlugin).getHandler();
                log.log(Level.INFO, prefixStd + "Using " + ((Permissions) permissionsPlugin).getDescription().getFullName());
            }
        }
    }
}
