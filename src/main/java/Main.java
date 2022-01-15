import annotations.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Table(tableName = "test")
public class Main {
    @PrimaryKey
    @Field(fieldName = "id")
    public Integer id;

    @Field(fieldName = "test")
    public String test;

    @OneToMany(tableName = "student", foreignKeyName = "testid", classObject = Student.class)
    public ArrayList<Student> students;

    public static void main(String[] args) throws SQLException {
        //TODO: Main class in student needs to be set
        Student student = (Student) Orm.getObject(Student.class, "4", true);
    }
}
