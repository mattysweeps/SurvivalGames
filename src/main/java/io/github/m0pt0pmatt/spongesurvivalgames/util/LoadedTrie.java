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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Specialized Trie data structure
 * <p>A LoadedTrie is a Trie where a payload is stored at the end of each branch (each leaf node).</p>
 *
 * @param <T> The type which makes up the entire branch of the trie. For standard tries, this type is Character
 * @param <D> The type data to stor at the end of branches (leaf nodes).
 */
public class LoadedTrie<T, D> {

    private Node first = new Node();

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

    /**
     * Attempts to traverse the trie
     *
     * @param input           The input sequence for traversing the trie.
     * @param missingSuffixes Any mismatched Ts are stored in this map. the key is the expected value, and the value was the actual input. Note that, since this is a Map, duplicates are erased
     * @return The data if a leaf node was reached, null otherwise
     */
    public D match(T[] input, Map<T, T> missingSuffixes) {
        if (input == null) return null;

        Node current = first;
        int i;

        //Traverse the trie while we get true matches
        for (i = 0; i < input.length; i++) {
            if (current.children.containsKey(input[i])) {
                current = current.children.get(input[i]);
            } else {
                break;
            }
        }

        //If there is only one possible path, we can continue, storing the actual prefix
        for (; current != null && current.children.size() > 0 && i < input.length; i++) {

            //There should only be one valid path
            if (current.children.entrySet().size() > 1) {
                return null;
            }

            T key = current.children.entrySet().iterator().next().getKey();
            missingSuffixes.put(key, input[i]);
            current = current.children.get(key);
        }

        if (current == null) return null;
        return current.data;
    }

    /**
     * Returns all the possible immediate suffixes available after following a branch
     *
     * @param list the input sequence for traversing the trie
     * @return List of the next Ts for the given input
     */
    public List<T> partialMatch(T[] list) {
        List<T> output = new LinkedList<>();

        if (list == null || list.length < 1) {
            output.addAll(first.children.keySet());
            return output;
        }

        Node current = first;

        //Traverse the trie while we get true matches
        for (T t : list) {
            if (current.children.containsKey(t)) {
                current = current.children.get(t);
            } else {
                break;
            }
        }

        if (current != null) {
            output.addAll(current.children.keySet());
        }

        return output;
    }

    private class Node {
        Map<T, Node> children = new HashMap<>();
        D data;

        public Node() {
            this.data = null;
        }

    }
}
