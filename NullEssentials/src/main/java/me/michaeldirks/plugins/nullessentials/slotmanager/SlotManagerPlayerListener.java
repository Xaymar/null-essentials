/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.michaeldirks.plugins.nullessentials.slotmanager;

import java.util.LinkedList;
import java.util.Random;
import me.michaeldirks.plugins.nullessentials.NullEssentials;
import me.michaeldirks.plugins.nullessentials.util.messaging;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 *
 * @author Xaymar
 */
public class SlotManagerPlayerListener extends PlayerListener {
    public void onEnable() {
        if (NullEssentials.enableSlotManager == true) {
            NullEssentials.server.getPluginManager().registerEvent(Type.PLAYER_LOGIN, (PlayerListener)this, Priority.Highest, NullEssentials.plugin);
            NullEssentials.server.getPluginManager().registerEvent(Type.PLAYER_JOIN, (PlayerListener)this, Priority.Lowest, NullEssentials.plugin);
        }
    }
    
    public void onDisable() {
        if (NullEssentials.enableSlotManager == true) {
            
        }
    }
    
    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        boolean permCheck = false;
        
        if (NullEssentials.config.getBoolean("slotmanager.reserved.allowplayers", true) == false) {
            int usedFreeSlots = 0;
            int totalFreeSlots = NullEssentials.server.getMaxPlayers()-NullEssentials.config.getInt("slots.reserved.count", 0);
            for (int i = 0;i < NullEssentials.server.getMaxPlayers();i++) {
                Player player = NullEssentials.server.getOnlinePlayers()[i];
                if ((!player.hasPermission("ne.slotmanager.reserved")) && (player != event.getPlayer())) {
                    usedFreeSlots++;
                }
            }
            if (usedFreeSlots >= totalFreeSlots) {
                permCheck = true;
            }
        } else {
            if (NullEssentials.server.getMaxPlayers() == NullEssentials.server.getOnlinePlayers().length) {
                permCheck = true;
            }
        }
        if (permCheck == true) {
            if (event.getPlayer().hasPermission("ne.slotmanager.reserved")) {
                int usedReservedSlots = 0;
                int totalReservedSlots = NullEssentials.config.getInt("slotmanager.reserved.count", 0);
                for (int i = 0;i < NullEssentials.server.getMaxPlayers();i++) {
                    Player player = NullEssentials.server.getOnlinePlayers()[i];
                    if ((player.hasPermission("ne.slotmanager.reserved")) && (player != event.getPlayer())) {
                        usedReservedSlots++;
                    }
                }
                if ((usedReservedSlots >= totalReservedSlots) || !((totalReservedSlots == -1) && (usedReservedSlots == NullEssentials.server.getMaxPlayers()))) {
                    event.setResult(PlayerLoginEvent.Result.KICK_FULL);
                    event.setKickMessage("No reserved slots left!");
                } else {
                    LinkedList<Player> kickList = new LinkedList<Player>();
                    Random randomGen = new Random( System.currentTimeMillis() );
                    for (int i = 0;i < NullEssentials.server.getMaxPlayers();i++) {
                        Player player = NullEssentials.server.getOnlinePlayers()[i];
                        if ((!player.hasPermission("ne.slotmanager.reserved")) && (player != event.getPlayer())) {
                            kickList.add(player);
                        }
                    }
                    Player toKick = kickList.get(randomGen.nextInt(kickList.size()));
                    if (toKick != null) {
                        toKick.kickPlayer("Kicked by reserved slot user.");
                    } else {
                        event.setResult(PlayerLoginEvent.Result.KICK_FULL);
                        event.setKickMessage("No reserved slots left!");
                    }
                }
            } else {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage("You do not have access to reserved slots.");
            }
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("ne.slotmanager.message")) {
            String strMessage = NullEssentials.config.getString("slotmanager.join."+event.getPlayer().getName()+".message", "");
            if (!strMessage.equals("")) {
                messaging.broadcast(strMessage); //messaging.parsePlayer(strMessage, event.getPlayer())
            }
        }
    }
}
