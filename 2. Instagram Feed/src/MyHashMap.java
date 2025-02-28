import java.util.ArrayList;

/***
 * A custom HashMap implementation
 * @param <K> The type of keys maintained by this map
 * @param <V> The type of mapped values
 */
public class MyHashMap<K, V> {
    /***
     * Represents an entry in the hashmap with a key-value pair
     * @param <K> The type of keys
     * @param <V> The type of values
     */
    private class MyEntry<K, V>{
        K key;
        V value;

        MyEntry<K, V> next;

        /***
         * Constructor to initialize a key-value pair.
         * @param key The key of the entry.
         * @param value The value of the entry.
         */
        MyEntry(K key, V value){
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private MyEntry<K, V>[] buckets;
    private int capacity;
    private int currentSize;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75; // threshold for triggering resizing

    /***
     * Constructor to initalize the hashmap with a given capacity.
     * @param capacity The initial capacity of the hashmap
     */
    @SuppressWarnings("unchecked")
    public MyHashMap(int capacity){
        this.capacity = capacity;
        this.currentSize = 0;
        this.buckets = (MyEntry<K, V>[]) new MyEntry[capacity];
    }

    /***
     * Calculates the index for a given key using its hash code.
     * @param key The key to calculate index for
     * @return The index within the bucket array.
     */
    private int getIndex(K key){
        return Math.abs(key.hashCode()) % capacity;
    }

    /***
     * Adds a key-value pair to the hashmap. If the key already exists, updates its value.
     * Handles collisions using separate chaining, linked list.
     * @param key The key to be added.
     * @param value The value to be associated with the key.
     */
    public void put(K key, V value){
        // check if the current load factor exceeds the threshold and resizing is needed.
        if ((double) currentSize / capacity >= LOAD_FACTOR_THRESHOLD){
            resize();
        }
        int index = getIndex(key);
        MyEntry<K, V> head = buckets[index];

        // iterate through the linked list to find if the key already exists
        while (head != null){
            if (head.key.equals(key)){
                head.value = value;
                return;
            }
            head = head.next;
        }
        // add new entry at the beginning of the linked list
        currentSize++;
        head = buckets[index];
        MyEntry<K, V> newNode = new MyEntry<>(key, value);
        newNode.next = head;
        buckets[index] = newNode;
    }

    /***
     * Retrieves the value associated with the given key.
     * @param key The key whose value is to be returned.
     * @return The value associated with the key, or null if the key is not found.
     */
    public V get(K key){
        int index = getIndex(key);
        MyEntry<K, V> head = buckets[index];

        // iterate through the linked list to find the key
        while (head != null){
            if (head.key.equals(key)){
                return head.value;
            }
            head = head.next;
        }

        return null;
    }

    /***
     * Checks if the hashmap contains a mapping for the specified key.
     * @param key The key whose presence is to be searched
     * @return True if this map contains the specified key, false otherwise.
     */
    public boolean containsKey(K key){
        int index = getIndex(key);
        MyEntry<K, V> head = buckets[index];

        while (head != null){
            if (head.key.equals(key)){
                return true;
            }
            head = head.next;
        }
        return false;
    }

    /***
     * Removes the mapping for a key from this map if it is present
     * Adjusts the linked list to maintain structure after removal.
     * @param key The key whose mapping is to be removed
     */
    public void remove(K key){
        int index = getIndex(key);
        MyEntry<K, V> head = buckets[index];
        MyEntry<K, V> prev = null;

        while (head != null){
            if (head.key.equals(key)){
                if (prev != null){
                    prev.next = head.next;
                } else {
                    buckets[index] = head.next;
                }
                currentSize--;
                return;
            }
            prev = head;
            head = head.next;
        }
    }


    /***
     * Resizes the hashmap when the load factor exceeds the threshold.
     * Doubles and adds 1 to the capacity and rehashes all entries to new bucket locations.
     */
    @SuppressWarnings("unchecked")
    private void resize(){
        int newCapacity = capacity * 2 + 1;
        MyEntry<K, V>[] newBuckets = (MyEntry<K, V>[]) new MyEntry[newCapacity];

        // rehash all entries into the new buckets.
        for (int i = 0; i < capacity; i++){
            MyEntry<K, V> head = buckets[i];
            while (head != null){
                MyEntry<K, V> next = head.next;
                int newIndex = Math.abs(head.key.hashCode()) % newCapacity;
                head.next = newBuckets[newIndex];
                newBuckets[newIndex] = head;
                head = next;
            }
        }
        buckets = newBuckets;
        capacity = newCapacity;
    }

    /***
     * Retrieves all values stored in the hashmap.
     * @return An ArrayList of all values in the map.
     */
    public ArrayList<V> values(){
        ArrayList<V> valuesList = new ArrayList<>();
        for (int i = 0; i < capacity; i++){
            MyEntry<K, V> head = buckets[i];
            while (head != null){
                valuesList.add(head.value);
                head = head.next;
            }
        }
        return valuesList;
    }
}
