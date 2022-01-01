import annotations.Table;
import lombok.Getter;
import lombok.Setter;
import metamodel.__Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

public final class Orm {
    private static final HashMap<Class, __Table> _tables = new HashMap<>();

    /** Get and Set Connection */
    @Getter
    @Setter
    private static Connection _connection;

    /** Connects to the wanted database
     * @throws SQLException Gets thrown when a connection failed */
    public static void connect () throws SQLException {
        _connection = DriverManager.getConnection("jdbc:postgresql://localhost/Orm", "postgres", "passwort");
    }

    public static void save(Object obj) throws SQLException {
        connect();
        __Table table = new __Table(Test.class);
        if(obj.getClass().isAnnotationPresent(annotations.Table.class) && !Objects.equals(obj.getClass().getAnnotation(Table.class).tableName(), "")){
            table.set_tableName(obj.getClass().getAnnotation(annotations.Table.class).tableName());
        } else {
            table.set_tableName(obj.getClass().getSimpleName());
        }
        ArrayList<Object> fieldNames = new ArrayList<>();
        ArrayList<Object> fields = new ArrayList<>();

        for(Field name : obj.getClass().getDeclaredFields()){
            if(name.isAnnotationPresent(annotations.Field.class) && !name.isAnnotationPresent(annotations.PrimaryKey.class)){
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
        System.out.println(insertQuery);

        int index = 1;
        for(Object items : fields){
            ps.setString(index++, items.toString());
        }
        ps.execute();
        ps.close();
    }

    public static void update(Object obj) throws SQLException {
        connect();
        StringBuilder updateQuery = new StringBuilder("UPDATE " + obj.getClass().getAnnotation(annotations.Table.class).tableName() + " SET ");
        HashMap<Object, Object> pairs = new HashMap<>();
        HashMap<Object, Object> primaryKeys = new HashMap<>();
        Object fieldName = "";

        for(Field name : obj.getClass().getDeclaredFields()){
            if(name.isAnnotationPresent(annotations.Field.class)) {
                name.setAccessible(true);
                fieldName = name.getName();
                if (!name.isAnnotationPresent(annotations.PrimaryKey.class)) {
                    try {
                        // get value of field
                        pairs.put(fieldName, name.get(obj));
                        System.out.println(pairs);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    //print name of field
                    System.out.println(name.getName());
                } else {
                    try {
                        primaryKeys.put(fieldName, name.get(obj));
                        System.out.println(primaryKeys);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        boolean firstItem = true;
        for(Object fieldKey : pairs.keySet()){
            if(firstItem) {
                firstItem = false;
            } else {
                updateQuery.append(", ");
            }
            updateQuery.append(fieldKey).append(" = ").append("'" + pairs.get(fieldKey) + "'");
        }

        updateQuery.append(" WHERE ");

        boolean firstPrimaryKey = true;
        for(Object fieldKey : primaryKeys.keySet()){
            if(firstPrimaryKey) {
                firstPrimaryKey = false;
            } else {
                updateQuery.append(", ");
            }
            updateQuery.append(fieldKey).append(" = ").append(primaryKeys.get(fieldKey));
        }
        System.out.println(updateQuery);
        PreparedStatement ps = get_connection().prepareStatement(updateQuery.toString());
        ps.execute();
        ps.close();
    }

    public static Object getObject(Class cls, String primaryKey) throws SQLException {
        String getObjectQuery = "SELECT * FROM " + ((Table) cls.getAnnotation(annotations.Table.class)).tableName() + " WHERE id = " + primaryKey;
        connect();
        ResultSet result = get_connection().prepareStatement(getObjectQuery).executeQuery();
        try {
            try {
                Object obj = cls.getConstructor().newInstance();
                result.next();
                for(Field field : cls.getDeclaredFields()) {
                    annotations.Field fieldAnnotation = field.getAnnotation(annotations.Field.class);
                    if (fieldAnnotation != null) {
                        Object value = result.getObject(fieldAnnotation.fieldName());
                        field.set(obj, value);
                    }
                }
                return obj;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void __getAllColumns(String tableName) throws SQLException {
        String tableQuery = "SELECT * FROM " + tableName;
        ResultSet result = get_connection().prepareStatement(tableQuery).executeQuery();
        while(result.next()){
            System.out.println(result.getString(2));
        }
    }
}
