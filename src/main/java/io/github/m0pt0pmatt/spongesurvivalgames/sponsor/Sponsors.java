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

import java.util.*;

import org.bukkit.potion.PotionEffectType;

public final class Sponsors {

    private static final Map<String, Sponsor> sponsors = new TreeMap<>();

    static {
        sponsors.put("restore-health", new RestoreHealthSponsor());
        sponsors.put("restore-hunger", new RestoreHungerSponsor());
        sponsors.put("give-light-armor", new OpenInventorySponsor(Collections.singletonList(SponsorItems.LIGHTARMOR)));
        sponsors.put("give-medium-armor", new OpenInventorySponsor(Collections.singletonList(SponsorItems.MEDIUMARMOR)));
        sponsors.put("give-light-sword", new OpenInventorySponsor(Collections.singletonList(SponsorItems.LIGHTSWORD)));
        sponsors.put("give-medium-sword", new OpenInventorySponsor(Collections.singletonList(SponsorItems.MEDIUMSWORD)));
        sponsors.put("give-bow", new OpenInventorySponsor(Collections.singletonList(SponsorItems.BOW)));
        sponsors.put("give-health-potion", new OpenInventorySponsor(Collections.singletonList(SponsorItems.HEALTHPOTION)));
        sponsors.put("give-poison-potion", new OpenInventorySponsor(Collections.singletonList(SponsorItems.POISONPOTION)));
        sponsors.put("give-food", new OpenInventorySponsor(Collections.singletonList(SponsorItems.FOOD1)));
        sponsors.put("give-food2", new OpenInventorySponsor(Collections.singletonList(SponsorItems.FOOD2)));
        sponsors.put("give-food3", new OpenInventorySponsor(Collections.singletonList(SponsorItems.FOOD3)));
        sponsors.put("give-wither", new StatusEffectSponsor(PotionEffectType.WITHER, 5, 1));
        sponsors.put("give-blindness",new StatusEffectSponsor(PotionEffectType.BLINDNESS, 10, 1));
        sponsors.put("give-poison1", new StatusEffectSponsor(PotionEffectType.POISON,5,1));
        sponsors.put("give-poison2", new StatusEffectSponsor(PotionEffectType.POISON,5,2));
        sponsors.put("give-nausea", new StatusEffectSponsor(PotionEffectType.CONFUSION,20,1));
    }

    public static Optional<Sponsor> get(String name) {
        Sponsor sponsor = sponsors.get(name);
        return sponsor == null ? Optional.empty() : Optional.of(sponsor);
    }

    public static Set<String> listAll() {
        return sponsors.keySet();
    }
}
