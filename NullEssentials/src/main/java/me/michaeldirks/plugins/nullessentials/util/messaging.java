/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.michaeldirks.plugins.nullessentials.util;

import me.michaeldirks.plugins.nullessentials.NullEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;


/**
 *
 * @author Xaymar
 */
public class messaging {
    public static String substitude(String On, String[] What, String[] With) {
        if (What.length != With.length) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        
        for (int count = 0; count < What.length; count++) {
            if (What[count].contains(",")) {
                String[] WhatArgs = What[count].split(",");
                for(String arg : WhatArgs)
                    On = On.replace(arg, With[count]);
            } else {
                On = On.replace(What[count], With[count]);
            }
        }
        
        return On;
    }
    
    //Reachable
    public static String colorize(String On) {
        return On.replaceAll("(&([A-Fa-f0-9]))", "\u00A7$2");
    }
    public static String parsePlayer(String On, Player plr) {
        return messaging.substitude(On,
                new String[] {
                    "+n,+name",
                    "+d,+displayname",
                    "+w,+world",
                    "+t,+time",
                    "+l,+location",
                    "+x",
                    "+y",
                    "+z",
                }, new String[] {
                    plr.getName(),
                    plr.getDisplayName(),
                    plr.getWorld().getName(),
                    String.valueOf(plr.getWorld().getTime()),
                    String.valueOf(plr.getLocation().getX())+"x, "+String.valueOf(plr.getLocation().getY())+"y, "+String.valueOf(plr.getLocation().getZ())+"z",
                    String.valueOf(plr.getLocation().getX()),
                    String.valueOf(plr.getLocation().getY()),
                    String.valueOf(plr.getLocation().getZ())
                });
    }
    
    public static void broadcast(String msg) {
        messaging.broadcast(msg,NullEssentials.server,new Player[] {});
    }
    public static void broadcast(String msg, Server srv) {
        messaging.broadcast(msg,srv,new Player[] {});
    }
    public static void broadcast(String msg, Server srv, Player[] but) {
        for(Player plr : srv.getOnlinePlayers()) {
            for (Player chk : but) {
                if (chk == plr) {
                    continue;
                }
            }
            plr.sendMessage(msg);
        }
    }
}
