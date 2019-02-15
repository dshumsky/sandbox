package dshumsky.core.tests.jbehave;

import java.util.AbstractList;

import org.jbehave.core.model.ExamplesTable;

import com.google.common.base.Preconditions;

/**
 * @author Dmitry Shumski (dshumsky@gmail.com)
 */
public class JBehaveMultiLineText extends AbstractList<String> {
    private final ExamplesTable table;
    private final String header;

    public JBehaveMultiLineText(ExamplesTable table) {
        Preconditions.checkState(table.getHeaders().size()==1);

        this.header = table.getHeaders().get(0);
        this.table = table;
    }

    @Override
    public int size() {
        return table.getRowCount();
    }

    @Override
    public String get(int index) {
        return table.getRow(index).get(header);
    }

}
