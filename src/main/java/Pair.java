public class Pair<T, U> {
    private final T t;
    private final U u;

    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    T t() {
        return this.t;
    }

    U u() {
        return this.u;
    }

}
