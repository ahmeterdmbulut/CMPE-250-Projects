/***
 * A custom priority queue implementation using a max-heap.
 * @param <T> The type of elements held in this priority queue. Must be comparable.
 */
public class MyPriorityQueue<T extends Comparable<T>> {
    private T[] heap;
    private int currentSize;

    /***
     * Constructor to initialize the priority queue with given capacity.
     * @param capacity the initial capacity of the priority queue.
     */
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int capacity){
        heap = (T[]) new Comparable[capacity + 1]; // root starts at index 1
        currentSize = 0;
    }

    /***
     * Resize the heap array when it becomes full.
     */
    @SuppressWarnings("unchecked")
    private void resize(){
        T[] newHeap = (T[]) new Comparable[heap.length * 2 + 1];
        System.arraycopy(heap, 1, newHeap, 1, currentSize);
        heap = newHeap;
    }

    /***
     * A method to maintain the max-heap property by percolating an element up.
     * It is called after adding a new element to restore the max-heap property.
     * @param index The index of the element to percolate up.
     */
    private void percolateUp(int index){
        while (index > 1){
            int parent = index / 2;
            if (heap[index].compareTo(heap[parent]) > 0){
                // Swap with parent if the current is greater
                T temp = heap[index];
                heap[index] = heap[parent];
                heap[parent] = temp;
                index = parent;
            } else {
                break;
            }
        }
    }

    /***
     * A method to maintain the max-heap property by percolating an element down.
     * It is called after removing the root to restore the max-heap property.
     * @param index The index of the element to percolate down.
     */
    private void percolateDown(int index){
        int leftChild = index * 2;
        int rightChild = index * 2 + 1;
        int largest = index;

        // Compare with left child
        if (leftChild <= currentSize && heap[leftChild].compareTo(heap[largest]) > 0){
            largest = leftChild;
        }
        // Compare with right child
        if (rightChild <= currentSize && heap[rightChild].compareTo(heap[largest]) > 0){
            largest = rightChild;
        }
        // Swap and continue percolating down if needed
        if (largest != index){
            T temp = heap[index];
            heap[index] = heap[largest];
            heap[largest] = temp;
            percolateDown(largest);
        }
    }

    /***
     * Adds an item to the priority queue.
     * @param item The item to be added.
     */
    public void add(T item){
        if (currentSize + 1 == heap.length){
            resize(); // if the heap is full, resize
        }
        heap[++currentSize] = item;
        percolateUp(currentSize); // to keep the max-heap property
    }

    /***
     * Removes and returns the maximum element at the root from the prioity queue.
     * @return The maximum element, or null if the priority queue is empty
     */
    public T poll(){
        if (currentSize == 0){
            return null;
        }
        T item = heap[1];
        heap[1] = heap[currentSize];
        currentSize--;
        percolateDown(1); // to keep the max-heap property
        return item;
    }


    /***
     * Checks if the priority queue is empty.
     * @return True if the priority queue is empty, false otherwise.
     */
    public boolean isEmpty(){
        return currentSize == 0;
    }
}
