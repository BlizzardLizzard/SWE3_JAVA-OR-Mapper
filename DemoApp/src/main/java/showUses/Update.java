package showUses;

import OrmFramework.Orm;
import classes.Test;

public class Update {
    public static void show(){
        System.out.println("(3) Get and modify object");
        System.out.println("-------------------------------");
        try {
            Test t = Orm.getObject(Test.class, 1, true);
            System.out.println("Getting: Test t with name: " + t.test + " and id: " + t.id);
            t.test = "INN";
            System.out.println("Changing Test t test to: " + t.test);
            try {
                Orm.update(t);
            } catch (Exception u) {
                System.out.println("Failed to update object");
            }
        } catch (Exception e) {
            System.out.println("Failed to get object.");
        }
        System.out.println("\n");
    }
}
