/*
 * This file is part of SurvivalGames, licensed under the MIT License (MIT).
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
package io.github.m0pt0pmatt.survivalgames.data;

import static java.lang.Double.max;
import static java.lang.Double.min;

import com.flowpowered.math.vector.Vector3d;
import java.util.Optional;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Area {

    @Setting(value = "lesser-boundary", comment = "Lesser Map boundary")
    private Vector3d lesserBoundary;

    @Setting(value = "greater-boundary", comment = "Greater Map boundary")
    private Vector3d greaterBoundary;

    public Optional<Vector3d> getLesserBoundary() {
        return Optional.ofNullable(lesserBoundary);
    }

    public Optional<Vector3d> getGreaterBoundary() {
        if (greaterBoundary != null) {
            return Optional.of(greaterBoundary);
        }

        return getLesserBoundary();
    }

    public void addBoundaryVector(Vector3d vector3i) {

        if (vector3i == null) {
            return;
        }

        if (lesserBoundary == null) {
            lesserBoundary = vector3i;
        } else {
            if (greaterBoundary != null) {

                double x1 = lesserBoundary.getX();
                double x2 = greaterBoundary.getX();
                double x3 = vector3i.getX();

                double y1 = lesserBoundary.getY();
                double y2 = greaterBoundary.getY();
                double y3 = vector3i.getY();

                double z1 = lesserBoundary.getZ();
                double z2 = greaterBoundary.getZ();
                double z3 = vector3i.getZ();

                lesserBoundary =
                        new Vector3d(
                                min(min(x1, x2), x3), min(min(y1, y2), y3), min(min(z1, z2), z3));

                greaterBoundary =
                        new Vector3d(
                                max(max(x1, x2), x3), max(max(y1, y2), y3), max(max(z1, z2), z3));
            } else {
                double x1 = lesserBoundary.getX();
                double x2 = vector3i.getX();

                double y1 = lesserBoundary.getY();
                double y2 = vector3i.getY();

                double z1 = lesserBoundary.getZ();
                double z2 = vector3i.getZ();

                lesserBoundary = new Vector3d(min(x1, x2), min(y1, y2), min(z1, z2));

                greaterBoundary = new Vector3d(max(x1, x2), max(y1, y2), max(z1, z2));
            }
        }
    }

    public void clearBoundaryVectors() {
        lesserBoundary = null;
        greaterBoundary = null;
    }
}
