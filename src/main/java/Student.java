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

    @ForeignKey(fKTableName = "test", foreignClass = Main.class)
    @Field(fieldName = "testid")
    public Main test;
}
