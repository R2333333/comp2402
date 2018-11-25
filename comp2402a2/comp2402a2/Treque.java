package comp2402a2;

import java.util.AbstractList;

/**
 * Treque : an implementation of the List interface 
 * that allows for fast modifications at the head, tail
 * and middle of the list.
 *
 * Modify the methods so that 
 *  -set/get have constant runtime
 *  -add/remove have O(1+min{i, size()-i, |size()/2-i|})
 *              amortized runtime.
 * 
 * @author morin/ArrayDeque
 *
 * @param <T> the type of objects stored in this list
 */
public class Treque<T> extends AbstractList<T> {
	/**
	 * The class of elements stored in this queue
	 */
	protected Factory<T> f;
	
	/**
	 * Array used to store elements
	 */
	protected T[] a;
	
	/**
	 * Index of next element to first de-queue
	 */
	protected int j;

	/**
	 * Index of next element to second de-queue
	 */
	protected int m = 0;
	
	/**
	 * Number of elements in the queue
	 */
	protected int n;
	protected int l;
	/**
	 * Grow/shrink the internal array
	 */
	protected ArrayDeque<T> front;

	// Back half - x[n/2], ..., x[n]
	protected ArrayDeque<T> back;
	public Treque(Class<T> t) {
		front = new ArrayDeque<>(t);
		back = new ArrayDeque<>(t);
	}

	// Returns element at global index i
	public T get(int i) {
		if (i < 0 || i >= size()) {
			throw new IndexOutOfBoundsException();
		}
		if (i < front.size()) {
			return front.get(i);
		} else {
			return back.get(i - front.size());
		}
	}
	// Replaces elements at global index i with x and
	// returns elements that was replaced
	public T set(int i, T x) {
		if (i < 0 || i >= size()) {
			throw new IndexOutOfBoundsException();
		}
		if (i < front.size()) {
			return front.set(i, x);
		} else {
			return back.set(i - front.size(), x);
		}
	}

	// Adds a new element at global index i
	// and balance both halves of the Treque
	public void add(int i, T x) {
		if (i < 0 || i > size()) {
			throw new IndexOutOfBoundsException();
		}
		if (i < front.size()) {
			front.add(i, x);
		} else {
			back.add(i - front.size(), x);
		}
		balanceDeques();
	}

	// Removes and returns element at global index i
	// and balance both halves of the Treque
	public T remove(int i) {
		if (i < 0 || i >= size()) {
			throw new IndexOutOfBoundsException();
		}
		T x = null;
		if (i < front.size()) {
			x = front.remove(i);
		} else {
			x = back.remove(i - front.size());
		}
		balanceDeques();

		return x;
	}

	public void clear() {
		front.clear();
		back.clear();
	}

	public int size() {
		return front.size() + back.size();
	}


	protected void balanceDeques() {
		final double scaleFactor = 1.00001;
		if (scaleFactor * front.size() < back.size()) {
			// Front deque is smaller than back
			// Need to move elements from front of back deque
			// to back of front deque
			while(front.size() < back.size()) {
				T removed = back.remove(0);
				front.add(front.size(), removed);
			}
		} else if (scaleFactor * back.size() < front.size()) {
			// Back deque is smaller than front
			// Need to move elements from back of the front deque
			// to front of back deque
			while(back.size() < front.size()) {
				T removed = front.remove(front.size() - 1);
				back.add(0, removed);
			}
		}
	}

}
