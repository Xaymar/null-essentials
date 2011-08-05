/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.michaeldirks.plugins.nullessentials.slotmanager;

import me.michaeldirks.plugins.nullessentials.NullEssentials;
import me.michaeldirks.plugins.nullessentials.util.util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Xaymar
 */
public class SlotManagerCommands {
    private static String prefixStd = NullEssentials.prefixStd+"[SM]";
    private static String prefixMsg = NullEssentials.prefixMsg+"[SM]";
    
    public static boolean slotManagerCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager (set|get) <arguments...>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (args.length == 0) { cs.sendMessage(helpText); } else {
            String[] newArgs = new String[args.length-1];
            for (int pos = 1; pos < args.length; pos++)
                newArgs[pos-1] = args[pos];
            if (args[0].equalsIgnoreCase("set")) {
                return slotManagerSetCommand(cs,cmnd,alias,newArgs);
            } else if (args[0].equalsIgnoreCase("get")) {
                return slotManagerGetCommand(cs,cmnd,alias,newArgs);
            } else { cs.sendMessage(helpText); }
        }
        return false;
    }
    
    private static boolean slotManagerGetCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager get (sound|message) <arguments...>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.slotmanager.get.message.own") || cs.hasPermission("ne.slotmanager.get.message.other")
                || cs.hasPermission("ne.slotmanager.get.sound.own") || cs.hasPermission("ne.slotmanager.get.sound.other")) {
            if (args.length == 0) { cs.sendMessage(helpText); } else {
                String[] newArgs = new String[args.length-1];
                for (int pos = 1; pos < args.length; pos++)
                    newArgs[pos-1] = args[pos];
                if (args[0].equalsIgnoreCase("sound")) {
                    return slotManagerGetSoundCommand(cs,cmnd,alias,newArgs);
                } else if (args[0].equalsIgnoreCase("message")) {
                    return slotManagerGetMessageCommand(cs,cmnd,alias,newArgs);
                } else { cs.sendMessage(helpText); }
            }
        } else { cs.sendMessage(permText); }
        return false;
    }
    
    private static boolean slotManagerGetSoundCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager get sound <player>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.slotmanager.get.sound.own") || cs.hasPermission("ne.slotmanager.get.sound.other")) {
            if (args.length == 0) {
                return slotManagerGetSoundOwnCommand(cs,cmnd,alias,args);
            } else {
                return slotManagerGetSoundOtherCommand(cs,cmnd,alias,args);
            }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerGetSoundOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager get sound");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        String failText = util.colorize(prefixMsg+"&cYou are not a player.");
        if (cs.hasPermission("ne.slotmanager.get.sound.own")) {
            if (cs instanceof Player) {
                cs.sendMessage(prefixMsg+"Your own Sound is: '"+NullEssentials.config.getString("slotmanager.join."+((Player)cs).getName()+".sound","")+"'");
                return true;
            } else { cs.sendMessage(failText); }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerGetSoundOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager get sound <player>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        String failText = util.colorize(prefixMsg+"&cCould not find Player.");
        if (cs.hasPermission("ne.slotmanager.get.sound.other")) {
            if (args.length == 0) { cs.sendMessage(helpText); } else {
                cs.sendMessage(prefixMsg+args[0]+"'s Sound is: '"+NullEssentials.config.getString("slotmanager.join."+args[0]+".sound","")+"'");
                return true;
            }
        } else { cs.sendMessage(permText); }
        return false;
    }
    
    private static boolean slotManagerGetMessageCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager get message <player>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.slotmanager.get.message.own") || cs.hasPermission("ne.slotmanager.get.message.other")) {
            if (args.length == 0) {
                return slotManagerGetMessageOwnCommand(cs,cmnd,alias,args);
            } else {
                return slotManagerGetMessageOtherCommand(cs,cmnd,alias,args);
            }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerGetMessageOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager get message");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        String failText = util.colorize(prefixMsg+"&cYou are not a player.");
        if (cs.hasPermission("ne.slotmanager.get.message.own")) {
            if (cs instanceof Player) {
                cs.sendMessage(prefixMsg+"Your own Message is: '"+NullEssentials.config.getString("slotmanager.join."+((Player)cs).getName()+".message","")+"'");
                return true;
            } else { cs.sendMessage(failText); }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerGetMessageOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager get message <player>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        String failText = util.colorize(prefixMsg+"&cCould not find Player.");
        if (cs.hasPermission("ne.slotmanager.get.message.other")) {
            if (args.length == 0) { cs.sendMessage(helpText); } else {
                cs.sendMessage(prefixMsg+args[0]+"'s Message is: '"+NullEssentials.config.getString("slotmanager.join."+args[0]+".message","")+"'");
                return true;
            }
        } else { cs.sendMessage(permText); }
        return false;
    }
    
    private static boolean slotManagerSetCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager set (sound|message) <arguments...>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.slotmanager.set.message.own") || cs.hasPermission("ne.slotmanager.set.message.other")
                || cs.hasPermission("ne.slotmanager.set.sound.own") || cs.hasPermission("ne.slotmanager.set.sound.other")) {
            if (args.length == 0) { cs.sendMessage(helpText); } else {
                String[] newArgs = new String[args.length-1];
                for (int pos = 1; pos < args.length; pos++)
                    newArgs[pos-1] = args[pos];
                if (args[0].equalsIgnoreCase("sound")) {
                    return slotManagerSetSoundCommand(cs,cmnd,alias,newArgs);
                } else if (args[0].equalsIgnoreCase("message")) {
                    return slotManagerSetMessageCommand(cs,cmnd,alias,newArgs);
                } else { cs.sendMessage(helpText); }
            }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerSetSoundCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager set sound <player|url> <arguments...>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.slotmanager.set.sound.own") || cs.hasPermission("ne.slotmanager.set.sound.other")) {
            if (args.length == 1) {
                return slotManagerSetSoundOwnCommand(cs,cmnd,alias,args);
            } else if (args.length >= 2) {
                return slotManagerSetSoundOtherCommand(cs,cmnd,alias,args);
            } else { cs.sendMessage(helpText); }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerSetSoundOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager set sound <url>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        String failText = util.colorize(prefixMsg+"&cYou are not a player.");
        if (cs.hasPermission("ne.slotmanager.set.sound.own")) {
            if (cs instanceof Player) {
                if (args.length == 0) { cs.sendMessage(helpText); } else {
                    NullEssentials.config.setProperty("slotmanager.join."+((Player)cs).getName()+".sound", args[0]);
                    NullEssentials.config.save();
                    return true;
                }
            } else { cs.sendMessage(failText); }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerSetSoundOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager set sound <player> <url>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        String failText = util.colorize(prefixMsg+"&cCould not find Player.");
        if (cs.hasPermission("ne.slotmanager.set.sound.other")) {
            if (args.length <= 1) { cs.sendMessage(helpText); } else {
                NullEssentials.config.setProperty("slotmanager.join."+args[0]+".sound", args[1]);
                NullEssentials.config.save();
                return true;
            }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerSetMessageCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager set message <player|text> <arguments...>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        if (cs.hasPermission("ne.slotmanager.set.message.own") || cs.hasPermission("ne.slotmanager.set.message.other")) {
            if (args.length == 1) {
                return slotManagerSetMessageOwnCommand(cs,cmnd,alias,args);
            } else if (args.length >= 2) {
                return slotManagerSetMessageOtherCommand(cs,cmnd,alias,args);
            } else { cs.sendMessage(helpText); }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerSetMessageOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager set message <text>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        String failText = util.colorize(prefixMsg+"&cYou are not a player.");
        if (cs.hasPermission("ne.slotmanager.set.message.own")) {
            if (cs instanceof Player) {
                if (args.length == 0) { cs.sendMessage(helpText); } else {
                    NullEssentials.config.setProperty("slotmanager.join."+((Player)cs).getName()+".message", args[0]);
                    NullEssentials.config.save();
                    return true;
                }
            } else { cs.sendMessage(failText); }
        } else { cs.sendMessage(permText); }
        return false;
    }

    private static boolean slotManagerSetMessageOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = util.colorize(prefixMsg+"/"+alias+" slotmanager set message <player> <text>");
        String permText = util.colorize(prefixMsg+"&cYou do not have permission to do that.");
        String failText = util.colorize(prefixMsg+"&cCould not find Player.");
        if (cs.hasPermission("ne.slotmanager.set.message.other")) {
            if (args.length <= 1) { cs.sendMessage(helpText); } else {
                NullEssentials.config.setProperty("slotmanager.join."+args[0]+".message", args[1]);
                NullEssentials.config.save();
                return true;
            }
        } else { cs.sendMessage(permText); }
        return false;
    }
}
