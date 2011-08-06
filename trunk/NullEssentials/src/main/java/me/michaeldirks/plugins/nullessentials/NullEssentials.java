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

public class NullEssentials extends JavaPlugin {
    public static NullEssentials        plugin                  = null;
    public static Server                server                  = null;
    public static Configuration         config                  = null;
    public static Logger                log                     = null;
    
    public static String                prefixStd               = "[NE]";
    public static String                prefixMsg               = "&6[NE]";
    
    //Part: Slot Manager
    public static boolean               enableSlotManager       = false;
    public static SlotManager           partSlotManager         = new SlotManager();
    
    //Part: Playerlist
    public static boolean               enablePlayerList        = false;
    public static PlayerList            partPlayerList          = new PlayerList();
    
    
    
    @Override
    public void onEnable() {
        plugin = this;
        server = this.getServer();
        log = server.getLogger();
        config = this.getConfiguration();
        
        log.log(Level.INFO, prefixStd+"Enabling...");
        
        readConfig();
        
        enableSlotManager = config.getBoolean("parts.slotmanager", false);
        enableSlotManager = config.getBoolean("parts.playerlist", false);
        
        partSlotManager.onEnable();
        partPlayerList.onEnable();
        
        log.log(Level.INFO, prefixStd+this+" is now enabled!");
    }
    @Override
    public void onDisable() {
        log.log(Level.INFO, prefixStd+"Disabling...");
        
        partSlotManager.onDisable();
        partPlayerList.onDisable();
        
        log.log(Level.INFO, prefixStd+this+" is now disabled!...");
    }

    //Configuration Handling
    public void readConfig() {
        config.load();
        
        config.setProperty("parts.slotmanager", (config.getProperty("parts.slotmanager") != null ? config.getProperty("parts.slotmanager") : "false" ));
        config.setProperty("parts.playerlist", (config.getProperty("parts.playerlist") != null ? config.getProperty("parts.playerlist") : "false" ));
        
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
        String helpText = util.colorize(prefixMsg+"/"+alias+" (parts|slotmanager) <arguments...>");
        if (args.length == 0) { cs.sendMessage(helpText); } else {
            String[] newArgs = new String[args.length-1];
            for (int pos = 1; pos < args.length; pos++)
                newArgs[pos-1] = args[pos];
            if (args[0].equalsIgnoreCase("parts")) {
                return nullPartsCommand(cs,cmnd,alias,newArgs);
            } else if (args[0].equalsIgnoreCase("slotmanager")) {
                return partSlotManager.onCommand(cs,cmnd,alias,newArgs);
            } else { cs.sendMessage(helpText); }
        }
        return false;
    }
    
    private static boolean nullPartsCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" parts <enable|disable|reload> <arguments...>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.parts")) {
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
        } else { cs.sendMessage(permText); }
        return false;
    }
    private static boolean nullPartsEnableCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" parts enable (part)");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.parts.state")) {
            if (args.length == 0) { cs.sendMessage(helpText); } else {
                if (args[0].equalsIgnoreCase("slotmanager")) {
                    if (cs.hasPermission("ne.slotmanager.state")) {
                        enableSlotManager = true;
                        partSlotManager.onEnable();
                        cs.sendMessage(util.colorize(prefixMsg+"Slot Manager enabled."));
                        return true;
                    } else { cs.sendMessage(permText); }
                } else { cs.sendMessage(helpText); }
            }
        } else { cs.sendMessage(permText); }
        return false;
    }
    private static boolean nullPartsDisableCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" parts disable (part)");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.parts.state")) {
            if (args.length == 0) { cs.sendMessage(helpText); } else {
                if (args[0].equalsIgnoreCase("slotmanager")) {
                    if (cs.hasPermission("ne.slotmanager.state")) {
                        partSlotManager.onDisable();
                        enableSlotManager = false;
                        cs.sendMessage(util.colorize(prefixMsg+"Slot Manager disabled."));
                        return true;
                    } else { cs.sendMessage(permText); }
                } else { cs.sendMessage(helpText); }
            }
        } else { cs.sendMessage(permText); }
        return false;
    }
    private static boolean nullPartsReloadCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.parts.reload")) {
            cs.sendMessage(util.colorize(prefixMsg+"Disabling parts..."));
            partSlotManager.onDisable();
            cs.sendMessage(util.colorize(prefixMsg+"Reloading configuration..."));
            config.load();
            cs.sendMessage(util.colorize(prefixMsg+"Enabling parts..."));
            partSlotManager.onEnable();
            cs.sendMessage(util.colorize(prefixMsg+"Done!"));
            return true;
        } else { cs.sendMessage(permText); }
        return false;
    }
}
