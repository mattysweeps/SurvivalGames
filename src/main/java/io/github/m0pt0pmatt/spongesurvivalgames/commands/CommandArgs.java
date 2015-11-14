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

package io.github.m0pt0pmatt.spongesurvivalgames.commands;

public final class CommandArgs {
    public static final CommandArgs ID = new CommandArgs("[id]", ArgTypes.ID);
    public static final CommandArgs X = new CommandArgs("[x]", ArgTypes.NONE);
    public static final CommandArgs Y = new CommandArgs("[y]", ArgTypes.NONE);
    public static final CommandArgs Z = new CommandArgs("[z]", ArgTypes.NONE);
    public static final CommandArgs WORLDNAME = new CommandArgs("[worldname]", ArgTypes.WORLD);
    public static final CommandArgs FILENAME = new CommandArgs("[filename]", ArgTypes.FILE);
    public static final CommandArgs XMIN = new CommandArgs("[xmin]", ArgTypes.NONE);
    public static final CommandArgs XMAX = new CommandArgs("[xmax]", ArgTypes.NONE);
    public static final CommandArgs ZMIN = new CommandArgs("[zmin]", ArgTypes.NONE);
    public static final CommandArgs ZMAX = new CommandArgs("[zmax]", ArgTypes.NONE);
    public static final CommandArgs WEIGHT = new CommandArgs("[weight]", ArgTypes.NONE);
    public static final CommandArgs MIDPOINT = new CommandArgs("[midpoint]", ArgTypes.NONE);
    public static final CommandArgs PLAYERNAME = new CommandArgs("[playername]", ArgTypes.PLAYER);
    public static final CommandArgs RANGE = new CommandArgs("[range]", ArgTypes.NONE);
    public static final CommandArgs COUNTDOWN = new CommandArgs("[countdown]", ArgTypes.NONE);
    public static final CommandArgs PLAYER_LIMIT = new CommandArgs("[player-limit]", ArgTypes.NONE);
    public static final CommandArgs DEATHMATCHRADIUS = new CommandArgs("[deathmatch-radius]", ArgTypes.NONE);
    public static final CommandArgs DEATHMATCHTIME = new CommandArgs("[deathmatch-time]", ArgTypes.NONE);
    public static final CommandArgs SPONSOR = new CommandArgs("[sponsor]", ArgTypes.SPONSOR);
    public static final CommandArgs OVERWRITE = new CommandArgs("[overwrite]", ArgTypes.NONE);
    public static final CommandArgs BACKUPNAME = new CommandArgs("[backupname]", ArgTypes.BACKUP);

    private final String name;
    private final ArgTypes type;

    private CommandArgs(String name, ArgTypes type) {
        this.name = name;
        this.type = type;
    }

    public ArgTypes getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof String || o instanceof CommandArgs) && name.equals(o);
    }
}
