/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode currentNode = this;
        TrieNode nextNode;
        for (int i=0; i<s.length(); i++) {
            nextNode = currentNode.children.get(s.charAt(i));
            if (nextNode == null) {
                TrieNode newNode = new TrieNode();
                currentNode.children.put(String.valueOf(s.charAt(i)), newNode);
                currentNode = newNode;
            }
            else {
                currentNode = nextNode;
            }
            if (i == s.length()-1) {
                currentNode.isWord = true;
            }
        }
    }

    public boolean isWord(String s) {
        TrieNode currentNode = this;
        for (int i=0; i<s.length(); i++) {
            currentNode = currentNode.children.get(s.charAt(i));
            if (currentNode == null) {
                return false;
            }
        }
        if (currentNode.isWord == true) {
            return true;
        }
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode currentNode = this;
        boolean flag = false;
        for (int i=0; i<s.length(); i++) {
            currentNode = currentNode.children.get(s.charAt(i));
            if (currentNode == null) {
                flag = true;
                break;
            }
        }
        if (flag == false) {
            while (currentNode.isWord == false) {
                Set set = currentNode.children.entrySet();
                Iterator i = set.iterator();
                String k = null;
                if (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    k = me.getKey().toString();
                }
                currentNode = currentNode.children.get(k);
                s = s + k;
            }
            return s;
        }
        return null;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
