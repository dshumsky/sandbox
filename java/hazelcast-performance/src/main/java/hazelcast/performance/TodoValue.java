package hazelcast.performance;

import java.io.IOException;
import java.time.LocalDate;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import lombok.Getter;
import lombok.Setter;
import hazelcast.performance.MyDataSerializableFactory.Types;

@Getter
@Setter
public class TodoValue implements IdentifiedDataSerializable {
    LocalDate advDateStart;
    long advRegionId;

    @Override
    public int getFactoryId() {
        return MyDataSerializableFactory.ID;
    }

    @Override
    public int getId() {
        return Types.TodoValue;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        // out.writeObject(advDateStart);
        out.writeLong(advRegionId);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        // advDateStart = in.readObject();
        advRegionId = in.readLong();
    }
}
