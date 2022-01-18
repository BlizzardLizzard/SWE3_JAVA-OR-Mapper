import annotations.Field;
import annotations.OneToMany;
import annotations.PrimaryKey;
import annotations.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

@Table(tableName = "test")
public class Main {
    @PrimaryKey
    @Field(fieldName = "id")
    public Integer id;

    @Field(fieldName = "test")
    public String test;

    @OneToMany(tableName = "student", foreignKeyName = "testid", classObject = Student.class)
    public ArrayList<Student> students;

    public static void main(String[] args){
        Student s = (Student) Orm.getObject(Student.class, "7");
        s.test = (Main) Orm.getObject(Main.class, "20");
        Orm.update(s);
    }
}
