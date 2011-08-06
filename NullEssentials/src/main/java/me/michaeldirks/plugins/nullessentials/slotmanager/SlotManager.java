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
import org.bukkit.util.config.Configuration;

/**
 *
 * @author Xaymar
 */
public class SlotManager {

    public static String prefixStd = NullEssentials.prefixStd + "[SM]";
    public static String prefixMsg = NullEssentials.prefixMsg + "[SM]";
    private static smPlayerListener playerListener = new smPlayerListener();
    private static smSpoutListener spoutListener = new smSpoutListener();

    public void onEnable() {
        if (NullEssentials.enableSlotManager == true) {
            playerListener.onEnable();
            spoutListener.onEnable();
        }
    }

    public void onDisable() {
        if (NullEssentials.enablePlayerList == true) {
            playerListener.onDisable();
            spoutListener.onDisable();
        }
    }

    public void readConfig(Configuration config) {
    }

    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        if (NullEssentials.enableSlotManager == false) {
            return false;
        }

        String helpText = prefixMsg + "/" + alias + " slotmanager (set|get) <arguments...>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (args.length == 0) {
            util.sendMessage(cs, helpText);
        } else {
            String[] newArgs = new String[args.length - 1];
            for (int pos = 1; pos < args.length; pos++) {
                newArgs[pos - 1] = args[pos];
            }
            if (args[0].equalsIgnoreCase("set")) {
                return slotManagerSetCommand(cs, cmnd, alias, newArgs);
            } else if (args[0].equalsIgnoreCase("get")) {
                return slotManagerGetCommand(cs, cmnd, alias, newArgs);
            } else {
                util.sendMessage(cs, helpText);
            }
        }
        return false;
    }

    private boolean slotManagerGetCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get (sound|join|leave) <arguments...>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (cs.hasPermission("ne.slotmanager.get.message.own") || cs.hasPermission("ne.slotmanager.get.message.other")
                || cs.hasPermission("ne.slotmanager.get.sound.own") || cs.hasPermission("ne.slotmanager.get.sound.other")) {
            if (args.length == 0) {
                util.sendMessage(cs, helpText);
            } else {
                String[] newArgs = new String[args.length - 1];
                for (int pos = 1; pos < args.length; pos++) {
                    newArgs[pos - 1] = args[pos];
                }
                if (args[0].equalsIgnoreCase("sound")) {
                    return slotManagerGetSoundCommand(cs, cmnd, alias, newArgs);
                } else if (args[0].equalsIgnoreCase("join")) {
                    return slotManagerGetJoinCommand(cs, cmnd, alias, newArgs);
                } else if (args[0].equalsIgnoreCase("leave")) {
                    return slotManagerGetLeaveCommand(cs, cmnd, alias, newArgs);
                } else {
                    util.sendMessage(cs, helpText);
                }
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetSoundCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get sound <player>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (cs.hasPermission("ne.slotmanager.get.sound.own") || cs.hasPermission("ne.slotmanager.get.sound.other")) {
            if (args.length == 0) {
                return slotManagerGetSoundOwnCommand(cs, cmnd, alias, args);
            } else {
                return slotManagerGetSoundOtherCommand(cs, cmnd, alias, args);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetSoundOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get sound";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cYou are not a player.";
        if (cs.hasPermission("ne.slotmanager.get.sound.own")) {
            if (cs instanceof Player) {
                util.sendMessage(cs, prefixMsg + "Your own Sound is: '" + NullEssentials.config.getString("slotmanager.join." + ((Player) cs).getName() + ".sound", "") + "'");
                return true;
            } else {
                util.sendMessage(cs, failText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetSoundOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get sound <player>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cCould not find Player.";
        if (cs.hasPermission("ne.slotmanager.get.sound.other")) {
            if (args.length == 0) {
                util.sendMessage(cs, helpText);
            } else {
                util.sendMessage(cs, prefixMsg + args[0] + "'s Sound is: '" + NullEssentials.config.getString("slotmanager.join." + args[0] + ".sound", "") + "'");
                return true;
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetJoinCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get join <player>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (cs.hasPermission("ne.slotmanager.get.message.own") || cs.hasPermission("ne.slotmanager.get.message.other")) {
            if (args.length == 0) {
                return slotManagerGetJoinOwnCommand(cs, cmnd, alias, args);
            } else {
                return slotManagerGetJoinOtherCommand(cs, cmnd, alias, args);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetJoinOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get join";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cYou are not a player.";
        if (cs.hasPermission("ne.slotmanager.get.message.own")) {
            if (cs instanceof Player) {
                util.sendMessage(cs, prefixMsg + "Your own join message is: '" + NullEssentials.config.getString("slotmanager.extra." + ((Player) cs).getName() + ".join", "") + "'");
                return true;
            } else {
                util.sendMessage(cs, failText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetJoinOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get join <player>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cCould not find Player.";
        if (cs.hasPermission("ne.slotmanager.get.message.other")) {
            if (args.length == 0) {
                util.sendMessage(cs, helpText);
            } else {
                util.sendMessage(cs, prefixMsg + args[0] + "'s join message is: '" + NullEssentials.config.getString("slotmanager.extra." + args[0] + ".join", "") + "'");
                return true;
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetLeaveCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get leave <player>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (cs.hasPermission("ne.slotmanager.get.leave.own") || cs.hasPermission("ne.slotmanager.get.leave.other")) {
            if (args.length == 0) {
                return slotManagerGetLeaveOwnCommand(cs, cmnd, alias, args);
            } else {
                return slotManagerGetLeaveOtherCommand(cs, cmnd, alias, args);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetLeaveOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get leave";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cYou are not a player.";
        if (cs.hasPermission("ne.slotmanager.get.message.own")) {
            if (cs instanceof Player) {
                util.sendMessage(cs, prefixMsg + "Your own leave message is: '" + NullEssentials.config.getString("slotmanager.extra." + ((Player) cs).getName() + ".leave", "") + "'");
                return true;
            } else {
                util.sendMessage(cs, failText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerGetLeaveOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager get leave <player>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cCould not find Player.";
        if (cs.hasPermission("ne.slotmanager.get.message.other")) {
            if (args.length == 0) {
                util.sendMessage(cs, helpText);
            } else {
                util.sendMessage(cs, prefixMsg + args[0] + "'s leave message is: '" + NullEssentials.config.getString("slotmanager.extra." + args[0] + ".leave", "") + "'");
                return true;
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set (sound|join|leave) <arguments...>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (cs.hasPermission("ne.slotmanager.set.message.own") || cs.hasPermission("ne.slotmanager.set.message.other")
                || cs.hasPermission("ne.slotmanager.set.sound.own") || cs.hasPermission("ne.slotmanager.set.sound.other")) {
            if (args.length == 0) {
                util.sendMessage(cs, helpText);
            } else {
                String[] newArgs = new String[args.length - 1];
                for (int pos = 1; pos < args.length; pos++) {
                    newArgs[pos - 1] = args[pos];
                }
                if (args[0].equalsIgnoreCase("sound")) {
                    return slotManagerSetSoundCommand(cs, cmnd, alias, newArgs);
                } else if (args[0].equalsIgnoreCase("join")) {
                    return slotManagerSetJoinCommand(cs, cmnd, alias, newArgs);
                } else if (args[0].equalsIgnoreCase("leave")) {
                    return slotManagerSetLeaveCommand(cs, cmnd, alias, newArgs);
                } else {
                    util.sendMessage(cs, helpText);
                }
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetSoundCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set sound <player|url> <arguments...>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (cs.hasPermission("ne.slotmanager.set.sound.own") || cs.hasPermission("ne.slotmanager.set.sound.other")) {
            if (args.length == 1) {
                return slotManagerSetSoundOwnCommand(cs, cmnd, alias, args);
            } else if (args.length >= 2) {
                return slotManagerSetSoundOtherCommand(cs, cmnd, alias, args);
            } else {
                util.sendMessage(cs, helpText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetSoundOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set sound <url>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cYou are not a player.";
        if (cs.hasPermission("ne.slotmanager.set.sound.own")) {
            if (cs instanceof Player) {
                if (args.length == 0) {
                    util.sendMessage(cs, helpText);
                } else {
                    NullEssentials.config.setProperty("slotmanager.join." + ((Player) cs).getName() + ".sound", args[0]);
                    NullEssentials.config.save();
                    util.sendMessage(cs, prefixMsg + "Your join sound is now: '" + args[0] + "'");
                    return true;
                }
            } else {
                util.sendMessage(cs, failText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetSoundOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set sound <player> <url>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cCould not find Player.";
        if (cs.hasPermission("ne.slotmanager.set.sound.other")) {
            if (args.length <= 1) {
                util.sendMessage(cs, helpText);
            } else {
                NullEssentials.config.setProperty("slotmanager.join." + args[0] + ".sound", args[1]);
                NullEssentials.config.save();
                util.sendMessage(cs, prefixMsg + args[0] + "'s join sound is now: '" + args[1] + "'");
                return true;
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetJoinCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set join <player|text> <arguments...>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (cs.hasPermission("ne.slotmanager.set.message.own") || cs.hasPermission("ne.slotmanager.set.message.other")) {
            if (args.length == 1) {
                return slotManagerSetJoinOwnCommand(cs, cmnd, alias, args);
            } else if (args.length >= 2) {
                return slotManagerSetJoinOtherCommand(cs, cmnd, alias, args);
            } else {
                util.sendMessage(cs, helpText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetJoinOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set join <text>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cYou are not a player.";
        if (cs.hasPermission("ne.slotmanager.set.message.own")) {
            if (cs instanceof Player) {
                if (args.length == 0) {
                    util.sendMessage(cs, helpText);
                } else {
                    NullEssentials.config.setProperty("slotmanager.extra." + ((Player) cs).getName() + ".join", args[0]);
                    NullEssentials.config.save();
                    util.sendMessage(cs, prefixMsg + "Your join message is now: '" + args[0] + "'");
                    return true;
                }
            } else {
                util.sendMessage(cs, failText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetJoinOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set join <player> <text>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cCould not find Player.";
        if (cs.hasPermission("ne.slotmanager.set.message.other")) {
            if (args.length <= 1) {
                util.sendMessage(cs, helpText);
            } else {
                NullEssentials.config.setProperty("slotmanager.extra." + args[0] + ".join", args[1]);
                NullEssentials.config.save();
                util.sendMessage(cs, prefixMsg + args[0] + "'s join message is now: '" + args[1] + "'");
                return true;
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetLeaveCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set leave <player|text> <arguments...>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        if (cs.hasPermission("ne.slotmanager.set.message.own") || cs.hasPermission("ne.slotmanager.set.message.other")) {
            if (args.length == 1) {
                return slotManagerSetLeaveOwnCommand(cs, cmnd, alias, args);
            } else if (args.length >= 2) {
                return slotManagerSetLeaveOtherCommand(cs, cmnd, alias, args);
            } else {
                util.sendMessage(cs, helpText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetLeaveOwnCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set leave <text>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cYou are not a player.";
        if (cs.hasPermission("ne.slotmanager.set.message.own")) {
            if (cs instanceof Player) {
                if (args.length == 0) {
                    util.sendMessage(cs, helpText);
                } else {
                    NullEssentials.config.setProperty("slotmanager.extra." + ((Player) cs).getName() + ".leave", args[0]);
                    NullEssentials.config.save();
                    util.sendMessage(cs, prefixMsg + "Your leave message is now: '" + args[0] + "'");
                    return true;
                }
            } else {
                util.sendMessage(cs, failText);
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }

    private boolean slotManagerSetLeaveOtherCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        String helpText = prefixMsg + "/" + alias + " slotmanager set leave <player> <text>";
        String permText = prefixMsg + "&cYou do not have permission to do that.";
        String failText = prefixMsg + "&cCould not find Player.";
        if (cs.hasPermission("ne.slotmanager.set.message.other")) {
            if (args.length <= 1) {
                util.sendMessage(cs, helpText);
            } else {
                NullEssentials.config.setProperty("slotmanager.extra." + args[0] + ".leave", args[1]);
                NullEssentials.config.save();
                util.sendMessage(cs, prefixMsg + args[0] + "' leave message is now: '" + args[1] + "'");
                return true;
            }
        } else {
            util.sendMessage(cs, permText);
        }
        return false;
    }
}
