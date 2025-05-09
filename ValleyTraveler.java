/**
 * ValleyTraveler class represents a magical map that can identify and modify
 * valley points in the landscape of Numerica.
 * 
 * @author Brandon Newman
 */

public class ValleyTraveler {

    private class Node {
        int height;
        int prefixSum; // Sum from head to this node (inclusive)
        Node prev, next;

        Node(int height, int prefixSum) {
            this.height = height;
            this.prefixSum = prefixSum;
        }
    }

    private Node head, tail;
    private Node firstValley; // Pointer to the first valley
    private double totalTreasure;

    /**
     * Constructor to initialize the magical map with the given landscape of
     * Numerica.
     * 
     * @param landscape An array of distinct integers representing the landscape.
     */
    public ValleyTraveler(int[] landscape) {
        // Initialize the internal state based on the provided landscape.
        if (landscape.length == 0)
            return;

        // Build doubly linked list with prefix sums
        head = new Node(landscape[0], landscape[0]);
        Node prev = head;

        for (int i = 1; i < landscape.length; i++) {
            Node curr = new Node(landscape[i], prev.prefixSum + landscape[i]);
            prev.next = curr;
            curr.prev = prev;
            prev = curr;
        }
        tail = prev;

        // Identify first valley
        updateFirstValley();
    }

    private void updateFirstValley() {
        Node curr = head;
        int idx = 0;
        while (curr != null) {
            boolean isValley = false;
            if (curr.prev == null && curr.next == null)
                isValley = true;
            else if (curr.prev == null)
                isValley = curr.height < curr.next.height;
            else if (curr.next == null)
                isValley = curr.height < curr.prev.height;
            else
                isValley = curr.height < curr.prev.height && curr.height < curr.next.height;

            if (isValley) {
                firstValley = curr;
                return;
            }
            curr = curr.next;
            idx++;
        }
        firstValley = null;
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
        if (firstValley == null)
            return -1.0;

        int idx = 0;
        int sum = firstValley.prefixSum;
        Node curr = head;
        while (curr != firstValley) {
            idx++;
            curr = curr.next;
        }
        return (double) sum / (idx + 1);
    }

    /**
     * Excavates the first valley point, removing it from the landscape of Numerica.
     * 
     * @return The treasure collected from the excavated valley point.
     */
    public double remove() {
        // Remove the first valley point and update internal state.
        if (firstValley == null)
            return -1.0;

        double treasure = getFirst();
        totalTreasure += treasure;

        // Remove firstValley from the list
        Node toRemove = firstValley;

        if (toRemove.prev == null && toRemove.next == null) {
            head = tail = null;
        } else if (toRemove.prev == null) {
            head = toRemove.next;
            head.prev = null;
        } else if (toRemove.next == null) {
            tail = toRemove.prev;
            tail.next = null;
        } else {
            toRemove.prev.next = toRemove.next;
            toRemove.next.prev = toRemove.prev;
        }

        // Recalculate prefix sums from the removed node's next
        Node curr = (toRemove.prev == null) ? head : toRemove.next;
        if (curr != null) {
            Node prev = curr.prev;
            while (curr != null) {
                curr.prefixSum = (prev == null) ? curr.height : prev.prefixSum + curr.height;
                prev = curr;
                curr = curr.next;
            }
        }

        updateFirstValley();
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
        if (firstValley == null)
            return; // Shouldn't happen due to problem guarantees

        Node newNode = new Node(height, 0);

        if (firstValley.prev == null) {
            // Insert at head
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else {
            // Insert before firstValley
            Node prev = firstValley.prev;
            prev.next = newNode;
            newNode.prev = prev;
            newNode.next = firstValley;
            firstValley.prev = newNode;
        }

        // Recompute prefix sums from newNode onward
        Node curr = newNode;
        Node prev = newNode.prev;
        while (curr != null) {
            curr.prefixSum = (prev == null) ? curr.height : prev.prefixSum + curr.height;
            prev = curr;
            curr = curr.next;
        }

        updateFirstValley();
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
