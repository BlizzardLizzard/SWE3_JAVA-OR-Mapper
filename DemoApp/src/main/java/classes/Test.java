package classes;

import OrmFramework.annotations.Field;
import OrmFramework.annotations.OneToMany;
import OrmFramework.annotations.PrimaryKey;
import OrmFramework.annotations.Table;

import java.sql.Date;
import java.util.ArrayList;

/**
 * This class represents a test for students
 */
@Table(tableName = "test")
public class Test {

    /**
     * Primary key id
     */
    @PrimaryKey
    @Field(fieldName = "id", notNull = true, unique = true)
    public Integer id;

    /**
     * Field test
     */
    @Field(fieldName = "test")
    public String test;

    /**
     * Field creationDate
     */
    @Field(fieldName = "creationtime")
    public Date creationDate;

    /**
     * One-To-Many Students for Test
     */
    @OneToMany(tableName = "student", foreignKeyName = "testid", classObject = Student.class)
    public ArrayList<Student> students;

}
