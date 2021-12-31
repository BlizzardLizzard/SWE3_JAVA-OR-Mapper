import annotations.Table;
import lombok.Getter;
import lombok.Setter;
import metamodel.Test;
import metamodel.__Table;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class Orm {
    private static final HashMap<Class, __Table> _tables = new HashMap<>();

    /** Get and Set Connection */
    @Getter
    @Setter
    private static Connection _connection;

    /** Connects to the wanted database
     * @param connectionString String needed to access database
     * @throws SQLException Gets thrown when a connection failed */
    public static void connect (String connectionString) throws SQLException {
        _connection = DriverManager.getConnection(connectionString, "postgres", "passwort");
        //connect("jdbc:postgresql://localhost/Orm");
    }

    public static void main(String[] args) throws SQLException {
        connect("jdbc:postgresql://localhost/Orm");
        save(new Test());
    }

    public static void save(Object obj) throws SQLException {
        __Table table = new __Table(Test.class);
        table.set_tableName(obj.getClass().getSimpleName());
        ArrayList<Object> fieldNames = new ArrayList<>();
        ArrayList<Object> fields = new ArrayList<>();

        for(Field name : obj.getClass().getDeclaredFields()){
            if(name.isAnnotationPresent(annotations.Field.class)){
                name.setAccessible(true);
                fieldNames.add(name.getName());
                try {
                    // get value of field
                    Object value = name.get(obj);
                    fields.add(value);
                    System.out.println(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //print name of field
                System.out.println(name.getName());
            }
        }
        System.out.println(fieldNames);
        System.out.println(fields);

        //TODO: Get names of table rows and put them into the String
       StringBuilder insertQuery = new StringBuilder("INSERT INTO " + table.get_tableName() + " (");

       boolean firstRow = true;
        for(Object fieldName : fieldNames){
            if(firstRow) {
                firstRow = false;
            } else {
                insertQuery.append(", ");
            }
            insertQuery.append(fieldName.toString());
        }
        insertQuery.append(") VALUES (");

        boolean firstItem = true;
        for(Object field : fields){
            if(firstItem) {
                firstItem = false;
            } else {
                insertQuery.append(", ");
            }
            insertQuery.append("?");
        }
        insertQuery.append(")");
        PreparedStatement ps = get_connection().prepareStatement(insertQuery.toString());

        int index = 1;
        for(Object items : fields){
            ps.setString(index++, items.toString());
        }
        System.out.println(insertQuery);
        ps.execute();
        ps.close();
    }

    public static void __getAllColumns(String tableName) throws SQLException {
        String tableQuery = "SELECT * FROM " + tableName;
        ResultSet result = get_connection().prepareStatement(tableQuery).executeQuery();
        while(result.next()){
            System.out.println(result.getString(2));
        }
    }
}
