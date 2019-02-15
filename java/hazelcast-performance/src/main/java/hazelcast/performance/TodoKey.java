package hazelcast.performance;

import java.io.IOException;
import java.util.Objects;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import hazelcast.performance.MyDataSerializableFactory.Types;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoKey implements IdentifiedDataSerializable {

    interface Fields {
        String countryId = "countryId";
        String whId = "whId";
    }

    long countryId;
    long advDateId;
    long storeid;
    long itemid;
    long whId;

    @Override
    public int getFactoryId() {
        return MyDataSerializableFactory.ID;
    }

    @Override
    public int getId() {
        return Types.TodoKey;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(countryId);
        out.writeLong(advDateId);
        out.writeLong(itemid);
        out.writeLong(storeid);
        out.writeLong(whId);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        countryId = in.readLong();
        advDateId = in.readLong();
        itemid = in.readLong();
        storeid = in.readLong();
        whId = in.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TodoKey todoKey = (TodoKey) o;
        return countryId == todoKey.countryId &&
                advDateId == todoKey.advDateId &&
                storeid == todoKey.storeid &&
                itemid == todoKey.itemid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId, advDateId, storeid, itemid);
    }
}
