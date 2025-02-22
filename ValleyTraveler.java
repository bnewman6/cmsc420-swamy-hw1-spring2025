/**
 * ValleyTraveler class represents a magical map that can identify and modify
 * valley points in the landscape of Numerica.
 * 
 * @author Brandon Newman
 */
class Node {
    long value;
    double sum;
    long idx;
    Node valley;
    Node next;
    Node prev;

    public Node(int value, double sum, long idx) {
        this.value = value;
        this.sum = sum;
        this.idx = idx;
        this.valley = null;
        this.next = null;
        this.prev = null;
    }
}

public class ValleyTraveler {

    // Instance variables to manage the landscape and collected treasures.
    private double totalTreasure;
    private int size;
    private Node head, tail, valley;

    /**
     * Constructor to initialize the magical map with the given landscape of
     * Numerica.
     * 
     * @param landscape An array of distinct integers representing the landscape.
     */
    public ValleyTraveler(int[] landscape) {
        // Initialize the internal state based on the provided landscape.
        this.size = landscape.length;
        this.totalTreasure = 0;

        if (size == 0)
            return;

        Node prev = null;
        double sum = 0;

        for (int i = 0; i < size; i++) {
            sum += landscape[i];
            Node node = new Node(landscape[i], sum, i + 1);

            if (prev != null) {
                if (i == size - 1) {
                    if (prev.value > node.value) {
                        node.valley = node;
                    }
                } else if (prev.value > node.value && node.value < landscape[i + 1]) {
                    node.valley = node;
                }
                prev.next = node;
                node.prev = prev;
            } else {
                if (i == size - 1 || node.value < landscape[i + 1]) {
                    node.valley = node;
                }
                head = node;
            }
            prev = node;
        }
        tail = prev;

        Node curr = tail;
        while (curr.prev != null) {
            curr = curr.prev;
            if (curr.valley == null) {
                curr.valley = curr.next.valley;
            }
        }
        valley = head.valley;
    }

    /**
     * Checks if the entire landscape is excavated (i.e., there are no landforms
     * left).
     * 
     * @return true if the landscape is empty, false otherwise.
     */
    public boolean isEmpty() {
        // Determine if the landscape is empty.
        return head == null;
    }

    /**
     * Locates the first valley point in the landscape of Numerica.
     * 
     * @return The treasure associated with the first valley point.
     */
    public double getFirst() {
        // Locate the first valley point and calculate its treasure.
        return valley.sum / valley.idx;
    }

    /**
     * Excavates the first valley point, removing it from the landscape of Numerica.
     * 
     * @return The treasure collected from the excavated valley point.
     */
    public double remove() {
        // Remove the first valley point and update internal state.
        double treasure = getFirst();
        Node curr = valley;
        double currSum = curr.value;

        if (curr == head) {
            if (curr == tail) {
                head = tail = valley = null;
            } else {
                head = curr.next;
                head.prev = null;
                if (head.next != null) {
                    if (head.next.value > head.value) {
                        head.valley = head;
                        valley = head;
                    } else {
                        valley = head.valley;
                    }
                } else {
                    tail = head;
                    valley = head;
                }
            }
        } else if (curr == tail) {
            curr.prev.next = null;
            tail = curr.prev;
            valley = tail;
        } else if (curr.prev == head) {
            if (curr.prev.value < curr.next.value) {
                head.valley = head;
                valley = head;
            } else if (curr.next == tail || curr.next.next.value > curr.next.value) {
                curr.next.valley = curr.next;
                valley = curr.next;
            } else {
                valley = curr.next.valley;
            }
            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
        } else if (curr.next == tail) {
            if (curr.next.value < curr.prev.value) {
                curr.next.valley = curr.next;
                valley = tail;
                curr.prev.valley = curr.next.valley;
            } else if (curr.prev.prev.value > curr.prev.value) {
                curr.prev.valley = curr.prev;
                valley = curr.prev;
            }
            curr.next.prev = curr.prev;
            curr.prev.next = curr.next;
        } else {
            if (curr.prev.prev.value > curr.prev.value && curr.prev.value < curr.next.value) {
                curr.prev.valley = curr.prev;
                valley = curr.prev;
            } else if (curr.prev.value > curr.next.value && curr.next.value < curr.next.next.value) {
                curr.next.valley = curr.next;
                curr.prev.valley = curr.next.valley;
                valley = curr.next;
            }
            curr.next.prev = curr.prev;
            curr.prev.next = curr.next;
        }

        while (curr.next != null) {
            curr = curr.next;
            curr.sum -= currSum;
            curr.idx--;
        }
        totalTreasure += treasure;
        size--;
        return treasure;
    }

    /**
     * Creates a new landform at the position where the first valley was just
     * removed.
     * 
     * @param height The height of the new landform.
     */
    public void insert(int height) {
        // Insert a new landform at the correct position.
        Node curr = new Node(height, valley.sum - valley.value + height, valley.idx);
        curr.next = valley;
        curr.prev = valley.prev;
        valley.prev = curr;
        curr.valley = curr.next.valley;

        if (valley == head) {
            head = curr;
            if (curr.value < valley.value) {
                curr.valley = curr;
                valley = curr;
            }
        } else {
            curr.prev.next = curr;
            if (curr.value > valley.value) {
                if (curr.prev.value < curr.value) {
                    if (curr.prev == head) {
                        curr.prev.valley = curr.prev;
                        valley = head;
                    } else if (curr.prev.prev.value > curr.prev.value) {
                        curr.prev.valley = curr.prev;
                        valley = curr.prev;
                    }
                }
            } else {
                curr.valley = curr;
                curr.prev.valley = curr.valley;
                valley = curr;
            }
        }

        while (curr.next != null) {
            curr = curr.next;
            curr.sum += height;
            curr.idx++;
        }
        size++;
    }

    /**
     * Returns the current total treasure collected through successive remove
     * operations.
     * 
     * @return The total treasure collected.
     */
    public double getTotalTreasure() {
        // Calculate and return the total treasure collected.
        return totalTreasure;
    }
}
