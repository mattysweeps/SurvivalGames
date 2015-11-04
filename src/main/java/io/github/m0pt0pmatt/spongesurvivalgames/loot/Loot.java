package io.github.m0pt0pmatt.spongesurvivalgames.loot;

import org.bukkit.inventory.ItemStack;

/**
 * Wrapper class for Loot which houses information about it's weight and the item it's storing
 *
 * @author Skyler
 */
public class Loot {

    private ItemStack item;

    private double weight;

    public Loot(ItemStack item, double weight) {
        this.item = item;
        this.weight = weight;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getWeight() {
        return weight;
    }


}
