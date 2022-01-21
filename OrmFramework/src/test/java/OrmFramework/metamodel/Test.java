package OrmFramework.metamodel;

import OrmFramework.annotations.*;

import java.util.ArrayList;

@Table(tableName = "test")
public class Test {
    @PrimaryKey
    @Field(fieldName = "id", notNull = true, unique = true)
    public Integer id;

    @Field(fieldName = "name")
    public String courseName;

    @ForeignKey(fKTableName = "test", foreignClass = Test.class)
    @Field(fieldName = "testid")
    public Test test;

    @ManyToMany(tableName = "teachers", foreignKeyNameOwn = "courseid", foreignKeyNameOther = "teacherid", classObject = Test.class, manyToManyTableName = "teacher_course")
    public ArrayList<Test> teachers;

    @OneToMany(tableName = "student", foreignKeyName = "testid", classObject = Test.class)
    public ArrayList<Test> students;
}
