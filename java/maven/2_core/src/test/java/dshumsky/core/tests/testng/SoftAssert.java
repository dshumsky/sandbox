package dshumsky.core.tests.testng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

import com.google.common.collect.Maps;

/**
 * When an assertion fails, don't throw an exception but record the failure.
 * Calling {@code assertAll()} will cause an exception to be thrown if at least one assertion failed.
 *
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class SoftAssert extends Assertion {

    private Map<AssertionError, IAssert> m_errors = Maps.newLinkedHashMap(); // LinkedHashMap to preserve the order

    @Override
    public void executeAssert(IAssert a) {
        try {
            a.doAssert();
        } catch (AssertionError ex) {
            m_errors.put(ex, a);
        }
    }

    /**
     * Will cause an exception to be thrown if at least one assertion failed.
     */
    public void assertAll() {
        if (!m_errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following asserts failed:\n");
            for (Map.Entry<AssertionError, IAssert> ae : m_errors.entrySet()) {
                IAssert anAssert = ae.getValue();
                sb.append(String.format("Actual=%s, Expected=%s, Message=%s\n", anAssert.getActual(), anAssert.getExpected(), anAssert.getMessage()));
                String stackTrace = ExceptionUtils.getFullStackTrace(ae.getKey());
                sb.append(stackTrace);
            }
            throw new AssertionError(sb.toString());
        }
    }

    /**
     * @return messages for all failures delimited by '\n'
     */
    public String getMessagesAsString() {
        StringBuilder sb = new StringBuilder();
        for (String message : getMessages()) {
            sb.append(message).append("\n");
        }
        return sb.toString();
    }

    /**
     * @return messages for all failures
     */
    public List<String> getMessages() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<AssertionError, IAssert> ae : m_errors.entrySet()) {
            list.add(ae.getValue().getMessage());
        }
        return list;
    }

    /**
     * Adds all failures from another {@link SoftAssert}
     *
     * @param softAssert another {@link SoftAssert}
     */
    public void attachAll(SoftAssert softAssert) {
        m_errors.putAll(softAssert.m_errors);
    }

    /**
     * @return true if there is at least one failure
     */
    public boolean hasErrors() {
        return !m_errors.isEmpty();
    }
}
