package showUses;

import OrmFramework.Orm;
import classes.Student;
import classes.Test;

public class WithOneToMany {
    public static void show() {
        System.out.println("(5) Get object and show 1:m objects");
        System.out.println("-------------------------------");
        Student s = new Student();
        s.id = 2;
        s.name = "Alec";
        s.inscribed = true;
        try {
            s.test = Orm.getObject(Test.class, 1, true);
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }
        try {
            Orm.save(s);
            System.out.println("Saved: Test t with name: " + s.name + ", id: " + s.id + " and inscribed" + s.inscribed + " with test: " + s.test.test + " and id: " + s.test.id);
        } catch (Exception e) {
            System.out.println("Failed to save object.");
        }
        try {
            Test t = Orm.getObject(Test.class, 1, true);
            System.out.println("Getting: Test t with name: " + t.test + " and id: " + t.id + " with students: ");
            for(Student student : t.students){
                System.out.println(student.name + " id: " + student.id + " and inscribed: " + s.inscribed);
            }
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }
        System.out.println("\n");
    }
}
