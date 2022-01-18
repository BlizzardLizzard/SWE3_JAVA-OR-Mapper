import annotations.Table;
import lombok.Getter;
import lombok.Setter;
import metamodel.__Field;
import metamodel.__TableObject;
import metamodel_2.__Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class Orm {

    /** Get and Set Connection */
    @Getter
    @Setter
    private static Connection _connection;

    /** Connects to the wanted database
     * @throws SQLException Gets thrown when a connection failed */
    public static void connect (){
        try {
            _connection = DriverManager.getConnection("jdbc:postgresql://localhost/Orm", "postgres", "passwort");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void save(Object obj){
        //TODO: move connect to user not Framework
        connect();
        __TableObject _tableObject= new __TableObject(obj);

        StringBuilder insertQuery = new StringBuilder("INSERT INTO " + _tableObject.get_tableName() + " (");

        boolean firstItem = true;
        for(metamodel.__Field _field : _tableObject.get_fields()) {
            if (_field.is_field()) {
                if (_field.is_primaryKey()) {
                    continue;
                }
                if (firstItem) {
                    firstItem = false;
                } else {
                    insertQuery.append(", ");
                }
                insertQuery.append(_field.getAnnotationFieldValue(_field, "fieldName"));
            }
        }
        insertQuery.append(") VALUES (");

        firstItem = true;
        for(metamodel.__Field _field : _tableObject.get_fields()) {
            if (_field.is_field()) {
                if (_field.is_primaryKey()) {
                    continue;
                }
                if (firstItem) {
                    firstItem = false;
                } else {
                    insertQuery.append(", ");
                }
                insertQuery.append("?");
            }
        }
        insertQuery.append(")");
        try {
            PreparedStatement ps = get_connection().prepareStatement(insertQuery.toString());
            System.out.println(insertQuery);

            int index = 1;
            for(metamodel.__Field _field : _tableObject.get_fields()) {
                if (_field.is_field()) {
                    if (_field.is_primaryKey()) {continue;}
                    if(_field.is_foreignKey()) {
                        //TODO: change student to studenid -> int
                        Object fkObj = _field.get_fieldValue();
                        __TableObject _fkTableObj = new __TableObject(fkObj);
                        for(metamodel.__Field _fkField : _fkTableObj.get_fields()){
                            if(_fkField.is_primaryKey()){
                                ps.setString(index, _fkField.get_fieldValue().toString());
                            }
                        }
                    } else {
                        ps.setString(index++, _field.get_fieldValue().toString());
                    }
                }
            }
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Object obj){
        connect();
        __TableObject _tableObject = new __TableObject(obj);
        StringBuilder updateQuery = new StringBuilder("UPDATE " + _tableObject.get_tableName() + " SET ");
        metamodel.__Field _pKField = __TableObject.getPrimaryKeyField(_tableObject);

        boolean firstItem = true;
        for(metamodel.__Field _field : _tableObject.get_fields()) {
            if (_field.is_field()) {
                if (_field.is_primaryKey()) {
                    continue;
                }
                if (firstItem) {
                    firstItem = false;
                } else {
                    updateQuery.append(", ");
                }
                //TODO: apply changes according to type
                if(_field.is_foreignKey()){
                    Object fkObject = _field.get_fieldValue();
                    __TableObject _fkTableObj = new __TableObject(fkObject);
                    for(metamodel.__Field _fkField : _fkTableObj.get_fields()){
                        if(_fkField.is_primaryKey()){
                            updateQuery.append(__Field.getAnnotationFieldValue(_field, "fieldName") + " = '" + _fkField.get_fieldValue().toString() + "'");
                        }
                    }
                } else {
                    updateQuery.append(__Field.getAnnotationFieldValue(_field, "fieldName") + " = '" + _field.get_fieldValue() + "'");
                }
            }
        }
        updateQuery.append(" WHERE ");
        updateQuery.append(__Field.getAnnotationFieldValue(_pKField, "fieldName")).append(" = ").append(_pKField.get_fieldValue());
        System.out.println(updateQuery);
        try {
            PreparedStatement ps = get_connection().prepareStatement(updateQuery.toString());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateOneToMany(Field field, Object obj){
        field.setAccessible(true);
        try {
            ArrayList objects = (ArrayList) field.get(obj);
            for(Object tmp : objects){
                __Table table = new __Table(tmp);
                table.get_rows().set_table(table);
                //TODO: INSERT INTO test (id, test) VALUES (21, 'conflict') ON CONFLICT (id) DO UPDATE SET test = excluded.test;
                //TODO: filter if id is null and if so create new entry
                if(table.get_rows().getPrimaryKeyField() == null){
                    save(tmp);
                } else {
                    update(tmp);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static Object getObject(Class cls, String primaryKey){
        connect();
        try {
            Object obj = cls.getConstructor().newInstance();
            __TableObject _table = new __TableObject(obj);
            String getObjectQuery = "SELECT * FROM " + _table.get_tableName() + " WHERE id = " + primaryKey;
            ResultSet result = get_connection().prepareStatement(getObjectQuery).executeQuery();

            result.next();
            System.out.println(result);
            for(Field field : cls.getDeclaredFields()) {
                annotations.Field fieldAnnotation = field.getAnnotation(annotations.Field.class);
                if (fieldAnnotation != null) {
                    if (field.isAnnotationPresent(annotations.ForeignKey.class)) {
                        field.set(obj, getObject(field.getAnnotation(annotations.ForeignKey.class).foreignClass(), "1"));
                    } else {
                        Object value = result.getObject(fieldAnnotation.fieldName());
                        field.set(obj, value);
                    }
                }
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException e) {
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
                                //Object fkObject = getObject(fieldResult.getAnnotation(annotations.ForeignKey.class).foreignClass(), (String) result.getObject(fieldAnnotation.fieldName()), false);
                                //fieldResult.set(tmp, fkObject);
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
