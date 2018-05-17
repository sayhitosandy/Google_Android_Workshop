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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random rand = new Random();
        if (prefix.length() == 0) {
            return words.get(rand.nextInt(words.size()));
        }
        else {
            int pos = binarySearch(0, words.size(), prefix);
            if (pos == -1) {
                return null;
            }
            List<String> choices = new ArrayList<>();
            for (int i=pos; i<words.size(); i++) {
                if (words.get(i).startsWith(prefix) == true) {
                    choices.add(words.get(i));
                }
                else {
                    break;
                }
            }
            return words.get(rand.nextInt(choices.size()));
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return getAnyWordStartingWith(prefix);
    }

    protected int binarySearch (int low, int high, String item) {
        int mid;
        while (low < high) {
            mid = low + (high - low)/2;
            String s = words.get(mid);
            if (s.startsWith(item) == true) {
                return mid;
            }
            else if (s.compareTo(item) > 0) {
                high = mid;
            }
            else {
                low = mid;
            }
        }
        return -1;
    }
}
