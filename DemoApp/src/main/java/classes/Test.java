package classes;

import OrFramework.Orm;
import OrFramework.annotations.Field;
import OrFramework.annotations.OneToMany;
import OrFramework.annotations.PrimaryKey;
import OrFramework.annotations.Table;

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
