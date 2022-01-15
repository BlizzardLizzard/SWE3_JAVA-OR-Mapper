import annotations.Table;
import lombok.Getter;
import lombok.Setter;
import metamodel.__Field;
import metamodel.__Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
        __Table table = new __Table(obj);
        table.get_rows().set_table(table);

       StringBuilder insertQuery = new StringBuilder("INSERT INTO " + table.get_tableName() + " (");

       boolean first = true;
       for(__Field field : table.get_rows().getFields()){
           if(field.is_primaryKey()){continue;}
           if(first){
               first = false;
           } else {
               insertQuery.append(", ");
           }
           insertQuery.append(field.get_tableFieldName());
       }
        insertQuery.append(") VALUES (");

        boolean firstItem = true;
        for(__Field field : table.get_rows().getFields()){
            if(field.is_primaryKey()){continue;}
            if(firstItem){
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
        for(__Field field : table.get_rows().getFields()){
            if(field.is_primaryKey()){continue;}
            if(field.is_foreignKey()) {
                //TODO: change student to studenid -> int
                try {
                    Object fkObj = obj.getClass().getField(field.get_fieldName()).get(obj);
                    for(Field fieldFK : fkObj.getClass().getFields()){
                        if(fieldFK.isAnnotationPresent(annotations.PrimaryKey.class)){
                            fieldFK.setAccessible(true);
                            ps.setString(index++, fieldFK.get(fkObj).toString());
                        }
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            } else {
                ps.setString(index++, field.get_field());
            }
        }
        ps.execute();
        ps.close();
    }

    public static void update(Object obj) throws SQLException {
        connect();
        StringBuilder updateQuery = new StringBuilder("UPDATE " + obj.getClass().getAnnotation(annotations.Table.class).tableName() + " SET ");
        __Table table = new __Table(obj);
        table.get_rows().set_table(table);
        __Field primaryKey = null;

        boolean first = true;
        for(__Field field : table.get_rows().getFields()){
            if(field.is_primaryKey()){
                primaryKey = field;
                continue;
            }
            if(first){
                first = false;
            } else {
                updateQuery.append(", ");
            }
            updateQuery.append(field.get_fieldName() + " = '" + field.get_field() + "'");
        }

        updateQuery.append(" WHERE ");
        updateQuery.append(primaryKey.get_fieldName()).append(" = ").append(primaryKey.get_field());
        System.out.println(updateQuery);
        PreparedStatement ps = get_connection().prepareStatement(updateQuery.toString());
        ps.execute();
        ps.close();
    }

    public static Object getObject(Class cls, String primaryKey, boolean onlyFields) throws SQLException {
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
                    if(field.isAnnotationPresent(annotations.OneToMany.class) && onlyFields){
                         field.set(obj, getOneToMany(obj, field));
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

    private static ArrayList<Object> getOneToMany(Object obj, Field field) {
        ArrayList<Object> arrayListMany = new ArrayList<>();
        __Table table = new __Table(obj);
        table.get_rows().set_table(table);
        String getObjetsForObject = "SELECT * FROM " + field.getAnnotation(annotations.OneToMany.class).tableName() + " WHERE " + field.getAnnotation(annotations.OneToMany.class).foreignKeyName() + " = '" + table.get_rows().getPrimaryKeyField().get_field() + "'";
        try {
            Class manyObject = field.getAnnotation(annotations.OneToMany.class).classObject();
            field.setAccessible(true);
            ResultSet result = get_connection().prepareStatement(getObjetsForObject).executeQuery();
            while (result.next()) {
                Object tmp = field.getAnnotation(annotations.OneToMany.class).classObject().getConstructor().newInstance();
                for (Field fieldResult : manyObject.getFields()) {
                    annotations.Field fieldAnnotation = fieldResult.getAnnotation(annotations.Field.class);
                    if(fieldAnnotation != null) {
                        if (fieldResult.isAnnotationPresent(annotations.PrimaryKey.class)) {
                            fieldResult.set(tmp, result.getObject(fieldAnnotation.fieldName()));
                        }else {
                            if (fieldResult.isAnnotationPresent(annotations.ForeignKey.class)) {
                                Object fkObject = getObject(fieldResult.getAnnotation(annotations.ForeignKey.class).foreignClass(), (String) result.getObject(fieldAnnotation.fieldName()), false);
                                fieldResult.set(tmp, fkObject);
                            } else {
                                fieldResult.set(tmp, result.getObject(fieldAnnotation.fieldName()));
                            }
                        }
                    }
                }
                arrayListMany.add(tmp);
            }
        } catch (IllegalAccessException | SQLException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return arrayListMany;
    }
}
