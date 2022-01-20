import annotations.Field;
import annotations.ManyToMany;
import annotations.PrimaryKey;
import annotations.Table;

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
