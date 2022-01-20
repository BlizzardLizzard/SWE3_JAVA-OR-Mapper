package classes;

import OrFramework.annotations.Field;
import OrFramework.annotations.ForeignKey;
import OrFramework.annotations.PrimaryKey;
import OrFramework.annotations.Table;

@Table(tableName = "student")
public class Student {
    @PrimaryKey
    @Field(fieldName = "id", notNull = true, unique = true)
    public Integer id;

    @Field(fieldName = "name")
    public String name;

    @ForeignKey(fKTableName = "test", foreignClass = Test.class)
    @Field(fieldName = "testid")
    public Test test;
}
