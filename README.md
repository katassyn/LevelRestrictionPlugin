# LevelRestrictionPlugin

A Minecraft plugin that restricts players from equipping armor or using specific items (e.g., weapons) if they do not meet the required level. The plugin is fully configurable, allowing you to define restricted items and custom messages in the `config.yml` file.

## Description

LevelRestrictionPlugin adds RPG-like level requirements to items in your Minecraft server. It prevents players from using items they don't have the appropriate level for, encouraging progression and providing a more structured gameplay experience.

---

## Features

### **Level-Based Restrictions**
- Prevents players from:
  - **Equipping armor** they don't have the level for
  - **Attacking with weapons** they don't have the level for
  - **Using restricted items** if they do not meet the required level

### **Customizable Messages**
- Send players a message when they attempt to use an item they are not eligible for
- Use the placeholder `%level%` to dynamically display the required level in the message

### **Item Identification**
- Items are identified by both their material type and display name
- Color codes in display names are supported
- Comparison ignores color codes to prevent issues with different formatting

---

## Installation

1. Download the latest version of the plugin from the releases page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server or use a plugin manager to load the plugin
4. The plugin will generate a default `config.yml` file
5. Configure the plugin to suit your server's needs (see Configuration section)

---

## Configuration

### **config.yml**
The configuration file allows you to define:
- The message shown to players when they can't use an item
- A list of restricted items with their required levels

#### Basic Structure:
```yaml
message: "You must be at least level %level% to use this item!"
items:
  [item_identifier]:
    id: [MATERIAL_ID]
    display_name: "[Item Display Name with Color Codes]"
    lv: [required_level]
```

#### Example Configuration:
```yaml
message: "You must be at least level %level% to use this item!"
items:
  worn_chainmail:
    id: CHAINMAIL_CHESTPLATE
    display_name: "&l&3○&r &3Worn Chainmail"
    lv: 100
  rusty_greatsword:
    id: IRON_SWORD
    display_name: '&l&3○&r &3Rusty Greatsword'
    lv: 90
  fyrgons_ring_of_fire:
    id: TNT_MINECART
    display_name: '&6[ III ] &l&cFYRGON`S RING OF FIRE'
    lv: 100
```

### Configuration Elements:

- **message**: The message displayed to players when they try to use an item they don't have the level for
  - Use `%level%` as a placeholder for the required level

- **items**: A section containing all restricted items
  - Each item has a unique identifier (e.g., `worn_chainmail`)
  - **id**: The Minecraft material ID of the item
  - **display_name**: The display name of the item with color codes (using & for formatting)
  - **lv**: The required level to use this item

---

## Commands and Permissions

The plugin defines the following command in plugin.yml, but it appears to be unimplemented in the current version:

- **/items_lvl reload**: Intended to reload the configuration
  - Permission: `items_lvl.reload`

---

## Usage Examples

### Creating Level-Restricted Items

1. **Create custom items** using a plugin like ItemsAdder, EcoItems, or custom NBT data
2. **Add them to the config.yml** with appropriate level requirements
3. **Distribute the items** to players through shops, quests, or other means

### Integration with Level Systems

This plugin does not provide a leveling system itself. It's designed to work with any plugin that stores player levels in the standard Minecraft experience system. Compatible level plugins include:

- McMMO
- SkillAPI
- LevelledMobs
- Custom level plugins that use the standard Minecraft level system

### Example Progression System

You can create a tiered progression system with items like:

```
Tier 1 (Levels 1-25):
- Leather Armor
- Wooden/Stone Weapons

Tier 2 (Levels 26-50):
- Chainmail Armor
- Iron Weapons

Tier 3 (Levels 51-75):
- Iron Armor
- Diamond Weapons

Tier 4 (Levels 76-100):
- Diamond Armor
- Netherite Weapons
```

---

## Compatibility

- **Minecraft Versions**: 1.20.x (as specified in plugin.yml)
- **Server Software**: Works with Spigot, Paper, and derivatives
- **Item Plugins**: Compatible with most item customization plugins

---

## Troubleshooting

### Items Not Being Restricted

If items are not being restricted properly:

1. **Check the material ID**: Ensure you're using the correct Minecraft material ID
2. **Verify the display name**: Make sure the display name in the config matches exactly (excluding color codes)
3. **Restart the server**: Some changes may require a server restart

### Permission Issues

If you're having trouble with permissions:

1. **Check permission nodes**: Ensure players have the correct permissions
2. **Verify permission plugin**: Make sure your permission plugin is working correctly

---

## Contributing

Contributions are welcome! Feel free to submit pull requests or open issues if you find bugs or have feature requests.

---

## License

This plugin is released under the [MIT License](LICENSE).
