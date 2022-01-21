package classes;

import OrmFramework.annotations.Field;
import OrmFramework.annotations.ForeignKey;
import OrmFramework.annotations.PrimaryKey;
import OrmFramework.annotations.Table;

/**
 * This class represents a student
 */
@Table(tableName = "student")
public class Student {

    /**
     * Primary key id
     */
    @PrimaryKey
    @Field(fieldName = "id", notNull = true, unique = true)
    public Integer id;

    /**
     * Field name
     */
    @Field(fieldName = "name")
    public String name;

    /**
     * Foreign key Test for Student
     */
    @ForeignKey(fKTableName = "test", foreignClass = Test.class)
    @Field(fieldName = "testid")
    public Test test;
}
