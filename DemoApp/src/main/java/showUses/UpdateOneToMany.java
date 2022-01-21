package showUses;

import OrmFramework.Orm;
import classes.Student;
import classes.Test;

public class UpdateOneToMany {
    public static void show(){
        System.out.println("(6) Get object and modify 1:m list");
        System.out.println("-------------------------------");
        try {
            Test t = Orm.getObject(Test.class, 1, true);
            System.out.println("Getting: Test t with name: " + t.test + " and id: " + t.id);
            Student s = new Student();
            s.id = 3;
            s.name = "Jacky";
            try {
                s.test = Orm.getObject(Test.class, 1, true);
                System.out.println("Getting: Test t with name: " + s.test.test + " and id: " + s.test.id);
                System.out.println("Adding Student s with name: " + s.name + " and id: " + s.id + " to Test t with id: " + s.test.id);
                t.students.add(s);
            } catch (Exception e) {
                System.out.println("Failed to get object.");
            }
            try {
                Orm.update(t);
            } catch (Exception u) {
                System.out.println("Failed to update object");
            }
            try {
                Test test = Orm.getObject(Test.class, 1, true);
                System.out.println("Getting: Test t with name: " + test.test + " and id: " + test.id + " with students: ");
                for(Student student : t.students){
                    System.out.println(student.name + " id: " + student.id);
                }
            } catch (Exception e) {
                System.out.println("Failed to get object.");
            }
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }
        System.out.println("\n");
    }
}
