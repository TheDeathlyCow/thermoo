---
title: ⛏️ Item Attribute API
status: new
---

Thermoo provides an event, `ModifyItemAttributeModifiersCallback.EVENT`, that can modify the attribute modifiers on an `ItemStack` when they are applied to an entity. This works very similarly to the event of the same name that was previously in Fabric API, but was removed in 1.20.5 due to difficulty with porting.

## Usage Example

Increase max health of all Helmets when worn in the Head Slot.

=== "Java"
    ```java
    static void initialize() {
        ModifyItemAttributeModifiersCallback.EVENT.register((stack, builder) -> {
            if (stack.isIn(ItemTags.HEAD_ARMOR)) {
                builder.add(
                        EntityAttributes.MAX_HEALTH,
                        new EntityAttributeModifier(
                                Identifier.of("example", "helmet_max_hp"),
                                4.0,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.HEAD
                );
            }
        });
    }
    ```
=== "Kotlin"
    ```kotlin
    fun initialize() {
        ModifyItemAttributeModifiersCallback.EVENT.register { stack, builder ->
            if (stack.isIn(ItemTags.HEAD_ARMOR)) {
                builder.add(
                        EntityAttributes.MAX_HEALTH,
                        EntityAttributeModifier(
                                Identifier.of("example", "helmet_max_hp"),
                                4.0,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.HEAD
                )
            }
        }
    }
    ```
