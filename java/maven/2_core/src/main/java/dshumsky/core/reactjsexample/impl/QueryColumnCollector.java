package dshumsky.core.reactjsexample.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.type.Type;

/**
 * Helps to collect sql columns and add scalars to hibernate queries.
 *
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class QueryColumnCollector {

    private final Map<String, String> renamedAliasMap = new HashMap<>();
    private final List<Column> columns = new ArrayList<>();

    /**
     * Adds column to collector
     *
     * @param needToAdd - only if it's true column will be added
     * @param name      - the name
     * @param alias     - the alias of the column
     * @param type      The Hibernate type as which to treat the value. If null, then addScalars will skip the column
     * @return a reference to this object.
     */
    public QueryColumnCollector add(boolean needToAdd, String name, String alias, Type type) {
        if (needToAdd) {
            columns.add(new Column(name, renameAlias(alias), type));
        }
        return this;
    }

    /**
     * Adds column to collector
     *
     * @param needToAdd - only if it's true column will be added
     * @param name      - the name
     * @param type      The Hibernate type as which to treat the value. If null, then addScalars will skip the column
     * @return a reference to this object.
     */
    public QueryColumnCollector add(boolean needToAdd, String name, Type type) {
        if (needToAdd) {
            columns.add(new Column(name, null, type));
        }
        return this;
    }

    /**
     * Adds column to collector
     *
     * @param name  - the name
     * @param alias - the alias of the column
     * @param type  The Hibernate type as which to treat the value. If null, then addScalars will skip the column
     * @return a reference to this object.
     */
    public QueryColumnCollector add(String name, String alias, Type type) {
        columns.add(new Column(name, renameAlias(alias), type));
        return this;
    }

    /**
     * Adds column to collector
     *
     * @param name - the name
     * @param type The Hibernate type as which to treat the value. If null, then addScalars will skip the column
     * @return a reference to this object.
     */
    public QueryColumnCollector add(String name, Type type) {
        columns.add(new Column(name, null, type));
        return this;
    }

    /**
     * @return columns in the form "columnName1, columnAlias2 ..."
     */
    public String getColumns() {
        return getColumns(false);
    }

    /**
     * @return columns in the form "columnName1 AS columnAlias1, columnName2 AS columnAlias2 ..."
     */
    public String getColumnsWithAliases() {
        return getColumns(true);
    }

    public String getColumns(boolean withAliases) {
        if (columns.isEmpty())
            return "";
        StringBuilder result = new StringBuilder();
        for (Column column : columns) {
            result.append(column.name);
            if (withAliases && column.alias != null) {
                result.append(" AS ").append(column.alias);
            }
            result.append(", ");
        }
        if (result.length() > 0) {
            result.setLength(result.length() - 2);
        }
        return result.toString();
    }

    /**
     * Adds scalars to query
     *
     * @param query - the query
     */
    public void addScalars(SQLQuery query) {
        for (Column column : columns) {
            if (column.type!=null) {
                query.addScalar(column.alias != null ? column.alias : column.name, column.type);
            }
        }
    }

    /**
     * @return a map from column aliases to property expressions.
     * It is supposed to be used for NestedAliasToBeanResultTransformer
     */
    public Map<String, String> getRenamedAliasMap() {
        return Collections.unmodifiableMap(renamedAliasMap);
    }

    private String renameAlias(String alias) {
        String result = alias;
        if (alias.length() > 31 || alias.contains(".")) {
            result = "LANH" + renamedAliasMap.size();
            renamedAliasMap.put(result, alias);
        }
        return result;
    }

    static class Column {
        private final String name;
        private final String alias;
        private final Type type;

        public Column(String name, String alias, Type type) {
            this.name = name;
            this.alias = alias;
            this.type = type;
        }
    }
}
