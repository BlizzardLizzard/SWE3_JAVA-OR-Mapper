import lombok.Getter;
import lombok.Setter;
import metamodel.__Field;
import metamodel.__TableObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;

public final class Orm {
    /** Get and Set Connection */
    @Getter
    @Setter
    private static Connection _connection;

    /** Connects to the wanted database */
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
        for(__Field _field : _tableObject.get_fields()) {
            if (_field.is_field()) {
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
        for(__Field _field : _tableObject.get_fields()) {
            if (_field.is_field()) {
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
            for(__Field _field : _tableObject.get_fields()) {
                if (_field.is_field()) {
                    if(_field.is_foreignKey()) {
                        //TODO: change student to studenid -> int
                        //TODO: implement save of foreign and other relations without their id
                        Object fkObj = _field.get_fieldValue();
                        __TableObject _fkTableObj = new __TableObject(fkObj);
                        for(__Field _fkField : _fkTableObj.get_fields()){
                            if(_fkField.is_primaryKey()){
                                ps.setString(index, _fkField.get_fieldValue().toString());
                            }
                        }
                    } else {
                        //TODO: adapt to value type
                        if(_field.is_primaryKey()){
                            ps.setInt(index++, Integer.parseInt(_field.get_fieldValue().toString()));
                        } else {
                            ps.setString(index++, _field.get_fieldValue().toString());
                        }
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
        __Field _pKField = __TableObject.getPkField(_tableObject);

        boolean firstItem = true;
        for(__Field _field : _tableObject.get_fields()) {
            if(_field.is_oneToMany() || _field.is_manyToMany()){
                updateToManyList(_field, obj);
            }
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
                    for(__Field _fkField : _fkTableObj.get_fields()){
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

    private static void updateToManyList(__Field _field, Object obj){
        if(_field.get_fieldValue() != null) {
            ArrayList<Object> arrayListMany = (ArrayList<Object>) _field.get_fieldValue();
            for (Object objList : arrayListMany) {
                __TableObject _table = new __TableObject(objList);
                if (__TableObject.getPkField(_table) != null) {
                    update(objList);
                } else {
                    save(objList);
                }
                if (_field.is_manyToMany()) {
                    __TableObject _tableMainObj = new __TableObject(obj);
                    String checkIfExists = "SELECT COUNT(*) FROM " + __Field.getAnnotationFieldValue(_field, "manyToManyTableName") + " WHERE " + __Field.getAnnotationFieldValue(_field, "foreignKeyNameOwn") + " = " + __TableObject.getPkField(_table).get_fieldValue() + " AND " + __Field.getAnnotationFieldValue(_field, "foreignKeyNameOther") + " = " + __TableObject.getPkField(_tableMainObj).get_fieldValue();
                    String insertManyFk = "INSERT INTO " + __Field.getAnnotationFieldValue(_field, "manyToManyTableName") + " (" + __Field.getAnnotationFieldValue(_field, "foreignKeyNameOwn") + ", " + __Field.getAnnotationFieldValue(_field, "foreignKeyNameOther") + ") VALUES (" + __TableObject.getPkField(_table).get_fieldValue() + ", " + __TableObject.getPkField(_tableMainObj).get_fieldValue() + ")";
                    try {
                        ResultSet result = get_connection().prepareStatement(checkIfExists).executeQuery();
                        result.next();
                        int numberOfRows = result.getInt(1);
                        if(numberOfRows < 1){
                            PreparedStatement ps = get_connection().prepareStatement(insertManyFk);
                            ps.execute();
                            ps.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Object getObject(Class cls, String primaryKey, boolean toMany){
        connect();
        try {
            Object obj = cls.getConstructor().newInstance();
            __TableObject _table = new __TableObject(obj);
            __Field _field = __TableObject.getPkField(_table);
            String getObjectQuery = "SELECT * FROM " + _table.get_tableName() + " WHERE " + __Field.getAnnotationFieldValue(_field, "fieldName") + " = " + primaryKey;
            System.out.println(getObjectQuery);
            ResultSet result = get_connection().prepareStatement(getObjectQuery).executeQuery();
            result.next();
            for (Field field : cls.getDeclaredFields()) {
                annotations.Field fieldAnnotation = field.getAnnotation(annotations.Field.class);
                if (fieldAnnotation != null) {
                    if (field.isAnnotationPresent(annotations.ForeignKey.class)) {
                        field.set(obj, getObject(field.getAnnotation(annotations.ForeignKey.class).foreignClass(), (String) result.getObject(field.getAnnotation(annotations.Field.class).fieldName()), false));
                    } else {
                        Object value = result.getObject(fieldAnnotation.fieldName());
                        field.set(obj, value);
                    }
                }
                if(toMany) {
                    if (field.isAnnotationPresent(annotations.OneToMany.class)) {
                        field.set(obj, getOneToManyList(field, primaryKey));
                    }
                    if (field.isAnnotationPresent(annotations.ManyToMany.class)) {
                        field.set(obj, getManyToManyList(field, primaryKey));
                    }
                }
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<Object> getOneToManyList(Field field, String primaryKey){
        ArrayList<Object> arrayListMany = new ArrayList<>();
        String getObjetsForObject = new StringBuilder().append("SELECT * FROM ").append(field.getAnnotation(annotations.OneToMany.class).tableName()).append(" WHERE ").append(field.getAnnotation(annotations.OneToMany.class).foreignKeyName()).append(" = '").append(primaryKey).append("'").toString();
        try {
            ResultSet resultMany = get_connection().prepareStatement(getObjetsForObject).executeQuery();
            Class oneToManyCls = field.getAnnotation(annotations.OneToMany.class).classObject();
            while (resultMany.next()) {
                Object oneToManyObj = oneToManyCls.getConstructor().newInstance();
                for(Field oneToManyField : oneToManyCls.getFields()){
                    annotations.Field oneToManyFieldAnnotation = oneToManyField.getAnnotation(annotations.Field.class);
                    if(oneToManyFieldAnnotation != null){
                        if(oneToManyField.isAnnotationPresent(annotations.ForeignKey.class)){
                            Object fkObject = getObject(oneToManyField.getAnnotation(annotations.ForeignKey.class).foreignClass(), primaryKey, false);
                            oneToManyField.set(oneToManyObj, fkObject);
                        } else {
                            oneToManyField.set(oneToManyObj, resultMany.getObject(oneToManyFieldAnnotation.fieldName()));
                        }
                    }
                }
                arrayListMany.add(oneToManyObj);
            }
            return arrayListMany;
        } catch (SQLException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<Object> getManyToManyList(Field field, String primaryKey){
        ArrayList<Object> arrayListMany = new ArrayList<>();
        //TODO: get ids from teacher_course and then get every object from the course fields
        String getIds = "SELECT * FROM " + field.getAnnotation(annotations.ManyToMany.class).manyToManyTableName() + " WHERE " + field.getAnnotation(annotations.ManyToMany.class).foreignKeyNameOwn() + " = " + primaryKey;
        System.out.println(getIds);
        try {
            ResultSet resultMany = get_connection().prepareStatement(getIds).executeQuery();
            while (resultMany.next()){
                Object tmp = getObject(field.getAnnotation(annotations.ManyToMany.class).classObject(), resultMany.getObject(field.getAnnotation(annotations.ManyToMany.class).foreignKeyNameOther()).toString(), false);
                arrayListMany.add(tmp);
            }
            return arrayListMany;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
