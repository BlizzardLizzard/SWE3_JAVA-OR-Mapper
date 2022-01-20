import OrFramework.Orm;
import showUses.CreateTables;
import showUses.Insert;

public class Program {
    public static void main(String[] args) {
        Orm.connect("jdbc:postgresql://localhost/Orm", "postgres", "passwort");
        //CreateTables.show();
        //Insert.show();
    }
}
