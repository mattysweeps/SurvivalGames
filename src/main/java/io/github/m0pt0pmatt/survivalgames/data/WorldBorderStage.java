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

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.google.common.base.Preconditions.checkNotNull;

@ConfigSerializable
public class WorldBorderStage {

    @Setting(value = "waitTime")
    private Long waitTime;

    @Setting(value = "movementTime")
    private Long movementTime;

    @Setting(value = "diameterDelta")
    private Integer diameterDelta;

    public Duration getWaitTime() {
        return Duration.of(waitTime, ChronoUnit.SECONDS);
    }

    public void setWaitTime(Duration waitTime) {
        this.waitTime = checkNotNull(waitTime, "waitTime").getSeconds();
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = checkNotNull(waitTime, "waitTime");
    }

    public Duration getMovementTime() {
        return Duration.of(movementTime, ChronoUnit.SECONDS);
    }

    public void setMovementTime(Duration movementTime) {
        this.movementTime = checkNotNull(movementTime, "movementTime").getSeconds();
    }

    public void setMovementTime(Long movementTime) {
        this.movementTime = checkNotNull(movementTime, "movementTime");
    }

    public int getDiameterDelta() {
        return diameterDelta;
    }

    public void setDiameterDelta(Integer diameterDelta) {
        this.diameterDelta = checkNotNull(diameterDelta, "diameterDelta");
    }

    public static class Builder {

        private Duration waitTime;
        private Duration movementTime;
        private Integer diameterDelta;

        private Builder() {

        }

        public Builder waitTime(Duration waitTime) {
            this.waitTime = waitTime;
            return this;
        }

        public Builder movementTime(Duration movementTime) {
            this.movementTime = movementTime;
            return this;
        }

        public Builder diameterDelta(Integer diameterDelta) {
            this.diameterDelta = diameterDelta;
            return this;
        }

        public WorldBorderStage build() {
            WorldBorderStage stage = new WorldBorderStage();
            stage.setWaitTime(waitTime);
            stage.setMovementTime(movementTime);
            stage.setDiameterDelta(diameterDelta);
            return stage;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
