/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.michaeldirks.plugins.nullessentials.util;

import java.util.ArrayList;
import me.michaeldirks.plugins.nullessentials.NullEssentials;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Xaymar
 */
public class util {

    public static String substitude(String On, String[] What, String[] With) {
        if (What.length != With.length) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }

        for (int count = 0; count < What.length; count++) {
            if (What[count].contains(",")) {
                String[] WhatArgs = What[count].split(",");
                for (String arg : WhatArgs) {
                    On = On.replace(arg, With[count]);
                }
            } else {
                On = On.replace(What[count], With[count]);
            }
        }

        return On;
    }

    public static String colorize(String On) {
        return On.replaceAll("([^&])(&([a-fA-F0-9]))", "$1\u00A7$3").replace("&&","&");
    }

    public static String parsePlayer(String On, Player plr) {
        return colorize(util.substitude(On,
                new String[]{
                    "+n,+name",
                    "+d,+displayname",
                    "+w,+world",
                    "+t,+time",
                    "+l,+location",
                    "+x",
                    "+y",
                    "+z",}, new String[]{
                    plr.getName(),
                    plr.getDisplayName(),
                    plr.getWorld().getName(),
                    String.valueOf(plr.getWorld().getTime()),
                    String.valueOf(plr.getLocation().getX()) + "x, " + String.valueOf(plr.getLocation().getY()) + "y, " + String.valueOf(plr.getLocation().getZ()) + "z",
                    String.valueOf(plr.getLocation().getX()),
                    String.valueOf(plr.getLocation().getY()),
                    String.valueOf(plr.getLocation().getZ())
                }));
    }

    public static void broadcast(String msg) {
        util.broadcast(msg, NullEssentials.server, new Player[]{});
    }

    public static void broadcast(String msg, Server srv) {
        util.broadcast(msg, srv, new Player[]{});
    }

    public static void broadcast(String msg, Server srv, Player[] but) {
        for (Player plr : srv.getOnlinePlayers()) {
            for (Player chk : but) {
                if (chk == plr) {
                    continue;
                }
            }
            util.sendMessage(plr, msg);
        }
    }
    
    public static void sendMessage(CommandSender cs, String msg) {
        if (cs instanceof Player) {
            cs.sendMessage(util.parsePlayer(util.colorize(msg), (Player)cs));
        } else {
            cs.sendMessage(util.colorize(msg));
        }
    }

    public static Player findPlayerName(String name) {
        for (Player plr : NullEssentials.server.getOnlinePlayers()) {
            if (plr.getName().contains(name)) {
                return plr;
            }
        }
        return null;
    }

    public static Player findPlayerDisplayName(String name) {
        for (Player plr : NullEssentials.server.getOnlinePlayers()) {
            if (plr.getDisplayName().contains(name)) {
                return plr;
            }
        }
        return null;
    }

    public static String[] reparseArgs(String[] args) {
        String fullArgs = arrayCombine(args, " ");
        char[] charArgs = fullArgs.toCharArray();
        ArrayList<String> newArgs = new ArrayList<String>();

        newArgs.clear();

        if (charArgs.length >= 1) {
            String toAdd = new String();
            boolean isQuoted = false;
            boolean isQuotedDual = false; //false = ', true = ";
            for (int i = 0; i < charArgs.length; i++) {
                if (isQuoted == true) {
                    if ((("\"".equals(Character.toString(charArgs[i]))) && (isQuotedDual == true)) || (("'".equals(Character.toString(charArgs[i]))) && (isQuotedDual == false))) {
                        isQuoted = false;
                        isQuotedDual = false;
                        newArgs.add(toAdd);
                        toAdd = "";
                    } else {
                        toAdd += Character.toString(charArgs[i]);
                    }
                } else {
                    if (" ".equals(Character.toString(charArgs[i])) || "\t".equals(Character.toString(charArgs[i]))) {
                        newArgs.add(toAdd);
                        toAdd = "";
                    } else if ("\"".equals(Character.toString(charArgs[i])) || "'".equals(Character.toString(charArgs[i]))) {
                        isQuotedDual = ("'".equals(Character.toString(charArgs[i])) == true ? false : true);
                        if (i > 0) {
                            if (" ".equals(Character.toString(charArgs[i - 1]))) {
                                isQuoted = true;
                                toAdd = "";
                            }
                        } else {
                            isQuoted = true;
                            toAdd = "";
                        }
                    } else {
                        toAdd += Character.toString(charArgs[i]);
                    }
                }
            }
            if (!"".equals(toAdd)) newArgs.add(toAdd);
        }
        String[] strArgs = new String[newArgs.size()];
        for (int i = 0; i < newArgs.size(); i++) {
            strArgs[i] = (String) newArgs.get(i);
        }

        return strArgs;
    }

    public static String[] arraySplit(String split, String delimiter) {
        return null;
    }

    public static String arrayCombine(String[] array, String delimiter) {
        String out = new String();
        if (array.length >= 1) {
            out += array[0];
            for (int i = 1; i < array.length; i++) {
                out += delimiter;
                out += array[i];
            }
        }
        return out;
    }
    
    public static boolean hasPermission(CommandSender cs, String node) {
        if (NullEssentials.pHandler != null) {
            //Use Permissions
            if (cs instanceof Player) {
                return NullEssentials.pHandler.has(((Player)cs).getWorld().getName(), ((Player)cs).getName(), node);
            } else if (cs instanceof Server) {
                return true;
            } else {
                return false;
            }
        } else {
            //Use SuperPermissions
            return cs.hasPermission(node);
        }
    }
}
