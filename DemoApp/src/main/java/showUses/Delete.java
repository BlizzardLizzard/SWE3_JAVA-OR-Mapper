package showUses;

import OrmFramework.Orm;
import classes.Course;
import classes.Student;
import classes.Test;

import java.sql.Date;

public class Delete {
    public static void show(){
        System.out.println("(9) Demonstrate deleting certain objects");
        System.out.println("-------------------------------");

        try {
            Student s = Orm.getObject(Student.class, 1, false);
            System.out.println("Getting: Student s with name: " + s.name + " and id: " + s.id);
            System.out.println("Deleting student " + s.name);
            try {
                Orm.delete(s, "id");
            } catch (Exception e){
                System.out.println("Failed to delete item");
            }
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }

        try {
            Test t = Orm.getObject(Test.class, 1, true);
            System.out.println("Getting: Test t with name: " + t.test + " and id: " + t.id);
            System.out.println("Deleting test " + t.test + " and dereference foreign key items in the database");
            try {
                 Orm.delete(t, "id");
            } catch (Exception e){
                System.out.println("Failed to delete item");
            }
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }

        try {
            Course c = Orm.getObject(Course.class, 1, true);
            System.out.println("Getting: Test t with name: " + c.courseName + " and id: " + c.id);
            System.out.println("Deleting test " + c.courseName + " and deleting junction table entry");
            try {
                Orm.delete(c, "id");
            } catch (Exception e){
                System.out.println("Failed to delete item");
            }
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }
        System.out.println("\n");
    }
}
