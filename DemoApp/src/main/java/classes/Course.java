package classes;

import OrFramework.annotations.Field;
import OrFramework.annotations.ManyToMany;
import OrFramework.annotations.PrimaryKey;
import OrFramework.annotations.Table;

import java.util.ArrayList;

@Table(tableName = "courses")
public class Course {
    @PrimaryKey
    @Field(fieldName = "id")
    public Integer id;

    @Field(fieldName = "name")
    public String courseName;

    @ManyToMany(tableName = "teachers", foreignKeyNameOwn = "courseid", foreignKeyNameOther = "teacherid",classObject = Teacher.class, manyToManyTableName = "teacher_course")
    public ArrayList<Teacher> teachers;
}
