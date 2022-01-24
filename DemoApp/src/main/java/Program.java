import OrmFramework.Orm;
import OrmFramework.queryEnums.QueryComparison;
import classes.Student;
import showUses.*;

import java.sql.SQLException;
import java.util.ArrayList;

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
        WithQuery.show();

        try {
            Orm.get_connection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
