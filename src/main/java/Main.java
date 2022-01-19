import annotations.Field;
import annotations.OneToMany;
import annotations.PrimaryKey;
import annotations.Table;
import metamodel.__TableObject;

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
        //TODO: Update every sql execution to prepared statements
        Teacher t = (Teacher) Orm.getObject(Teacher.class, "1", true);
        Orm.update(t);
    }
}
