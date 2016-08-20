/*
 * This file is part of SpongeSurvivalGamesPlugin, licensed under the MIT License (MIT).
 *
 * Copyright (c) Matthew Broomfield <m0pt0pmatt17@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Collections;

final class SponsorItems {

    public static final ItemStack LIGHTARMOR = ItemStack.of(ItemTypes.LEATHER_CHESTPLATE, 0);
    public static final ItemStack MEDIUMARMOR = ItemStack.of(ItemTypes.CHAINMAIL_CHESTPLATE, 0);
    public static final ItemStack LIGHTSWORD = ItemStack.of(ItemTypes.WOODEN_SWORD, 0);
    public static final ItemStack MEDIUMSWORD = ItemStack.of(ItemTypes.IRON_SWORD, 0);
    public static final ItemStack BOW = ItemStack.of(ItemTypes.BOW, 0);
    public static final ItemStack HEALTHPOTION = ItemStack.builder()
        .itemType(ItemTypes.SPLASH_POTION)
        .add(Keys.POTION_EFFECTS, Collections.singletonList(PotionEffect.of(PotionEffectTypes.HEALTH_BOOST, 1, 10)))
        .quantity(1)
        .build();

    public static final ItemStack POISONPOTION = ItemStack.builder()
        .itemType(ItemTypes.SPLASH_POTION)
        .add(Keys.POTION_EFFECTS, Collections.singletonList(PotionEffect.of(PotionEffectTypes.POISON, 1, 10)))
        .quantity(1)
        .build();

    public static final ItemStack FOOD1 = ItemStack.of(ItemTypes.CARROT, 5);
    public static final ItemStack FOOD2 = ItemStack.of(ItemTypes.BREAD, 5);
    public static final ItemStack FOOD3 = ItemStack.of(ItemTypes.COOKED_CHICKEN, 5);
}
