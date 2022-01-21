package showUses;

import OrmFramework.Orm;
import classes.Student;
import classes.Test;

import java.sql.Date;

public class Insert {
    public static void show(){
        System.out.println("(2) Demonstrate inserting objects into tables");
        System.out.println("-------------------------------");

        Test t = new Test();
        t.id = 1;
        t.test = "VTSE";
        t.creationDate = Date.valueOf("2021-12-24");
        try {
            Orm.save(t);
            System.out.println("Saved: Test t with name: " + t.test + " and id: " + t.id);
        } catch (Exception e) {
            System.out.println("Failed to save object.");
        }
        System.out.println("\n");
    }
}
