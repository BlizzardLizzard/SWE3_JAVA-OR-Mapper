import annotations.Field;
import annotations.OneToMany;
import annotations.PrimaryKey;
import annotations.Table;

import java.util.ArrayList;

@Table(tableName = "test")
public class Main {
    @PrimaryKey
    @Field(fieldName = "id", notNull = true, unique = true)
    public Integer id;

    @Field(fieldName = "test")
    public String test;

    @OneToMany(tableName = "student", foreignKeyName = "testid", classObject = Student.class)
    public ArrayList<Student> students;

    public static void main(String[] args){
        //TODO: DON'T DELETE
        Orm.connect("jdbc:postgresql://localhost/Orm", "postgres", "passwort");
        Orm.createTableFK(Student.class, Main.class);
    }
}
