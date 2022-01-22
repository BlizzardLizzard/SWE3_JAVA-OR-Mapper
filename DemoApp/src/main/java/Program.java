import OrmFramework.Orm;
import classes.Student;
import classes.Test;
import showUses.*;

import java.sql.SQLException;

/**
 * Sample app
 */
public class Program {

    /**
     * Program start for sample app
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        //Please set your database info in the db.properties
        Orm.connect();
        CreateTables.show();
        Insert.show();
        Update.show();
        WithFK.show();
        WithOneToMany.show();
        UpdateOneToMany.show();
        WithManyToMany.show();

        try {
            Orm.get_connection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
