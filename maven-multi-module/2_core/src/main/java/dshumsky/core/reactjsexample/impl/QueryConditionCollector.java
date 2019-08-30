package dshumsky.core.reactjsexample.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.hibernate.Query;

/**
 * Helps to collect sql conditions and pass parameters to hibernate queries.
 *
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class QueryConditionCollector {

    private final List<Condition> conditions = new ArrayList<>();

    public QueryConditionCollector() {
    }

    /**
     * Adds sql-condition to collector
     *
     * @param needToAdd - only if it's true condition will be added
     * @param sql - the sql condition
     * @param setValueFunction - function to pass value
     * @return a reference to this object.
     */
    public QueryConditionCollector add(boolean needToAdd, String sql, Function<Query, Void> setValueFunction) {
        if (needToAdd) {
            conditions.add(new Condition(sql, setValueFunction));
        }
        return this;
    }

    /**
     * Adds sql-condition to collector
     *
     * @param sql - the sql condition
     * @param setValueFunction - function to pass value
     * @return a reference to this object.
     */
    public QueryConditionCollector add(String sql, Function<Query, Void> setValueFunction) {
        conditions.add(new Condition(sql, setValueFunction));
        return this;
    }

    /**
     * Adds sql-condition to collector
     *
     * @param sql - the sql condition
     * @return a reference to this object.
     */
    public QueryConditionCollector add(String sql) {
        conditions.add(new Condition(sql, null));
        return this;
    }

    /**
     * @return conditions in the form "(condition#1) AND (condition#2) ..."
     */
    public String sqlUsingAnd() {
        if (conditions.isEmpty())
            return "(1=1)";
        StringBuilder sql = new StringBuilder();
        for (Condition condition : conditions) {
            sql.append("(").append(condition.sql).append(") AND");
        }
        if (sql.length() > 0) {
            sql.setLength(sql.length() - 4);
        }
        return sql.toString();
    }

    /**
     * pass parameters to query
     */
    public void setParameters(Query query) {
        for (Condition condition : conditions) {
            if (condition.setValueFunction != null) {
                condition.setValueFunction.apply(query);
            }
        }
    }

    static class Condition {
        private final String sql;
        private final Function<Query, Void> setValueFunction;

        Condition(String sql, Function<Query, Void> setValueFunction) {
            this.sql = sql;
            this.setValueFunction = setValueFunction;
        }
    }
}

