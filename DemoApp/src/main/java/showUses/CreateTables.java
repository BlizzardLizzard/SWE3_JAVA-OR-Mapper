package showUses;

import OrFramework.Orm;
import classes.Course;
import classes.Student;
import classes.Teacher;
import classes.Test;

public class CreateTables {
    public static void show(){
        System.out.println("(1) Demonstrate creating tables");
        System.out.println("-------------------------------");
        try {
            Orm.createTable(Test.class);
        } catch (Exception e) {
            System.out.println("Failed to save simple table.");
        }
        try {
            Orm.createTableFK(Student.class, Test.class);
        } catch (Exception e) {
            System.out.println("Failed to save table with foreign key.");
        }
        try {
            Orm.createTable(Course.class);
            Orm.createTable(Teacher.class);
            Orm.linkManyToManyTables(Teacher.class, Course.class);
        } catch (Exception e) {
            System.out.println("Failed to save table with many to many.");
        }
        System.out.println("\n");
    }
}
