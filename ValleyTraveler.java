/**
 * ValleyTraveler class represents a magical map that can identify and modify
 * valley points in the landscape of Numerica.
 * 
 * @author Brandon Newman
 */
public class ValleyTraveler {

    // Instance variables to manage the landscape and collected treasures.
    int[] numerica;
    double totalTreasure = 0;

    /**
     * Constructor to initialize the magical map with the given landscape of
     * Numerica.
     * 
     * @param landscape An array of distinct integers representing the landscape.
     */
    public ValleyTraveler(int[] landscape) {
        // Initialize the internal state based on the provided landscape.
        numerica = landscape.clone();
    }

    /**
     * Checks if the entire landscape is excavated (i.e., there are no landforms
     * left).
     * 
     * @return true if the landscape is empty, false otherwise.
     */
    public boolean isEmpty() {
        // Determine if the landscape is empty.
        return numerica.length == 0;
    }

    /**
     * Locates the first valley point in the landscape of Numerica.
     * 
     * @return The treasure associated with the first valley point.
     */
    public double getFirst() {
        // Locate the first valley point and calculate its treasure.
        if (isEmpty()) {
            return -1;
        } else if (numerica.length == 1 || numerica[0] < numerica[1]) {
            return 0;
        }

        for (int i = 1; i < numerica.length - 1; i++) {
            if (numerica[i] < numerica[i - 1] && numerica[i] < numerica[i + 1]) {
                return i;
            }
        }
        if (numerica[numerica.length - 1] < numerica[numerica.length - 2]) {
            return numerica.length - 1;
        }
        return -1;
    }

    /**
     * Excavates the first valley point, removing it from the landscape of Numerica.
     * 
     * @return The treasure collected from the excavated valley point.
     */
    public double remove() {
        // Remove the first valley point and update internal state.
        double idx = getFirst();
        if (idx == -1) {
            return 0;
        }

        int[] numerica2 = new int[numerica.length - 1];
        double treasure = 0;

        for (int i = 0, k = 0; i < numerica.length; i++) {
            if (i <= idx) {
                treasure += numerica[i];
            }

            if (i != idx) {
                numerica2[k++] = numerica[i];
            }
        }

        numerica = numerica2;

        totalTreasure += treasure / (idx + 1);
        return treasure / (idx + 1);
    }

    /**
     * Creates a new landform at the position where the first valley was just
     * removed.
     * 
     * @param height The height of the new landform.
     */
    public void insert(int height) {
        // Insert a new landform at the correct position.
        double idx = getFirst();
        if (idx == -1) {
            idx = 0;
        }
        int[] numerica2 = new int[numerica.length + 1];

        for (int i = 0, k = 0; i < numerica.length; i++) {
            if (i == idx) {
                numerica2[k++] = height;
            }
            numerica2[k++] = numerica[i];
        }

        numerica = numerica2;
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
