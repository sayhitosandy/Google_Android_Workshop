package com.google.engedu.puzzle8;

import java.util.ArrayList;
import java.util.Comparator;

class MyPriorityQueue {
	
	private ArrayList<PuzzleBoard> myList;
    private int first, last;

	public MyPriorityQueue() {
		myList = new ArrayList<>();
        first = last = -1;
	}
	
	public boolean add(PuzzleBoard board) {
		myList.add(board);
        if (first == -1) {
            first = 0;
        }
        last = myList.size() - 1;

		int k = last;
		while (k > 0 && compare(myList.get(k>>1), board) > 0) {
			myList.set(k, myList.get(k>>1));
			k = k>>1;
		}
		myList.set (k, board);
		return true;
	}

	public PuzzleBoard remove() {
		if (isEmpty() == false) {
			PuzzleBoard tempBoard = myList.get(first);
			myList.set(first, myList.get(last));
			myList.remove(last);
			if (last > 0) {
				heapify(first);
			}
			return tempBoard;
		}
		return null;
	}
	
	public boolean isEmpty() {
		if (last == -1) {
			return true;
		}
		return false;
	}

	private void heapify(int root) {
		PuzzleBoard rootBoard =  myList.get(root);
		int child, k = root;
		while (2*k + 1 <= last) {
			child = 2*k + 1;
			if (child < last && compare(myList.get(child), myList.get(child + 1)) > 0) {
				child++;
			}
			if (compare(rootBoard, myList.get(child)) <= 0) {
                break;
            }
            else {
                myList.set(k, myList.get(child));
                k = child;
            }
        }
        myList.set(k, rootBoard);
	}

    private int compare(PuzzleBoard b1, PuzzleBoard b2) {
        return b1.priority() - b2.priority();
    }
}