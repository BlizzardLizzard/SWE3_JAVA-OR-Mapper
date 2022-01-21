import OrmFramework.Orm;
import showUses.*;

public class Program {
    public static void main(String[] args) {
        //TODO: put the info from connect into a properties file
        Orm.connect("jdbc:postgresql://localhost/Orm", "postgres", "passwort");
        CreateTables.show();
        Insert.show();
        Update.show();
        WithFK.show();
        WithOneToMany.show();
        UpdateOneToMany.show();
        WithManyToMany.show();
    }
}
