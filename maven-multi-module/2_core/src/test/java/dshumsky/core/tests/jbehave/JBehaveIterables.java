package dshumsky.core.tests.jbehave;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.javatuples.Triplet;

import com.google.common.base.Function;

/**
 * This class contains static utility methods that operate on objects of type {@code Iterable}
 */
public class JBehaveIterables {

    private JBehaveIterables() {
    }

    /**
     * Applies {@code function} to each combination of elements from {@code aIterable} and {@code bIterable}.
     * 
     * @param aIterable {@link Iterable}
     * @param bIterable {@link Iterable}
     * @param function {@link Function}
     */
    public static <A, B> void forEach(Iterable<A> aIterable, Iterable<B> bIterable, Function<Pair<A, B>, Void> function) {
        for (A a : aIterable) {
            for (B b : bIterable) {
                function.apply(Pair.with(a, b));
            }
        }
    }

    /**
     * Applies {@code function} to each combination of elements from {@code aIterable}, {@code bIterable} and
     * {@code cIterable}
     * 
     * @param aIterable {@link Iterable}
     * @param bIterable {@link Iterable}
     * @param cIterable {@link Iterable}
     * @param function {@link Function}
     */
    public static <A, B, C> void forEach(Iterable<A> aIterable, Iterable<B> bIterable, Iterable<C> cIterable,
            Function<Triplet<A, B, C>, Void> function) {
        for (A a : aIterable) {
            for (B b : bIterable) {
                for (C c : cIterable) {
                    function.apply(Triplet.with(a, b, c));
                }
            }
        }
    }

    /**
     * Applies {@code function} to each combination of elements from {@code aIterable}, {@code bIterable},
     * {@code cIterable} and {@code dIterable}
     * 
     * @param aIterable {@link Iterable}
     * @param bIterable {@link Iterable}
     * @param cIterable {@link Iterable}
     * @param dIterable {@link Iterable}
     * @param function {@link Function}
     */
    public static <A, B, C, D> void forEach(Iterable<A> aIterable, Iterable<B> bIterable, Iterable<C> cIterable, Iterable<D> dIterable,
            Function<Quartet<A, B, C, D>, Void> function) {
        for (A a : aIterable) {
            for (B b : bIterable) {
                for (C c : cIterable) {
                    for (D d : dIterable) {
                        function.apply(Quartet.with(a, b, c, d));
                    }
                }
            }
        }
    }

    /**
     * Applies {@code function} to each combination of elements from {@code aIterable}, {@code bIterable},
     * {@code cIterable}, {@code dIterable} and {@code eIterable}
     * 
     * @param aIterable {@link Iterable}
     * @param bIterable {@link Iterable}
     * @param cIterable {@link Iterable}
     * @param dIterable {@link Iterable}
     * @param eIterable {@link Iterable}
     * @param function {@link Function}
     */
    public static <A, B, C, D, E> void forEach(Iterable<A> aIterable, Iterable<B> bIterable, Iterable<C> cIterable, Iterable<D> dIterable,
            Iterable<E> eIterable, Function<Quintet<A, B, C, D, E>, Void> function) {
        for (A a : aIterable) {
            for (B b : bIterable) {
                for (C c : cIterable) {
                    for (D d : dIterable) {
                        for (E e : eIterable) {
                            function.apply(Quintet.with(a, b, c, d, e));
                        }
                    }
                }
            }
        }
    }
}