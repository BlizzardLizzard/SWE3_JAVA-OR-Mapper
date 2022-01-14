import annotations.Field;
import annotations.ForeignKey;
import annotations.PrimaryKey;
import annotations.Table;

@Table(tableName = "student")
public class Student {
    @PrimaryKey
    @Field(fieldName = "id")
    public Integer id;

    @Field(fieldName = "name")
    public String name;

    @Field(fieldName = "testid")
    @ForeignKey(fKTableName = "test", foreignClass = Main.class)
    public Main test;
}
