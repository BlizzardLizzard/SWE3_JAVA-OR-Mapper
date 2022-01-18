import annotations.Field;
import annotations.OneToMany;
import annotations.PrimaryKey;
import annotations.Table;

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
        Main m = (Main) Orm.getObject(Main.class, "1", true);
        for(Student s : m.students){
            if(Objects.equals(s.name, "Jakob")){
                s.name = "Jackos";
            }
        }
        Orm.update(m);
    }
}
