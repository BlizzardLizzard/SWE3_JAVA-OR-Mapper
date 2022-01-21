package classes;

import OrmFramework.annotations.*;

import java.util.ArrayList;

/**
 * This class represents the teachers for courses
 */
@Table(tableName = "teachers")
public class Teacher {

        /**
         * Primary key id
         */
        @PrimaryKey
        @Field(fieldName = "id", notNull = true, unique = true)
        public Integer id;

        /**
         * Field teacher name
         */
        @Field(fieldName = "name")
        public String teacherName;

        /**
         * Many-To-Many Courses for Teacher
         */
        @ManyToMany(tableName = "courses", foreignKeyNameOwn = "teacherid", foreignKeyNameOther = "courseid", classObject = Course.class, manyToManyTableName = "teacher_course")
        public ArrayList<Course> courses;

        /**
         * Foreign key Test for Teacher
         */
        @Field(fieldName = "testid", notNull = true)
        @ForeignKey(fKTableName = "test", foreignClass = Test.class)
        public Test test;
}
