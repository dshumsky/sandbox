package hazelcast.performance;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class MyDataSerializableFactory implements DataSerializableFactory {

    public static final int ID = 1;

    //@formatter:off
    interface Types {
        int TodoKey       = 1;
        int TodoValue     = 2;
        int ItemSaleKey   = 3;
        int ItemSaleValue = 4;
    }

    @Override
    public IdentifiedDataSerializable create(int typeId) {

        switch (typeId) {
            case Types.TodoKey       : return new TodoKey();
            case Types.TodoValue     : return new TodoValue();
            case Types.ItemSaleKey   : return new ItemSaleKey();
            case Types.ItemSaleValue : return new ItemSaleValue();
            default:
                throw new IllegalArgumentException("typeId="+typeId);
        }
    }
    //@formatter:on
}
