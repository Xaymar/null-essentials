package me.michaeldirks.plugins.nullessentials;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import me.michaeldirks.plugins.nullessentials.slotmanager.SlotManagerCommands;
import me.michaeldirks.plugins.nullessentials.slotmanager.SlotManagerPlayerListener;
import me.michaeldirks.plugins.nullessentials.slotmanager.SlotManagerSpoutListener;
import me.michaeldirks.plugins.nullessentials.util.util;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class NullEssentials extends JavaPlugin {
    public static NullEssentials plugin = null;
    public static Server server = null;
    public static Configuration config = null;
    public static Logger log = null;
    public static String prefixStd = "[NULL]";
    public static String prefixMsg = "&6[NULL]";
    public static boolean enableSpout = false;
    public static boolean enableSlotManager = false;
    public static boolean firstRun = true;
    public static boolean lastRun = false;
    private static SlotManagerPlayerListener partsSlotManager_PlayerListener = new SlotManagerPlayerListener();
    private static SlotManagerSpoutListener partsSlotManager_SpoutListener = new SlotManagerSpoutListener();

    public void onDisable() {
        lastRun = true;
        
        System.out.println(prefixStd+"Disabling...");
        
        partsSlotManager_PlayerListener.onDisable();
        partsSlotManager_SpoutListener.onDisable();
        
        System.out.println(prefixStd+this+" is now disabled!...");
        lastRun = false;
    }

    public void onEnable() {
        firstRun = true;
        
        plugin = this;
        server = this.getServer();
        log = server.getLogger();
        
        log.log(Level.INFO, prefixStd+"Enabling...");
        
        File configFile = new File(plugin.getDataFolder().getPath()+"/config.yml");
        if (configFile.exists() == false) {
            log.log(Level.INFO, plugin.getFile().getPath());
            try (JarFile pluginFile = new JarFile(plugin.getFile())) {
                ZipEntry configZip = pluginFile.getEntry("config.yml");
                try (FileOutputStream configOut = new FileOutputStream(plugin.getDataFolder().getPath()+"/config.yml"); InputStream configIn = pluginFile.getInputStream(configZip)) {
                    for (int c = configIn.read(); c!= -1; c = configIn.read()) {
                        configOut.write(c);
                    }
                    log.log(Level.CONFIG, prefixStd+"Unzipped config.yml, please close the server and configure needed settings!");
                }
            } catch (IOException ex) {
                log.log(Level.SEVERE, prefixStd+"Unable to unzip config.yml from plugin, manual unzip required.");
            }
        }
        
        config = this.getConfiguration();
        config.load();
        
        enableSpout = (server.getPluginManager().getPlugin("Spout") != null);
        if (enableSpout == true) log.log(Level.INFO, prefixStd+"Spout is installed! Continuing happily.");
        
        //Find enabled parts
        enableSlotManager = config.getBoolean("parts.slotmanager", false);
        
        //Run onEnable for all known parts
        if (enableSlotManager) {
            partsSlotManager_PlayerListener.onEnable();
            partsSlotManager_SpoutListener.onEnable();
        }
        
        //Enable command using
        System.out.println("pre-getCommand");
        getCommand("null").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                return NullEssentials.nullCommand(cs,cmnd,string,strings);
            }
        });
        System.out.println("post-getCommand");
        
        log.log(Level.INFO, prefixStd+this+" is now enabled!");
        firstRun = false;
    }
    
    //Command Handling
    public static boolean nullCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/null (parts|slotmanager) <arguments...>");
        if (args.length == 0) { cs.sendMessage(helpText); } else {
            String[] newArgs = new String[args.length-1];
            for (int pos = 1; pos < args.length; pos++)
                newArgs[pos-1] = args[pos];
            if (args[0].equalsIgnoreCase("parts")) {
                return nullPartsCommand(cs,cmnd,alias,newArgs);
            } else if (args[0].equalsIgnoreCase("slotmanager")) {
                return SlotManagerCommands.slotManagerCommand(cs,cmnd,alias,args);
            } else { cs.sendMessage(helpText); }
        }
        return false;
    }
    
    private static boolean nullPartsCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/null parts <enable|disable|reload> <arguments...>");
        if (args.length == 0) {
            String parts = prefixMsg+"Parts: ";
            if (enableSlotManager == true)
                parts += "&2";
            else
                parts += "&4";
            parts += "Slot Manager"+"&6, ";
            cs.sendMessage(util.colorize(parts));
            return true;
        } else {
            String[] newArgs = new String[args.length-1];
            for (int pos = 1; pos < args.length; pos++)
                newArgs[pos-1] = args[pos];
            if (args[0].equalsIgnoreCase("enable")) {
                return nullPartsEnableCommand(cs,cmnd,alias,newArgs);
            } else if (args[0].equalsIgnoreCase("disable")) {
                return nullPartsDisableCommand(cs,cmnd,alias,newArgs);
            } else if (args[0].equalsIgnoreCase("reload")) {
                return nullPartsReloadCommand(cs,cmnd,alias,newArgs);
            } else {
                cs.sendMessage(helpText);
            }
        }
        return false;
    }
    
    private static boolean nullPartsEnableCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/null parts enable (part)");
        if (args.length == 0) { cs.sendMessage(helpText); } else {
            if (args[0].equalsIgnoreCase("slotmanager")) {
                if (enableSlotManager == false) {
                    enableSlotManager = true;
                    partsSlotManager_PlayerListener.onEnable();
                    partsSlotManager_SpoutListener.onEnable();
                }
                cs.sendMessage(util.colorize(prefixMsg+"Slot Manager enabled."));
                return true;
            } else { cs.sendMessage(helpText); }
        }
        return false;
    }

    private static boolean nullPartsDisableCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/null parts disable (part)");
        if (args.length == 0) { cs.sendMessage(helpText); } else {
            if (args[0].equalsIgnoreCase("slotmanager")) {
                if (enableSlotManager) {
                    partsSlotManager_PlayerListener.onDisable();
                    partsSlotManager_SpoutListener.onDisable();
                    enableSlotManager = false;
                }
                cs.sendMessage(util.colorize(prefixMsg+"Slot Manager disabled."));
                return true;
            } else { cs.sendMessage(helpText); }
        }
        return false;
    }

    private static boolean nullPartsReloadCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        cs.sendMessage(util.colorize(prefixMsg+"Disabling parts..."));
        if (enableSlotManager) {
            partsSlotManager_PlayerListener.onDisable();
            partsSlotManager_SpoutListener.onDisable();
        }
        cs.sendMessage(util.colorize(prefixMsg+"Reloading configuration..."));
        config.load();
        cs.sendMessage(util.colorize(prefixMsg+"Enabling parts..."));
        if (enableSlotManager) {
            partsSlotManager_PlayerListener.onEnable();
            partsSlotManager_SpoutListener.onEnable();
        }
        cs.sendMessage(util.colorize(prefixMsg+"Done!"));
        return true;
    }
}
