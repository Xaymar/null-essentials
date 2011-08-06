/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.michaeldirks.plugins.nullessentials.slotmanager;

import java.util.LinkedList;
import java.util.Random;
import me.michaeldirks.plugins.nullessentials.NullEssentials;
import me.michaeldirks.plugins.nullessentials.util.util;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Xaymar
 */
public class smPlayerListener extends PlayerListener {

    public void onEnable() {
        NullEssentials.server.getPluginManager().registerEvent(Type.PLAYER_LOGIN, (PlayerListener) this, Priority.Highest, NullEssentials.plugin);
        NullEssentials.server.getPluginManager().registerEvent(Type.PLAYER_JOIN, (PlayerListener) this, Priority.Highest, NullEssentials.plugin);
    }

    public void onDisable() {
    }

    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (NullEssentials.enableSlotManager == false) {
            return;
        }

        boolean permCheck = false;
        if (NullEssentials.config.getBoolean("slotmanager.reserved.allowplayers", true) == false) {
            int usedFreeSlots = 0;
            int totalFreeSlots = (NullEssentials.config.getInt("slots.reserved.count", 0) == -1 ? 0 : NullEssentials.server.getMaxPlayers() - NullEssentials.config.getInt("slots.reserved.count", 0));
            for (Player player : NullEssentials.server.getOnlinePlayers()) {
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
                int totalReservedSlots = (NullEssentials.config.getInt("slots.reserved.count", 0) == -1 ? NullEssentials.server.getMaxPlayers() : NullEssentials.config.getInt("slotmanager.reserved.count", 0));
                for (Player player : NullEssentials.server.getOnlinePlayers()) {
                    if ((player.hasPermission("ne.slotmanager.reserved")) && (player != event.getPlayer())) {
                        usedReservedSlots++;
                    }
                }
                if ((usedReservedSlots >= totalReservedSlots) || !((totalReservedSlots == -1) && (usedReservedSlots == NullEssentials.server.getMaxPlayers()))) {
                    event.setResult(PlayerLoginEvent.Result.KICK_FULL);
                    event.setKickMessage("No reserved slots left!");
                } else {
                    LinkedList<Player> kickList = new LinkedList<Player>();
                    Random randomGen = new Random(System.currentTimeMillis());
                    for (Player player : NullEssentials.server.getOnlinePlayers()) {
                        if ((!player.hasPermission("ne.slotmanager.reserved")) && (player != event.getPlayer())) {
                            kickList.add(player);
                        }
                    }
                    Player toKick = kickList.get(randomGen.nextInt(kickList.size()));
                    if (toKick != null) {
                        toKick.kickPlayer("Kicked by reserved slot user.");
                        event.setResult(PlayerLoginEvent.Result.ALLOWED);
                    } else {
                        event.setResult(PlayerLoginEvent.Result.KICK_FULL);
                        event.setKickMessage("No reserved slots left!");
                    }
                }
            } else {
                event.setResult(PlayerLoginEvent.Result.KICK_FULL);
                event.setKickMessage("You do not have access to reserved slots.");
            }
        }
    }

    @Override
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (NullEssentials.enableSlotManager == false) {
            return;
        }

        if (event.getPlayer().hasPermission("ne.slotmanager.message")) {
            event.setJoinMessage("");
            NullEssentials.server.getScheduler().scheduleSyncDelayedTask(NullEssentials.plugin, new Runnable() {

                private final Player plr = event.getPlayer();

                @Override
                public void run() {
                    String strMessage = NullEssentials.config.getString("slotmanager.extra." + plr.getName() + ".join", "");
                    if (!strMessage.equals("")) {
                        util.broadcast(util.parsePlayer(strMessage, plr));
                    }
                }
            });
        }
    }

    @Override
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (NullEssentials.enableSlotManager == false) {
            return;
        }

        if (event.getPlayer().hasPermission("ne.slotmanager.message")) {
            event.setQuitMessage("");
            NullEssentials.server.getScheduler().scheduleSyncDelayedTask(NullEssentials.plugin, new Runnable() {

                private final Player plr = event.getPlayer();

                @Override
                public void run() {
                    String strMessage = NullEssentials.config.getString("slotmanager.extra." + plr.getName() + ".leave", "");
                    if (!strMessage.equals("")) {
                        util.broadcast(util.parsePlayer(strMessage, plr));
                    }
                }
            });
        }
    }
}
