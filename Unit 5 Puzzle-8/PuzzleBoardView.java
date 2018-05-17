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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            for (int j = 0; j < NUM_SHUFFLE_STEPS; j++) {
                ArrayList<PuzzleBoard> neighboursList = puzzleBoard.neighbours();
                int i = random.nextInt(neighboursList.size());
                puzzleBoard = new PuzzleBoard(neighboursList.get(i));
                // Do something. Then:
                puzzleBoard.reset();
                invalidate();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {
        Queue<PuzzleBoard> queue = new PriorityQueue<>();
        puzzleBoard.setSteps(0);
        puzzleBoard.setPreviousBoard(null);
        queue.add(puzzleBoard);

        ArrayList<PuzzleBoard> neighboursList, previousBoardsList;
        while (!queue.isEmpty()) {
            PuzzleBoard removedBoard = queue.poll();
            if (removedBoard.resolved() == false) {
                neighboursList = removedBoard.neighbours();
                for (int i=0; i<neighboursList.size(); i++) {
                    PuzzleBoard tempBoard = neighboursList.get(i);
                    if (tempBoard.equals(tempBoard.getPreviousBoard()) == false) {
                        queue.add(tempBoard);
                    }
                }
            }
            else {
                previousBoardsList = new ArrayList<>();
                PuzzleBoard prevBoard = removedBoard.getPreviousBoard();
                while (prevBoard.resolved() == false) {
                    previousBoardsList.add(prevBoard);
                    prevBoard = prevBoard.getPreviousBoard();
                }
                Collections.reverse(previousBoardsList);
                animation = previousBoardsList;
            }
        }
    }
}