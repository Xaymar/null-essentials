package me.michaeldirks.plugins.nullessentials;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import me.michaeldirks.plugins.nullessentials.slotmanager.SlotManagerPlayerListener;
import me.michaeldirks.plugins.nullessentials.slotmanager.SlotManagerSpoutListener;
import org.bukkit.Server;
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
    
    private static SlotManagerPlayerListener partsSlotManager_PlayerListener = new SlotManagerPlayerListener();
    private static SlotManagerSpoutListener partsSlotManager_SpoutListener = new SlotManagerSpoutListener();
    
    public void onDisable() {
        System.out.println(prefixStd+"Disabling...");
        
        partsSlotManager_PlayerListener.onDisable();
        partsSlotManager_SpoutListener.onDisable();
        
        System.out.println(prefixStd+this+" is now disabled!...");
    }

    public void onEnable() {
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
        
        log.log(Level.INFO, prefixStd+this+" is now enabled!");
    }
}
