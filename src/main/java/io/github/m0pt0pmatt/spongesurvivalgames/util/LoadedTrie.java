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
 * @param <D> The type data to store at the end of branches (leaf nodes).
 */
public class LoadedTrie<T, D> {

    private Node first = new Node();

    /**
     * Adds a new branch to the trie
     *
     * @param list The new branch to be added
     * @param data The data to store at the end of the branch
     */
    public void add(T[] list, T[] leftovers, D data) {
        if (list == null) return;

        Node current = first;
        for (T t : list) {
            if (!current.children.containsKey(t)) {
                current.children.put(t, new Node());
            }

            current = current.children.get(t);
        }

        current.data = data;
        current.leftovers = leftovers;
    }

    public void add(T[] list, D data) {
        add(list, null, data);
    }

    public LoadedTrieReturn<T, D> match(T[] input) {

        LoadedTrieReturn<T, D> trieReturn = new LoadedTrieReturn<>();

        if (input == null) return trieReturn;

        Node current = first;
        int i;

        //Traverse the trie while we get true matches
        for (i = 0; i < input.length; i++) {
            if (current.children.containsKey(input[i])) {
                current = current.children.get(input[i]);
                trieReturn.matched.add(input[i]);
            } else {
                break;
            }
        }

        if (current == null || current.data == null) return trieReturn;

        //Collect leftovers
        for (int j = 0; i < input.length && j < current.leftovers.length; i++, j++) {
            trieReturn.leftoverMap.put(current.leftovers[j], input[i]);
        }

        trieReturn.data = current.data;
        trieReturn.leftovers = current.leftovers;
        return trieReturn;
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
        D data = null;
        T[] leftovers = null;
    }

}
