import annotations.Field;
import annotations.Table;

import java.sql.SQLException;

@Table
public class Test {
    @Field(fieldName = "id")
    public Integer id;

    @Field(fieldName = "test")
    public String test;

    public void test(){}

    public static void main(String[] args) throws SQLException {
        Test t = (Test) Orm.getObject(Test.class, "6");
    }
}
