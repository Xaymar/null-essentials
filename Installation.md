

# Null Essentials: Installation #
## Requirements ##
You need to have at least the following:
  * CraftBukkit 1000 or above
  * A Minecraft client or a Texteditor supporting UTF-8
However, for full functionality, these are required aswell:
  * [Spout](http://forums.bukkit.org/threads/spout.29259/) 1.0.1 or higher
  * A dedicated webspace for files

## Installation ##
First start off by downloading the [latest featured build](http://code.google.com/p/null-essentials/downloads/list?can=3&q=NullEssentials&colspec=Filename+Summary+Uploaded+ReleaseDate+Size+DownloadCount) and move it into /plugins/ of your CraftBukkit Server.
Then run(or reload) your CraftBukkit Server to load Null Essentials and let it create it's _config.yml_.
Edit it to your needs and save the file, then run _ne parts reload_ from console or any player with the ne.parts and ne.parts.reload permission.
This should make Null Essentials close all of it's subparts and reload the configuration, then open the enabled parts(A full plugin restart/reload is required when changing what parts are enabled).

## Configuration ##
Since we cannot just grant everyone the right to do everything, here are the Permissions you should grant:
| Node | For | Parent(s) |
|:-----|:----|:----------|
| ne.parts | /ne parts | ne.`*` |
| ne.parts.state | /ne parts <enable|disable> | ne.parts.`*`, ne.`*` |
| ne.parts.reload | /ne parts reload | ne.parts.`*`, ne.`*` |
| ne.slotmanager | [Slot Manager](http://code.google.com/p/null-essentials/wiki/Configuration_SlotManager) | ne.`*` |