package showUses;

import OrmFramework.Orm;
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
            System.out.println("Creating simple table for Test.class.");
        } catch (Exception e) {
            System.out.println("Failed to save simple table.");
        }
        try {
            Orm.createTableFK(Student.class, Test.class);
            System.out.println("Creating table with foreign key Test.class for Student.class.");
        } catch (Exception e) {
            System.out.println("Failed to save table with foreign key.");
        }
        try {
            Orm.createTable(Course.class);
            System.out.println("Creating simple table for Course.class.");
            Orm.createTable(Teacher.class);
            System.out.println("Creating simple table for Teacher.class.");
            Orm.linkManyToManyTables(Teacher.class, Course.class);
            System.out.println("Creating junction table for Course.class and Teacher.class");
        } catch (Exception e) {
            System.out.println("Failed to save table with many to many.");
        }
        System.out.println("\n");
    }
}
