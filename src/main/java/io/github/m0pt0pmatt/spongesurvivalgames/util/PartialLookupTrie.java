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

public class PartialLookupTrie<T, C> {

    private Node<T, C> first = new Node<>();

    public void add(T[] list, C data) {
        if (list == null || list.length < 1) return;

        Node<T, C> current = first;
        for (T t : list) {
            if (!current.children.containsKey(t)) {
                current.children.put(t, new Node<>());
            }

            current = current.children.get(t);
        }

        current.data = data;
    }

    public C match(T[] input, Map<T, T> arguments) {
        if (input == null || input.length < 1) return null;
        if (first == null) return null;

        Node<T, C> current = first;

        int i;
        for (i = 0; i < input.length; i++) {

            //True match
            if (current.children.containsKey(input[i])) {
                current = current.children.get(input[i]);
            } else {
                break;
            }
        }

        for (; current != null && current.children.size() > 0 && i < input.length; i++) {

            //There should only be one valid path
            if (current.children.entrySet().size() > 1) {
                return null;
            }

            T key = current.children.entrySet().iterator().next().getKey();
            arguments.put(key, input[i]);
            current = current.children.get(key);
        }

        if (current == null) return null;

        return current.data;
    }

    public List<T> partialMatch(T[] list) {
        List<T> output = new LinkedList<>();

        if (list == null || list.length < 1) {
            output.addAll(first.children.keySet());
            return output;
        }

        Node<T, C> current = first;

        for (T t : list) {

            //True match
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

    private class Node<E, D> {
        Map<E, Node<E, D>> children = new HashMap<>();
        D data;

        public Node() {
            this.data = null;
        }

    }
}
