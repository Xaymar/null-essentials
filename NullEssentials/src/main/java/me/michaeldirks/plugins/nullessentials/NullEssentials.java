package me.michaeldirks.plugins.nullessentials;

import me.michaeldirks.plugins.nullessentials.slotmanager.SlotManagerPlayerListener;
import me.michaeldirks.plugins.nullessentials.slotmanager.SlotManagerSpoutListener;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class NullEssentials extends JavaPlugin {
    public static NullEssentials plugin = null;
    public static Server server = null;
    public static Configuration config = null;
    public static String prefixStd = "[NULL]";
    public static String prefixMsg = "[NULL]";
    
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
        config = this.getConfiguration(); config.load();
        System.out.println(prefixStd+"Enabling...");
        
        enableSpout = (server.getPluginManager().getPlugin("Spout") != null);
        if (enableSpout == true) System.out.println(prefixStd+"Spout is installed! Continueing happily.");
        
        //Find enabled parts
        enableSlotManager = config.getBoolean("parts.slotmanager", false);
        
        //Run onEnable for all known parts
        if (enableSlotManager) {
            partsSlotManager_PlayerListener.onEnable();
            partsSlotManager_SpoutListener.onEnable();
        }
        
        System.out.println(prefixStd+this+" is now enabled!");
    }
}

