package thonk;

/**
 * A convenience class to represent name-value pairs.
 * @param <T> Pair 1
 * @param <U> Pair 2
 */
public class Pair<T, U> {
    private final T t;
    private final U u;

    /**
     * Creates a new pair
     * @param t key
     * @param u value
     */
    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }
    /**
     * Gets the key for this pair.
     * @return key
     */
    public T t() {
        return this.t;
    }

    /**
     * Gets the value for this pair
     * @return value
     */
    public U u() {
        return this.u;
    }

}
