package showUses;

import OrmFramework.Orm;
import classes.Course;
import classes.Teacher;
import classes.Test;

public class WithManyToMany {
    public static void show(){
        //TODO: Maybe initialise arraylists always
        System.out.println("(7) Create m:n objects and join them");
        System.out.println("-------------------------------");

        Teacher t = new Teacher();
        t.id = 1;
        t.teacherName = "Dr. Acula";
        try {
            Orm.save(t);
            System.out.println("Saved: Teacher t with name: " + t.teacherName + " and id: " + t.id);
        } catch (Exception e) {
            System.out.println("Failed to save object.");
        }
        Course c = new Course();
        c.id = 1;
        c.courseName = "SWE3";
        try {
            Orm.save(c);
            System.out.println("Saved: Test t with name: " + c.courseName + " and id: " + c.id);
        } catch (Exception e) {
            System.out.println("Failed to save object.");
        }
        Teacher teacher = Orm.getObject(Teacher.class, 1, true);
        System.out.println("Adding Course c with name: " + c.courseName + " and id: " + c.id + " to Teacher t with id: " + t.id);
        try {
            teacher.courses.add(Orm.getObject(Course.class, 1, true));
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }

        try {
            Orm.update(teacher);
            System.out.println("Relation Teacher with Course successful: ");
            for(Course course : teacher.courses){
                System.out.println("Id: " + course.id + " and name: " + course.courseName);
            }
        } catch (Exception u) {
            System.out.println("Failed to update object");
        }
        try {
            Course course = Orm.getObject(Course.class, 1, true);
            System.out.println("Relation Course with Teacher successful: ");
            for(Teacher teacherC : course.teachers){
                System.out.println("Id: " + teacherC.id + " and name: " + teacherC.teacherName);
            }
        } catch (Exception u) {
            System.out.println("Failed to update object");
        }
        System.out.println("\n");
    }
}
