package showUses;

import OrmFramework.Orm;
import OrmFramework.queryEnums.QueryComparison;
import classes.Course;
import classes.Student;
import classes.Teacher;
import classes.Test;

import java.util.ArrayList;

public class WithQuery {
    public static void show(){
        System.out.println("(8) Demonstrate custom query");
        System.out.println("-------------------------------");

        try{
            System.out.println("Get all students");
            ArrayList<Student> student = Orm.query().SELECT().FROM(Student.class).executeQuery();
            for(Student s : student){
                System.out.println("Student: " + s.name + " with id: " + s.id + " is inscribed: " + s.inscribed + " taking test: " + s.test.test);
            }
        } catch (Exception e){
            System.out.println("SQL invalid");
        }

        System.out.println("\n");
        try{
            System.out.println("Get students with id = 1");
            ArrayList<Student> student = Orm.query().SELECT().FROM(Student.class)
                    .WHERE("id").compare(QueryComparison.EQUAL, 1)
                    .executeQuery();
            for(Student s : student){
                System.out.println("Student: " + s.name + " with id: " + s.id + " is inscribed: " + s.inscribed + " taking test: " + s.test.test);
            }
        } catch (Exception e){
            System.out.println("SQL invalid");
        }

        System.out.println("\n");
        try{
            System.out.println("Get students with id = 1 and starts with a and id between 0 and 1");
            ArrayList<Student> student = Orm.query().SELECT().FROM(Student.class)
                    .WHERE("name").LIKE("J%")
                    .AND().WHERE("id").BETWEEN(0, 1)
                    .AND().WHERE("inscribed").compare(QueryComparison.EQUAL, true)
                    .executeQuery();
            for(Student s : student){
                System.out.println("Student: " + s.name + " with id: " + s.id + " is inscribed: " + s.inscribed + " taking test: " + s.test.test);
            }
        } catch (Exception e){
            System.out.println("SQL invalid");
        }

        System.out.println("\n");
        try{
            System.out.println("Get tests and display all students from test");
            ArrayList<Test> test = Orm.query().SELECT().FROM(Test.class)
                    .WHERE("id").compare(QueryComparison.EQUAL, 1)
                    .executeQuery();
            for(Test t : test){
                System.out.println("Test: " + t.test + " with id: " + t.id + " with creation date: " + t.creationDate + " and Students: ");
                for(Student s : t.students){
                    System.out.println("Student: " + s.name + " with id: " + s.id + " is inscribed: " + s.inscribed + " taking test: " + s.test.test);
                }
            }
        } catch (Exception e){
            System.out.println("SQL invalid");
        }

        System.out.println("\n");
        try{
            System.out.println("Get teachers for courses");
            ArrayList<Course> course = Orm.query().SELECT().FROM(Course.class)
                    .WHERE("id").compare(QueryComparison.EQUAL, 1)
                    .executeQuery();
            for(Course c : course){
                System.out.println("Course: " + c.courseName + " with id: " + c.id + " and Teachers: ");
                for(Teacher t : c.teachers){
                    System.out.println("Teacher: " + t.teacherName + " with id: " + t.id);
                }
            }
        } catch (Exception e){
            System.out.println("SQL invalid");
        }
    }
}
