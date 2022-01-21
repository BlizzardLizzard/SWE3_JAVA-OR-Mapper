package classes;

import OrmFramework.annotations.Field;
import OrmFramework.annotations.ManyToMany;
import OrmFramework.annotations.PrimaryKey;
import OrmFramework.annotations.Table;

import java.util.ArrayList;

/**
 * This class represents a course for teachers
 */
@Table(tableName = "courses")
public class Course {

    /**
     * Primary key id
     */
    @PrimaryKey
    @Field(fieldName = "id")
    public Integer id;

    /**
     * Field course name
     */
    @Field(fieldName = "name")
    public String courseName;

    /**
     * Many-To-Many Teachers for Course
     */
    @ManyToMany(tableName = "teachers", foreignKeyNameOwn = "courseid", foreignKeyNameOther = "teacherid",classObject = Teacher.class, manyToManyTableName = "teacher_course")
    public ArrayList<Teacher> teachers;
}
