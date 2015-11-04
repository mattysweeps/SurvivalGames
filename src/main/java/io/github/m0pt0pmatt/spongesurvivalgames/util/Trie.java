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
import java.util.List;
import java.util.Map;

public class Trie<T, C> {

    private Node<T, C> first = null;

    private class Node<T, C>{
        Map<T, Node<T, C>> children = new HashMap<>();
        C data;

        public Node(){
            this.data = null;
        }

        public Node(C data){
            this.data = data;
        }
    }

    public void add(T[] list, C data){
        if (list.length < 1) return;
        int i = 0;


        if (first == null){
            first = new Node<>();
            i++;
        }

        Node<T, C> current = first;
        for (; i < list.length; i++){
            if (!current.children.containsKey(list[i])){
                current.children.put(list[i], new Node<>());
            }

            current = current.children.get(list[i]);
        }

        current.data = data;
    }

    public C match(T[] input, Map<T, T> arguments){

        return null;
    }

    public List<T> partialMatch(T[] list) {
        return null;
    }
}
