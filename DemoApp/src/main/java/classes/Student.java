package classes;

import OrmFramework.annotations.Field;
import OrmFramework.annotations.ForeignKey;
import OrmFramework.annotations.PrimaryKey;
import OrmFramework.annotations.Table;

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
