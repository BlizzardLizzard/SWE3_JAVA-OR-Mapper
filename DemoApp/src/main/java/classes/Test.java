package classes;

import OrmFramework.annotations.Field;
import OrmFramework.annotations.OneToMany;
import OrmFramework.annotations.PrimaryKey;
import OrmFramework.annotations.Table;

import java.util.ArrayList;

@Table(tableName = "test")
public class Test {
    @PrimaryKey
    @Field(fieldName = "id", notNull = true, unique = true)
    public Integer id;

    @Field(fieldName = "test")
    public String test;

    @OneToMany(tableName = "student", foreignKeyName = "testid", classObject = Student.class)
    public ArrayList<Student> students;

}
