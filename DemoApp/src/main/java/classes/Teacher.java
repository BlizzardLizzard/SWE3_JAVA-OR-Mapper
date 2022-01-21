package classes;

import OrmFramework.annotations.Field;
import OrmFramework.annotations.ManyToMany;
import OrmFramework.annotations.PrimaryKey;
import OrmFramework.annotations.Table;

import java.util.ArrayList;

@Table(tableName = "teachers")
public class Teacher {
        @PrimaryKey
        @Field(fieldName = "id")
        public Integer id;

        @Field(fieldName = "name")
        public String teacherName;

        @ManyToMany(tableName = "courses", foreignKeyNameOwn = "teacherid", foreignKeyNameOther = "courseid", classObject = Course.class, manyToManyTableName = "teacher_course")
        public ArrayList<Course> courses;
}
