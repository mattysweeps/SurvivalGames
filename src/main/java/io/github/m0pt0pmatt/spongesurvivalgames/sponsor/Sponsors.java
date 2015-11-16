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

import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public final class Sponsors {

    private static final Map<String, Sponsor> sponsors = new TreeMap<>();

    static {
        sponsors.put("restore-health", new RestoreHealthSponsor());
        sponsors.put("restore-hunger", new RestoreHungerSponsor());
        sponsors.put("light-armor", new OpenInventorySponsor(Collections.singletonList(SponsorItems.LIGHTARMOR)));
        sponsors.put("medium-armor", new OpenInventorySponsor(Collections.singletonList(SponsorItems.MEDIUMARMOR)));
        sponsors.put("light-sword", new OpenInventorySponsor(Collections.singletonList(SponsorItems.LIGHTSWORD)));
        sponsors.put("medium-sword", new OpenInventorySponsor(Collections.singletonList(SponsorItems.MEDIUMSWORD)));
        sponsors.put("bow", new OpenInventorySponsor(Collections.singletonList(SponsorItems.BOW)));
        sponsors.put("health-potion", new OpenInventorySponsor(Collections.singletonList(SponsorItems.HEALTHPOTION)));
        sponsors.put("poison-potion", new OpenInventorySponsor(Collections.singletonList(SponsorItems.POISONPOTION)));
        sponsors.put("food", new OpenInventorySponsor(Collections.singletonList(SponsorItems.FOOD1)));
        sponsors.put("food2", new OpenInventorySponsor(Collections.singletonList(SponsorItems.FOOD2)));
        sponsors.put("food3", new OpenInventorySponsor(Collections.singletonList(SponsorItems.FOOD3)));
        sponsors.put("clear-status-effects", new ClearStatusEffectSponsor());
        sponsors.put("wither", new StatusEffectSponsor(PotionEffectType.WITHER, 100, 1));
        sponsors.put("blindness", new StatusEffectSponsor(PotionEffectType.BLINDNESS, 200, 1));
        sponsors.put("poison1", new StatusEffectSponsor(PotionEffectType.POISON, 100, 1));
        sponsors.put("poison2", new StatusEffectSponsor(PotionEffectType.POISON, 100, 2));
        sponsors.put("nausea", new StatusEffectSponsor(PotionEffectType.CONFUSION, 400, 1));
        sponsors.put("spawn-zombies", new SpawnEntitySponsor(EntityType.ZOMBIE, 10));
        sponsors.put("spawn-skeletons", new SpawnEntitySponsor(EntityType.SKELETON, 10));
        sponsors.put("spawn-creepers", new SpawnEntitySponsor(EntityType.CREEPER, 3));
    }

    public static Optional<Sponsor> get(String name) {
        Sponsor sponsor = sponsors.get(name);
        return sponsor == null ? Optional.empty() : Optional.of(sponsor);
    }

    public static Set<String> listAll() {
        return sponsors.keySet();
    }
}
