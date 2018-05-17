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

package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 2;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, parentWidth, parentWidth, false);

        int rows = NUM_TILES;
        int cols = NUM_TILES;
        int chunkHeight = scaledBitmap.getHeight()/rows;
        int chunkWidth = scaledBitmap.getWidth()/cols;

        tiles = new ArrayList<>(NUM_TILES*NUM_TILES);

        int y = 0;
        for (int i=0; i<rows; i++) {
            int x = 0;
            for (int j=0; j<cols; j++) {
                tiles.add(new PuzzleTile(Bitmap.createBitmap(scaledBitmap, x, y, chunkWidth, chunkHeight), i*NUM_TILES + j));
                x += chunkWidth;
            }
            y += chunkHeight;
        }
        tiles.set(NUM_TILES*NUM_TILES - 1, null);
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
                PuzzleTile tile = tiles.get(i);
                if (tile != null) {
                    tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> neighboursList = new ArrayList<>();

        int locationX = NUM_TILES - 1, locationY = NUM_TILES - 1;
        for (int i=0; i<NUM_TILES*NUM_TILES; i++) {
            if (tiles.get(i) == null) {
                locationX = i % NUM_TILES;
                locationY = i / NUM_TILES;
                break;
            }
        }

        int tileX, tileY;
        for (int[] delta : NEIGHBOUR_COORDS) {
            tileX = locationX + delta[0];
            tileY = locationY + delta[1];
            if (tileX >= 0 && tileX < NUM_TILES && tileY >= 0 && tileY < NUM_TILES ) {
                PuzzleBoard newBoard = new PuzzleBoard(this);
                newBoard.swapTiles(XYtoIndex(locationX, locationY), XYtoIndex(tileX, tileY));
                neighboursList.add(newBoard);
            }
        }

        return neighboursList;
    }

    public int priority() {
        return 0;
    }

}
