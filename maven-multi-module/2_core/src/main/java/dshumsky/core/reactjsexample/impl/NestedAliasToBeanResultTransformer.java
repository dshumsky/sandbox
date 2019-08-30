package dshumsky.core.reactjsexample.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class NestedAliasToBeanResultTransformer<T> extends AliasedTupleSubsetResultTransformer {

    private static final long serialVersionUID = -9026031013709849199L;

    public static final String ELEMENT = "element";
    public static final String KEY = "key";

    private final Class<T> resultClass;
    private final Map<String, Integer> aliasIndexes = new HashMap<>();
    private final Map<String, String> renamedAliasMap;
    private boolean isInitialized;

    /**
     * Map with cached AccessProperties instances.
     * The key pair is (Class, fieldName) and value is AccessProperties instance.
     * Performance: HashBasedTable is typed but slower than MultiKeyMap.
     * Thread-safety: doesn't need to be thread-safe, it's called from one Hibernate thread
     */
    private final MultiKeyMap cachedFieldMap = new MultiKeyMap();

    /**
     * Class used as a value for access properties of class/fieldName pairs.
     */
    private static class AccessProperties {
        private final Field field;
        private final Method writeMethod;
        private final Method readMethod;

        public AccessProperties(Field field, Method writeMethod, Method readMethod) {
            this.field = field;
            this.writeMethod = writeMethod;
            this.readMethod = readMethod;
        }

        // getters/setters omitted for performance reasons
    }

    /**
     * Creates a new instance of {@link NestedAliasToBeanResultTransformer}
     *
     * @param resultClass the class of the dto to populate
     */
    public NestedAliasToBeanResultTransformer(Class<T> resultClass) {
        this(resultClass, new HashMap<>());
    }

    /**
     * Creates a new instance of {@link NestedAliasToBeanResultTransformer}
     *
     * @param resultClass the class of the dto to populate
     * @param renamedAliasMap a map from column aliases to property expressions. This is usefull, if the property
     *            expression is too long (more then 30 characters) to be used as as a column alias. Then you can assign
     *            a short name to the column and map it to the real property expression.
     */
    public NestedAliasToBeanResultTransformer(Class<T> resultClass, Map<String, String> renamedAliasMap) {
        this.resultClass = resultClass;
        this.renamedAliasMap = renamedAliasMap;
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] arg0, int arg1) {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public List transformList(List list) {
        return new ArrayList(new LinkedHashSet(list)); // linked hash set to keep order
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        initialize(aliases);
        String alias = null;
        Object value = null;
        try {
            // instantiate the bean or get it from the alreadyTransformedMap if it's already been instantiated
            // while processing some previous result set row
            T result = resultClass.newInstance();

            // iterate over the columns (only those with defined scalar aliases) and set the values
            for (int i = 0; i < aliases.length; i++) {
                value = tuple[i];
                if (value != null) {
                    alias = getRealAlias(aliases[i]);
                    setNestedProperty(result, alias, value, tuple);
                }

            }

            return result;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException
                | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            throw new RuntimeException("Unable to transform value=" + value + ", for alias=" + alias, e);
        }
    }

    private void initialize(String[] aliases) {
        if (!isInitialized) {
            for (int i = 0; i < aliases.length; i++) {
                String alias = getRealAlias(aliases[i]);
                aliasIndexes.put(alias, i); // remember tuple index of each alias
            }
            isInitialized = true;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setNestedProperty(Object bean, String name, Object value, Object[] tuple) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchFieldException, InstantiationException, NoSuchMethodException, SecurityException {
        Preconditions.checkNotNull(bean, "Cannot set property on a null bean");
        Preconditions.checkNotNull(name, "Cannot set property with null name");

        String nextProperty;
        String pathToBean = "";
        Type declaredBeanType = null;

        int cursorStart = 0;
        int cursorNextDot;
        while ((cursorNextDot = name.indexOf('.', cursorStart)) != -1) { // iterate through the dot notation property path
            nextProperty = name.substring(cursorStart, cursorNextDot);
            cursorStart = cursorNextDot + 1;

            boolean isMap = bean instanceof Map;

            // .key column values are not directly written, they are used when writing the values
            if (isMap && KEY.equals(nextProperty)) {
                continue;
            }

            // since there's at least one more property in the path ahead, nestedBean must be a Map or a DTO
            Object nestedBean = getProperty(bean, nextProperty, pathToBean, tuple);

            if (nestedBean == null) { // instantiate the nested bean if necessary
                Class nestedBeanClass;

                // if current parent bean is a Map, we need to resolve the type of the value from the field type
                // declared in it's container bean, which we have remembered in the previous iteration
                if (isMap) {
                    ParameterizedType parameterizedType = (ParameterizedType) declaredBeanType;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    nestedBeanClass = (Class) actualTypeArguments[1];
                } else { // else it's a simple field, we can read it's type from it's field declaration
                    nestedBeanClass = findAccessProperties(bean.getClass(), nextProperty).field.getType();
                }

                // instantiate using default constructor
                nestedBean = nestedBeanClass.getConstructor().newInstance();

                // and add the new instance to the current parent bean (assign to field or add to Map)
                setProperty(bean, nextProperty, nestedBean, pathToBean, tuple);
            }

            // we don't support Maps of Maps so if current parent bean is a Map, the
            // next bean mustn't be a Map, so we won't need its declared type
            if (isMap) {
                declaredBeanType = null;
            } else {
                declaredBeanType = findAccessProperties(bean.getClass(), nextProperty).field.getGenericType();
            }

            // move to next property for the next iteration
            bean = nestedBean;
            pathToBean = append(pathToBean, nextProperty);
        }

        nextProperty = name.substring(cursorStart);
        // we're at the end of the path, let's set the value
        setProperty(bean, nextProperty, value, pathToBean, tuple);
    }

    @SuppressWarnings("rawtypes")
    private Object getProperty(Object bean, String name, String pathToBean, Object[] tuple)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        if (bean instanceof Map) {
            if (ELEMENT.equals(name) && aliasIndexes.containsKey(append(pathToBean, KEY))) {
                // key into the map is the value of another column named pathToBean.key
                Object key = tuple[aliasIndexes.get(append(pathToBean, KEY))];
                return ((Map) bean).get(key);
            } else {
                // key into the map is the name of this column
                return ((Map) bean).get(name);
            }
        } else {
            return findAccessProperties(bean.getClass(), name).readMethod.invoke(bean);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setProperty(Object bean, String name, Object value, String pathToBean, Object[] tuple)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (bean instanceof Map) {
            if (KEY.equals(name)) {
                return; // don't set anything, the key will be used later, when setting the map values
            }
            if (ELEMENT.equals(name) && aliasIndexes.containsKey(append(pathToBean, KEY))) {
                // key into the map is the value of another column named pathToBean.key
                Object key = tuple[aliasIndexes.get(append(pathToBean, KEY))];
                ((Map) bean).put(key, value);
            } else {
                // key into the map is the name of this column
                ((Map) bean).put(name, value);
            }
        } else {
            AccessProperties ap = findAccessProperties(bean.getClass(), name);
            ap.writeMethod.invoke(bean, value);
        }
    }

    private String append(String currentPath, String nextProperty) {
        if (StringUtils.isEmpty(currentPath)) {
            return nextProperty;
        } else {
            return currentPath + "." + nextProperty;
        }
    }

    /**
     * In case that the given alias is mapped to a property expression in the {@link #renamedAliasMap} return the
     * property expression, otherwise returns the alias itself
     *
     * @param alias a column alias from the query
     * @return the mapped value or itself
     */
    private String getRealAlias(String alias) {
        String result = renamedAliasMap.get(alias);
        return result != null ? result : alias;
    }

    /**
     * Iterates through the class hierarchy and tries to find the AccessProperties
     * for the given class and field name.
     *
     * @param clazz the class where to start searching
     * @param fieldName the name of the field
     * @return the AccessProperties with the given class/field name
     */
    private AccessProperties findAccessProperties(final Class<?> clazz, String fieldName) throws NoSuchFieldException {
        AccessProperties accessProperties = (AccessProperties) cachedFieldMap.get(clazz, fieldName);

        if (accessProperties != null) {
            return accessProperties;
        }

        for (Class<?> tmpClazz = clazz; tmpClazz.getSuperclass() != null; tmpClazz = tmpClazz.getSuperclass()) { // terminates when tmpClazz is Object.class
            try {
                Field field = tmpClazz.getDeclaredField(fieldName);
                PropertyDescriptor propDescriptor = new PropertyDescriptor(fieldName, tmpClazz);
                Method writeMethod = propDescriptor.getWriteMethod();
                Method readMethod = propDescriptor.getReadMethod();

                accessProperties = new AccessProperties(field, writeMethod, readMethod);
                cachedFieldMap.put(clazz, fieldName, accessProperties);

                return accessProperties;
            } catch (NoSuchFieldException | IntrospectionException e) {
                // do nothing, continue to search
            }
        }

        throw new NoSuchFieldException("Unable to find a field with name " + fieldName + " in the " + clazz.getSimpleName() + " class hierarchy");
    }
}
