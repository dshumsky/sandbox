package hazelcast.performance;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import hazelcast.performance.MyDataSerializableFactory.Types;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSaleValue implements IdentifiedDataSerializable {
    private long sumQtyPiece;
    private long sumTurnover;

    @Override
    public int getFactoryId() {
        return MyDataSerializableFactory.ID;
    }

    @Override
    public int getId() {
        return Types.ItemSaleValue;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(sumQtyPiece);
        out.writeLong(sumTurnover);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        sumQtyPiece = in.readLong();
        sumTurnover = in.readLong();
    }
}
