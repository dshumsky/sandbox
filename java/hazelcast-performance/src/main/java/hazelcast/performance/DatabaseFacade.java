package hazelcast.performance;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DatabaseFacade {

    @PersistenceContext
    EntityManager entityManager;
    private final static int FETCHSIZE = 1000;

    @SuppressWarnings("unchecked")
    @Transactional
    public void loadTodos(Consumer<Pair<TodoKey, TodoValue>> consumer, long rows) {
        final SQLQuery sqlQuery = getCurrentSession().createSQLQuery(
                "select C1705_COUNTRYID,      \n" +
                        " C1705_ADVDATEID,    \n" +
                        " C1705_ITEMID,       \n" +
                        " C1705_STOREID,      \n" +
                        " C1705_WHID,         \n" +
                        " C1705_ADVDATE_START,\n" +
                        " C1705_ADVREGIONID   \n" +
                        "FROM T1705_ITEM_STORE_TODO_HR \n" +
                        "where rownum<=" + rows);
        sqlQuery.setFetchSize(FETCHSIZE);
        sqlQuery.addScalar("C1705_COUNTRYID", LongType.INSTANCE);
        sqlQuery.addScalar("C1705_ADVDATEID", LongType.INSTANCE);
        sqlQuery.addScalar("C1705_ITEMID", LongType.INSTANCE);
        sqlQuery.addScalar("C1705_STOREID", LongType.INSTANCE);
        sqlQuery.addScalar("C1705_WHID", LongType.INSTANCE);
        sqlQuery.addScalar("C1705_ADVDATE_START", DateType.INSTANCE);
        sqlQuery.addScalar("C1705_ADVREGIONID", LongType.INSTANCE);
        sqlQuery.setResultTransformer(new ResultTransformer() {
            int processedRows = 0;

            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                TodoKey key = new TodoKey();
                key.setCountryId((Long) tuple[0]);
                key.setAdvDateId((Long) tuple[1]);
                key.setItemid((Long) tuple[2]);
                key.setStoreid((Long) tuple[3]);
                key.setWhId((Long) tuple[4]);
                TodoValue value = new TodoValue();
                value.setAdvDateStart(toLocalDate(((Date) tuple[5])));
                value.setAdvRegionId((Long) tuple[6]);
                processedRows++;
                if (processedRows % 100_000 == 0)
                    log.info("loadTodos: processed {} rows", processedRows);
                return Pair.with(key, value);
            }

            @Override
            public List transformList(List collection) {
                return collection;
            }
        });
        final ScrollableResults list = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);
        while (list.next()) {
            consumer.accept((Pair<TodoKey, TodoValue>) list.get()[0]);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Transactional
    public void loadItemSales(Consumer<Pair<ItemSaleKey, ItemSaleValue>> consumer, long rows) {
        final SQLQuery sqlQuery = getCurrentSession().createSQLQuery(
                "select C085_ITEMID,      \n" +
                        " C085_STOREID,       \n" +
                        " C085_WHID,          \n" +
                        " C085_RECEIPTDATE,   \n" +
                        " C085_SUM_QTY_PIECE, \n" +
                        " C085_SUM_TURNOVER   \n" +
                        "FROM T085_ITEMSALE_DAY \n" +
                        "where rownum<=" + rows);
        sqlQuery.setFetchSize(FETCHSIZE);
        sqlQuery.addScalar("C085_ITEMID", LongType.INSTANCE);
        sqlQuery.addScalar("C085_STOREID", LongType.INSTANCE);
        sqlQuery.addScalar("C085_WHID", LongType.INSTANCE);
        sqlQuery.addScalar("C085_RECEIPTDATE", DateType.INSTANCE);
        sqlQuery.addScalar("C085_SUM_QTY_PIECE", LongType.INSTANCE);
        sqlQuery.addScalar("C085_SUM_TURNOVER", LongType.INSTANCE);

        sqlQuery.setResultTransformer(new ResultTransformer() {
            int processedRows = 0;

            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                ItemSaleKey key = new ItemSaleKey();
                key.setItemId((Long) tuple[0]);
                key.setStoreId((Long) tuple[1]);
                key.setWhId((Long) tuple[2]);
                key.setReceiptDate(toLocalDate(((Date) tuple[3])));

                ItemSaleValue value = new ItemSaleValue();
                value.setSumQtyPiece((Long) tuple[4]);
                value.setSumTurnover((Long) tuple[5]);

                consumer.accept(Pair.with(key, value));
                processedRows++;
                if (processedRows % 100_000 == 0)
                    log.info("loadItemSales: processed {} rows", processedRows);
                return null;
            }

            @Override
            public List transformList(List collection) {
                return null;
            }
        });
        final ScrollableResults results = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);
        while (results.next()) {
        }
    }

    private LocalDate toLocalDate(java.util.Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }

    /*
     * select
     * todos.C1705_COUNTRYID,
     * todos.C1705_ADVDATE_START,
     * todos.C1705_ITEMID,
     * todos.C1705_STOREID,
     * todos.C1705_ADVDATEID,
     * todos.C1705_ADVREGIONID
     * itemSale.C085_RECEIPTDATE,
     * coalesce(itemSale.C085_SUM_QTY_PIECE, 0),
     * coalesce(itemSale.C085_SUM_TURNOVER, 0),
     * FROM t1705_item_store_todo_hr todos
     * INNER JOIN T085_ITEMSALE_DAY itemSale
     * ON itemSale.C085_ITEMID = todos.C1705_ITEMID
     * AND itemSale.C085_STOREID = todos.C1705_STOREID
     * AND itemSale.C085_WHID IN (10, 20)
     * AND itemSale.C085_RECEIPTDATE >= greatest(coalesce(todos.C1705_FIXED_DATE + 1, to_date('1970-01-01','YYYY-MM-DD')), todos.C1705_ADVDATE_START + 30)
     * WHERE todos.C1705_WHID = 10
     * and rownum<2
     * ORDER BY
     * todos.C1705_ADVDATEID ASC,
     * todos.C1705_ITEMID ASC,
     * todos.C1705_STOREID ASC,
     * itemSale.C085_RECEIPTDATE ASC
     */

    /*
     * final long start = System.currentTimeMillis();
     * print(query, 0, start);
     * final SQLQuery sqlQuery = getCurrentSession().createSQLQuery(query);
     * sqlQuery.setFetchSize(fetchSize);
     * final AtomicInteger index = new AtomicInteger(0);
     * final AtomicInteger printed = new AtomicInteger(0);
     * final AtomicReference<Object[]> tupleReference = new AtomicReference<>();
     * sqlQuery.setResultTransformer(new ResultTransformer() {
     * @Override
     * public Object transformTuple(final Object[] tuple, final String[] aliases) {
     * final int i = index.incrementAndGet();
     * if (i == 1 || i % n == 0) {
     * print(tuple, i, start);
     * printed.set(i);
     * }
     * tupleReference.set(tuple);
     * return tuple;
     * }
     * @Override
     * public List transformList(final List collection) {
     * return collection;
     * }
     * });
     * final ScrollableResults list = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);
     * int size = 0;
     * while (list.next()) {
     * list.get();
     * size++;
     * }
     * if (tupleReference.get() == null) {
     * print("returns 0 rows", 0, start);
     * } else if (size != printed.get()) {
     * print(tupleReference.get(), size, start);
     * }
     * }
     * private void print(final Object[] tuple, final int row, final long start) {
     * print(Arrays.toString(tuple), row, start);
     * }
     * private void print(final String s, final int row, final long start) {
     * System.out.println("time=" + (System.currentTimeMillis() - start) + ", row[" + row + "]=" + s);
     * }
     */
    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
}
