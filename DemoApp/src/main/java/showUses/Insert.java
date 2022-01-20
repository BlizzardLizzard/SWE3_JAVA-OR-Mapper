package showUses;

import OrFramework.Orm;
import classes.Test;

public class Insert {
    public static void show(){
        System.out.println("(2) Demonstrate inserting objects into tables");
        System.out.println("-------------------------------");

        Test t = new Test();
        t.id = 1;
        t.test = "VTSE";
        try {
            Orm.save(t);
        } catch (Exception e) {
            System.out.println("Failed to save object.");
        }
        System.out.println("\n");
    }
}
