package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<Node> heap;
    private HashMap<T, Double> items;

    private class Node {
        T item;
        double priority;

        Node(T i, double p) {
            this.item = i;
            this.priority = p;
        }
    }

    public ArrayHeapMinPQ() {
        heap = new ArrayList<>();
    }

    private static int parent(int i) {
        return (i - 1) / 2;
    }

    private static int leftChild(int i) {
        return 2 * i + 1;
    }

    private static int rightChild(int i) {
        return 2 * i + 2;
    }

    private void swap(int i, int j) {
        Node tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
    }

    private boolean less(int i, int j) {
        return heap.get(i).priority < heap.get(j).priority;
    }

    private void swim(int i) {
        while (i > 0) {
            int p = parent(i);
            if (!less(i, p)) {
                break;
            }
            swap(i, p);
            i = p;
        }
    }

    private void sink(int i) {
        while (leftChild(i) < size()) {
            int s = leftChild(i);
            if (rightChild(i) < size() && less(rightChild(i), s)) {
                s = rightChild(i);
            }
            if (less(i, s)) {
                break;
            }
            swap(i, s);
            i = s;
        }
    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present. */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        heap.add(new Node(item, priority));
        swim(heap.size() - 1);
        items.put(item, priority);
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return items.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        return heap.get(0).item;
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        T min = heap.get(0).item;
        swap(0, size() - 1);
        heap.remove(size() - 1);
        sink(0);
        items.remove(min);
        return min;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        for (int i = 0; i < size(); i++) {
            if (heap.get(i).item.equals(item)) {
                double oldPriority = heap.get(i).priority;
                heap.get(i).priority = priority;
                if (oldPriority < priority) {
                    sink(i);
                } else {
                    swim(i);
                }
                return;
            }
        }
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return heap.size();
    }
}
