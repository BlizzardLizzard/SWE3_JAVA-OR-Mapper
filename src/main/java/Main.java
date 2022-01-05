import annotations.Field;
import annotations.PrimaryKey;
import annotations.Table;

import java.sql.SQLException;

@Table(tableName = "test")
public class Main {
    @PrimaryKey
    @Field(fieldName = "id")
    public Integer id;

    @Field(fieldName = "test")
    public String test;

    public static void main(String[] args) throws SQLException {
        Main m = (Main) Orm.getObject(Main.class, "13");
        System.out.println(m.test);
        m.test = "test";
        Orm.update(m);
    }
}
