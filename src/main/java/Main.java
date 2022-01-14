import annotations.*;

import java.sql.SQLException;
import java.util.HashSet;

@Table(tableName = "test")
public class Main {
    @PrimaryKey
    @Field(fieldName = "id")
    public Integer id;

    @Field(fieldName = "test")
    public String test;

    @OneToMany(tableName = "student")
    public Student[] students;

    public static void main(String[] args) throws SQLException {
        Main m = (Main) Orm.getObject(Main.class, "1");
        Student student = new Student();
        student.name = "jakob";
        student.test = m;
        Orm.save(student);
    }
}
