package showUses;

import OrmFramework.Orm;
import classes.Student;
import classes.Test;

public class WithFK {
    public static void show() {
        System.out.println("(4) Create and save object with foreign key");
        System.out.println("-------------------------------");
        Student s = new Student();
        s.id = 1;
        s.name = "Jakob";
        s.inscribed = true;

        try {
            s.test = Orm.getObject(Test.class, 1, true);
            System.out.println("Getting: Test t with name: " + s.test.test + " and id: " + s.test.id);
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }
        try {
            Orm.save(s);
            System.out.println("Saved: Test t with name: " + s.name + ", id: " + s.id + " and inscribed: " + s.inscribed + " with test: " + s.test.test +  " and id: " + s.test.id);
        } catch (Exception e) {
            System.out.println("Failed to save object.");
        }
        System.out.println("\n");
    }
}
