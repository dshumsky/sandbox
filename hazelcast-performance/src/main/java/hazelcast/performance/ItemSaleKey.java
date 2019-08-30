package hazelcast.performance;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import hazelcast.performance.MyDataSerializableFactory.Types;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSaleKey implements IdentifiedDataSerializable {
    private long itemId;
    private long storeId;
    private long whId;
    private LocalDate receiptDate;

    @Override
    public int getFactoryId() {
        return MyDataSerializableFactory.ID;
    }

    @Override
    public int getId() {
        return Types.ItemSaleKey;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(itemId);
        out.writeLong(storeId);
        out.writeLong(whId);
        out.writeObject(receiptDate);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        itemId = in.readLong();
        storeId = in.readLong();
        whId = in.readLong();
        receiptDate = in.readObject();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemSaleKey that = (ItemSaleKey) o;
        return itemId == that.itemId &&
                storeId == that.storeId &&
                whId == that.whId &&
                Objects.equals(receiptDate, that.receiptDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, storeId, whId, receiptDate);
    }
}
