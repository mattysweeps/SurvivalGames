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

package io.github.m0pt0pmatt.spongesurvivalgames.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Specialized Trie data structure
 * <p>A LoadedTrie is a Trie where a payload is stored at the end of each branch (each leaf node).</p>
 *
 * @param <T> The type which makes up the entire branch of the trie. For standard tries, this type is Character
 * @param <D> The type data to store at the end of branches (leaf nodes).
 */
public class LoadedTrie<T, D> {

    private final Node first = new Node();

    /**
     * Adds a new branch to the trie
     *
     * @param list The new branch to be added
     * @param data The data to store at the end of the branch
     */
    public void add(T[] list, D data) {
        if (list == null) return;

        Node current = first;
        for (T t : list) {
            if (!current.children.containsKey(t)) {
                current.children.put(t, new Node());
            }

            current = current.children.get(t);
        }

        current.data = data;
    }

    public D match(List<T> input) {

        if (input == null) return null;

        Node current = first;

        //Traverse the trie while we get true matches
        for (Iterator<T> i = input.iterator(); i.hasNext(); i.remove()) {
            T data = i.next();
            if (current.children.containsKey(data)) {
                current = current.children.get(data);
            } else {
                break;
            }
        }

        if (current == null || current.data == null) return null;

        return current.data;
    }

    /**
     * Returns all the possible immediate suffixes available after following a branch
     */
    public D partialMatch(List<T> input, List<T> output) {

        if (output == null) {
            return null;
        }

        if (input == null || input.size() < 1) {
            output.addAll(first.children.keySet());
            return null;
        }

        Node current = first;

        //Traverse the trie while we get true matches
        for (Iterator<T> i = input.iterator(); i.hasNext(); i.remove()) {
            T data = i.next();
            if (current.children.containsKey(data)) {
                current = current.children.get(data);
            } else {
                break;
            }
        }

        if (current == null) {
            return null;
        }

        output.addAll(current.children.keySet());
        return current.data;

    }

    private class Node {
        final Map<T, Node> children = new HashMap<>();
        D data = null;
    }

}
