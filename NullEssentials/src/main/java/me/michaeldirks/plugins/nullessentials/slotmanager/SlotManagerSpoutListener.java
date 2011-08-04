/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.michaeldirks.plugins.nullessentials.slotmanager;

import me.michaeldirks.plugins.nullessentials.NullEssentials;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;

/**
 *
 * @author Xaymar
 */
public class SlotManagerSpoutListener extends SpoutListener {
    public void onEnable() {
        if (NullEssentials.enableSlotManager == true) {
            NullEssentials.server.getPluginManager().registerEvent(Type.CUSTOM_EVENT, (SpoutListener)this, Priority.Highest, NullEssentials.plugin);
        }
    }
    
    public void onDisable() {
        if (NullEssentials.enableSlotManager == true) {
            
        }
    }
    
    @Override
    public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
        if (event.getPlayer().hasPermission("ne.slotmanager.sound")) {
            String strSound = NullEssentials.config.getString("slotmanager.join."+event.getPlayer().getName()+".sound", "");
            if (NullEssentials.enableSpout) {
                if (!strSound.equals("")) {
                    SpoutManager.getSoundManager().playGlobalCustomSoundEffect(NullEssentials.plugin, strSound, false);
                }
            }
        }
    }
}
