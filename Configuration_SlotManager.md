

# Slot Manager #
Slot Manager is a part of Null Essentials that allows your Server to have reserved Slots, custom join messages, leave messages and join sounds.

## Configuration ##
If we take a look at the configuration there are two parts that are important to the Slot Manager:
  * parts
  * slotmanager
To enable slotmanager we have to set parts.slotmanager to true, we can do that ingame though(/ne parts enable|disable slotmanager).
Then we have slotmanager, the main part for Slot Manager configuration.
This should have at least two nodes, named "reserved" and "extra".

### reserved ###
Let's take a look at the reserved node, which contains "count" and "allowplayers".
"count" tells Slot Manager how many slots to mark as reserved. Possible values are 0-MaxPlayers and -1, while -1 means all.
"allowplayers" tells Slot Manager if normal players are allowed into the reserved slots. Possible values are true and false, while true blocks players that try to join reserved slots and false allows them to join those.

### extra ###
These define what player has which messages and sound. The notation is easy:
```
slotmanager:
...
  extra:
    PLAYERNAME:
      join: JOINMESSAGE
      leave: LEAVEMESSAGE
      sound: JOINSOUND
```
"join" and "leave" support all colorcodes(&0-F) and the following:
  * _+n +name_ : The name of the player.
  * _+d +displayname_ : The display name of the player.
  * _+w +world_ : The name of the world the player is in.
  * _+t +time_ : The time of the world the player is in.
  * _+l +location_ : The location of the player as "x, y, z".
  * _+x +y +z_ : The single parts of the location of the player.
"sound" does not support colorcodes and the above due to url construct like these: http://example.org/+worldtime/arb.wav, which would result in http://example.org/MYWORLDtime/arb.wav(if your world is named MYWORLD).

## Permissions ##
| Node | For | Parents |
|:-----|:----|:--------|
| ne.slotmanager.state | /ne parts <enable|disable> slotmanager | ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.reserved | Access to reserved slots | ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.message | Allow user to have a custom join message | ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.sound | Allow user to have a custom join sound | ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.set.sound.own | /ne slotmanager set sound `<`url`>` | ne.slotmanager.set.sound.`*`, ne.slotmanager.set.`*`, ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.set.sound.other | /ne slotmanager set sound `<`player`>` `<`url`>` | ne.slotmanager.set.sound.`*`, ne.slotmanager.set.`*`, ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.get.sound.own | /ne slotmanager get sound | ne.slotmanager.get.sound.`*`, ne.slotmanager.get.`*`, ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.get.sound.other | /ne slotmanager get sound `<`player`>` | ne.slotmanager.get.sound.`*`, ne.slotmanager.get.`*`, ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.set.message.own | /ne slotmanager set join|leave `<`text`>` | ne.slotmanager.set.message.`*`, ne.slotmanager.set.`*`, ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.set.message.other | /ne slotmanager set join|leave `<`player`>` `<`text`>` | ne.slotmanager.set.message.`*`, ne.slotmanager.set.`*`, ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.get.message.own | /ne slotmanager get join|leave | ne.slotmanager.get.message.`*`, ne.slotmanager.get.`*`, ne.slotmanager.`*`, ne.`*` |
| ne.slotmanager.get.message.other | /ne slotmanager get join|leave `<`player`>` | ne.slotmanager.get.message.`*`, ne.slotmanager.get.`*`, ne.slotmanager.`*`, ne.`*` |