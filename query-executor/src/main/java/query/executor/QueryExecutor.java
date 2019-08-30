package query.executor;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Service
public class QueryExecutor {

    public static final Logger LOG = Logger.getLogger(QueryExecutor.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void execute(final String query, final int n, final int fetchSize) {
        final long start = System.currentTimeMillis();
        print(query, 0, start);
        final SQLQuery sqlQuery = getCurrentSession().createSQLQuery(query);
        sqlQuery.setFetchSize(fetchSize);
        final AtomicInteger index = new AtomicInteger(0);
        final AtomicInteger printed = new AtomicInteger(0);
        final AtomicReference<Object[]> tupleReference = new AtomicReference<>();

        sqlQuery.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(final Object[] tuple, final String[] aliases) {
                final int i = index.incrementAndGet();
                if (i == 1 || i % n == 0) {
                    print(tuple, i, start);
                    printed.set(i);
                }
                tupleReference.set(tuple);
                return tuple;
            }

            @Override
            public List transformList(final List collection) {
                return collection;
            }
        });
        final ScrollableResults list = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);
        int size = 0;
        while (list.next()) {
            list.get();
            size++;
        }
        if (tupleReference.get() == null) {
            print("returns 0 rows", 0, start);
        } else if (size != printed.get()) {
            print(tupleReference.get(), size, start);
        }
    }

    private void print(final Object[] tuple, final int row, final long start) {
        print(Arrays.toString(tuple), row, start);
    }

    private void print(final String s, final int row, final long start) {
        System.out.println("time=" + (System.currentTimeMillis() - start) + ", row[" + row + "]=" + s);
    }

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
}
