# LevelRestrictionPlugin

A Minecraft plugin that restricts players from equipping armor or using specific items (e.g., weapons) if they do not meet the required level. The plugin is fully configurable, allowing you to define restricted items and custom messages in the `config.yml` file.

---

## Features

### **Level-Based Restrictions**
- Prevents players from:
  - **Equipping armor**.
  - **Attacking with items**.
  - **Using restricted items** if they do not meet the required level.

### **Customizable Messages**
- Send players a message when they attempt to use an item they are not eligible for.
- Use the placeholder `%level%` to dynamically display the required level in the message.

---

## Configuration

### **config.yml**
Define restricted items and their required levels:
```yaml
message: "You must be at least level %level% to use this item!"
items:
  worn_chainmail:
    id: CHAINMAIL_CHESTPLATE
    display_name: "&l&3○&r &3Worn Chainmail"
    lv: 100
  legendary_sword:
    id: DIAMOND_SWORD
    display_name: "&6Legendary Sword of Power"
    lv: 200
