import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int _size = 0;
    private Item[] _queue = (Item[]) new Object[2];

    public RandomizedQueue() { }

    // is the queue empty?
    public boolean isEmpty() {
        return _size == 0;
    }

    // return the number of items on the queue
    public int size() {
        return _size;
    }

    // add the item
    public void enqueue(Item item) {
        checkOnNull(item);

        increaseSizeIfNeed();

        _queue[_size] = item;
        _size += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        checkOnEmpty();

        int lastIndex = _size - 1;
        int randomIndex = StdRandom.uniform(_size);
        Item item = _queue[randomIndex];

        if (randomIndex == lastIndex) {
            _queue[randomIndex] = null;
        } else {
            _queue[randomIndex] = _queue[lastIndex];
            _queue[lastIndex] = null;
        }

        _size -= 1;
        decreaseSizeIfNeed();
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        checkOnEmpty();
        int randomIndex = StdRandom.uniform(_size);
        return _queue[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void checkOnNull(Item item) {
        if (item == null) throw new NullPointerException();
    }

    private void checkOnEmpty() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    private void increaseSizeIfNeed() {
        if (_size == _queue.length) {
            resize(2 * _queue.length);
        }
    }

    private void decreaseSizeIfNeed() {
        if (_size > 0 && _size == _queue.length / 4) {
            resize(_queue.length / 2);
        }
    }

    private void resize(int max) {
        assert max >= _size;
        Item[] temp = (Item[]) new Object[max];

        for (int i = 0; i < _size; i++) {
            temp[i] = _queue[i];
        }
        _queue = temp;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private int[] indx;

        public RandomizedQueueIterator() {
            indx = new int[_size];
            for (int j = 0; j < _size; j++) {
                indx[j] = j;
            }
            StdRandom.shuffle(indx);
        }

        public boolean hasNext() {
            return i < _size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return _queue[indx[i++]];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();

        for (int i = 0; i < 10; i++)
            rq.enqueue(i);

        for (int i = 0; i < 10; i++)
            rq.dequeue();

        for (int i = 0; i < 5; i++)
            System.out.println(rq.sample());
    }
}