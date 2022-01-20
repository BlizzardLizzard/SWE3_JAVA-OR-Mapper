import lombok.Getter;
import lombok.Setter;
import metamodel.__Field;
import metamodel.__TableObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;

public final class Orm {
    /** Get and Set Connection */
    @Getter
    @Setter
    private static Connection _connection;

    /** Connects to the wanted database */
    public static void connect (String connectionString, String username, String password){
        try {
            _connection = DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void save(Object obj){
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
                insertQuery.append(__Field.getAnnotationFieldValue(_field, "fieldName"));
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
                        Object fkObj = _field.get_fieldValue();
                        __TableObject _fkTableObj = new __TableObject(fkObj);
                        for(__Field _fkField : _fkTableObj.get_fields()){
                            if(_fkField.is_primaryKey()){
                               index = setPsForTypeForField(ps, _fkField, index);
                            }
                        }
                    } else {
                        index = setPsForTypeForField(ps, _field, index);
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
        __TableObject _tableObject = new __TableObject(obj);
        StringBuilder updateQuery = new StringBuilder("UPDATE " + _tableObject.get_tableName() + " SET ");
        __Field _pKField = __TableObject.getPkField(_tableObject);

        boolean firstItem = true;
        for(__Field _field : _tableObject.get_fields()) {
            if(_field.is_oneToMany() || _field.is_manyToMany()){
                updateToManyList(_field, obj);
            }
            if (_field.is_field()) {
                if(_field.is_primaryKey()){continue;}
                if (firstItem) {
                    firstItem = false;
                } else {
                    updateQuery.append(", ");
                }
                if(_field.is_foreignKey()){
                    Object fkObject = _field.get_fieldValue();
                    __TableObject _fkTableObj = new __TableObject(fkObject);
                    for(__Field _fkField : _fkTableObj.get_fields()){
                        if(_fkField.is_primaryKey()){
                            updateQuery.append(__Field.getAnnotationFieldValue(_field, "fieldName")).append(" = ?");
                        }
                    }
                } else {
                    updateQuery.append(__Field.getAnnotationFieldValue(_field, "fieldName")).append(" = ?");
                }
            }
        }
        updateQuery.append(" WHERE ");
        updateQuery.append(__Field.getAnnotationFieldValue(_pKField, "fieldName")).append(" = ?");
        System.out.println(updateQuery);
        try {
            PreparedStatement ps = get_connection().prepareStatement(updateQuery.toString());
            int index = 1;
            for(__Field _field : _tableObject.get_fields()) {
                if (_field.is_field()){
                    if(!_field.is_primaryKey() && !_field.is_foreignKey()) {
                        index = setPsForTypeForField(ps, _field, index);
                    }
                    if (_field.is_foreignKey()) {
                        Object fkObj = _field.get_fieldValue();
                        __TableObject _fkTableObj = new __TableObject(fkObj);
                        for (__Field _fkField : _fkTableObj.get_fields()) {
                            if (_fkField.is_primaryKey()) {
                                index = setPsForTypeForField(ps, _fkField, index);
                            }
                        }
                    }
                }
            }
            setPsForTypeForField(ps, _pKField, index);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateToManyList(__Field _field, Object obj){
        try {
            if(_field.get_fieldValue() != null) {
                ArrayList<Object> arrayListMany = (ArrayList<Object>) _field.get_fieldValue();
                for (Object objList : arrayListMany) {
                    __TableObject _table = new __TableObject(objList);
                    String checkIfExists = "SELECT COUNT(*) FROM " + _table.get_tableName() + " WHERE " + __TableObject.getPkField(_table).get_fieldName() + " = ?";
                    PreparedStatement psCountOneToMany = get_connection().prepareStatement(checkIfExists);
                    setPsForTypeForField(psCountOneToMany, __TableObject.getPkField(_table), 1);
                    ResultSet resultOneToMany = psCountOneToMany.executeQuery();
                    resultOneToMany.next();
                    int numberOfRows = resultOneToMany.getInt(1);
                    System.out.println(numberOfRows);
                    if (numberOfRows >= 1) {
                        update(objList);
                    } else {
                        save(objList);
                    }
                    if (_field.is_manyToMany()) {
                        __TableObject _tableMainObj = new __TableObject(obj);
                        checkIfExists = "SELECT COUNT(*) FROM " + __Field.getAnnotationFieldValue(_field, "manyToManyTableName") + " WHERE " + __Field.getAnnotationFieldValue(_field, "foreignKeyNameOwn") + " = ?  AND " + __Field.getAnnotationFieldValue(_field, "foreignKeyNameOther") + " = ?";
                        String insertManyFk = "INSERT INTO " + __Field.getAnnotationFieldValue(_field, "manyToManyTableName") + " (" + __Field.getAnnotationFieldValue(_field, "foreignKeyNameOwn") + ", " + __Field.getAnnotationFieldValue(_field, "foreignKeyNameOther") + ") VALUES ( ?, ? )";
                        PreparedStatement psCountManyToMany = get_connection().prepareStatement(checkIfExists);
                        setPsForTypeForField(psCountManyToMany, __TableObject.getPkField(_tableMainObj), 1);
                        setPsForTypeForField(psCountManyToMany, __TableObject.getPkField(_table), 2);
                        ResultSet result = psCountManyToMany.executeQuery();
                        result.next();
                        numberOfRows = result.getInt(1);
                        if(numberOfRows < 1){
                            PreparedStatement psInsert = get_connection().prepareStatement(insertManyFk);
                            setPsForTypeForField(psInsert, __TableObject.getPkField(_tableMainObj), 1);
                            setPsForTypeForField(psInsert, __TableObject.getPkField(_table), 2);
                            psInsert.execute();
                            psInsert.close();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getObject(Class cls, Object primaryKey, boolean toMany){
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
                        field.set(obj, getObject(field.getAnnotation(annotations.ForeignKey.class).foreignClass(), result.getObject(field.getAnnotation(annotations.Field.class).fieldName()), false));
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
            return (T) cls.cast(obj);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<Object> getOneToManyList(Field field, Object primaryKey){
        ArrayList<Object> arrayListMany = new ArrayList<>();
        String getObjetsForObject = "SELECT * FROM " + field.getAnnotation(annotations.OneToMany.class).tableName() + " WHERE " + field.getAnnotation(annotations.OneToMany.class).foreignKeyName() + " = " + primaryKey;
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

    private static ArrayList<Object> getManyToManyList(Field field, Object primaryKey){
        ArrayList<Object> arrayListMany = new ArrayList<>();
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

    private static int setPsForTypeForField(PreparedStatement ps, __Field _field, int index){
        if(_field.is_field()) {
            try {
            Type type = (Type) _field.get_fieldType();
                if(type.equals(Integer.class)){
                        ps.setInt(index++, (Integer) _field.get_fieldValue());
                }
                if(type.equals(String.class)){
                    ps.setString(index++, (String) _field.get_fieldValue());
                }
                if(type.equals(Boolean.class)){
                    ps.setBoolean(index++, (Boolean) _field.get_fieldValue());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return index;
    }

    public static void createTable(Class cls){
        try {
            Object obj = cls.getConstructor().newInstance();
            __TableObject _table = new __TableObject(obj);
            __Field _pkField = null;
            StringBuilder createQuery = new StringBuilder("CREATE TABLE " + _table.get_tableName() + " (");
            boolean firstItem = true;
            for(__Field _field : _table.get_fields()){
                if(_field.is_field()) {
                    if(_field.is_primaryKey()){
                        _pkField = _field;
                    }
                    if (firstItem) {
                        firstItem = false;
                    } else {
                        createQuery.append(", ");
                    }
                    createQuery.append(__Field.getAnnotationFieldValue(_field, "fieldName")).append(" ").append(changeTypeForSQL(_field));
                    if ((boolean) __Field.getAnnotationFieldValue(_field, "notNull")) {
                        createQuery.append(" NOT NULL");
                    }
                    if ((boolean) __Field.getAnnotationFieldValue(_field, "unique")) {
                        createQuery.append(" UNIQUE");
                    }

                }
            }
            if(_pkField != null){
                createQuery.append(", PRIMARY KEY (" + __Field.getAnnotationFieldValue(_pkField, "fieldName") + ")");
            }
            createQuery.append(")");
            System.out.println(createQuery);
            PreparedStatement ps = get_connection().prepareStatement(createQuery.toString());
            ps.execute();
            ps.close();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SQLException |NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void createTableFK(Class cls, Class clsFK){
        try {
            Object obj = cls.getConstructor().newInstance();
            __TableObject _table = new __TableObject(obj);
            __Field _pkField = __TableObject.getPkField(_table);
            __Field _fkField = null;
            StringBuilder createQuery = new StringBuilder("CREATE TABLE " + _table.get_tableName() + " (");
            boolean firstItem = true;
            for(__Field _field : _table.get_fields()){
                if(_field.is_field()) {
                    if(_field.is_foreignKey()){
                        _fkField = _field;
                    }
                    if (firstItem) {
                        firstItem = false;
                    } else {
                        createQuery.append(", ");
                    }
                    if(_field.is_foreignKey()){
                        Object objFK = clsFK.getConstructor().newInstance();
                        __TableObject _tableFK = new __TableObject(objFK);
                        __Field _fkFieldId = __TableObject.getPkField(_tableFK);
                        createQuery.append(__Field.getAnnotationFieldValue(_field, "fieldName")).append(" ").append(changeTypeForSQL(_fkFieldId));
                    } else {
                        createQuery.append(__Field.getAnnotationFieldValue(_field, "fieldName")).append(" ").append(changeTypeForSQL(_field));
                    }
                    if ((boolean) __Field.getAnnotationFieldValue(_field, "notNull")) {
                        createQuery.append(" NOT NULL");
                    }
                    if ((boolean) __Field.getAnnotationFieldValue(_field, "unique")) {
                        createQuery.append(" UNIQUE");
                    }
                }
            }
            if(_pkField != null){
                createQuery.append(", PRIMARY KEY (" + __Field.getAnnotationFieldValue(_pkField, "fieldName") + ")");
            }
            if(_fkField != null){
                Object objFK = clsFK.getConstructor().newInstance();
                __TableObject _tableFK = new __TableObject(objFK);
                __Field _fkFieldId = __TableObject.getPkField(_tableFK);
                createQuery.append(", FOREIGN KEY (" + __Field.getAnnotationFieldValue(_fkField, "fieldName") + ")");
                createQuery.append(" REFERENCES " + _tableFK.get_tableName() + " (" + __Field.getAnnotationFieldValue(_fkFieldId, "fieldName") + ")");
            }
            createQuery.append(")");
            System.out.println(createQuery);
            PreparedStatement ps = get_connection().prepareStatement(createQuery.toString());
            ps.execute();
            ps.close();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | SQLException |NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void linkManyToManyTables(Class clsOne, Class clsTwo){
        try {
            __TableObject _tableOne = new __TableObject(clsOne.getConstructor().newInstance());
            __TableObject _tableTwo = new __TableObject(clsTwo.getConstructor().newInstance());
            __Field _manyToManyField = null;
            __Field _primaryKeyOne = __TableObject.getPkField(_tableOne);
            __Field _primaryKeyTwo = __TableObject.getPkField(_tableTwo);
            for(__Field _field : _tableOne.get_fields()){
                if(_field.is_manyToMany()){
                    _manyToManyField = _field;
                }
            }
            String linkManyToMany = "CREATE TABLE " + __Field.getAnnotationFieldValue(_manyToManyField, "manyToManyTableName") + " (" + __Field.getAnnotationFieldValue(_manyToManyField, "foreignKeyNameOwn") + " " + changeTypeForSQL(_primaryKeyOne) + ", " + __Field.getAnnotationFieldValue(_manyToManyField, "foreignKeyNameOther") + " " + changeTypeForSQL(_primaryKeyTwo) + ", FOREIGN KEY (" + __Field.getAnnotationFieldValue(_manyToManyField, "foreignKeyNameOwn") + ") REFERENCES " + _tableOne.get_tableName() + " (" + __Field.getAnnotationFieldValue(_primaryKeyOne, "fieldName") + ")";
            linkManyToMany += ", FOREIGN KEY (" + __Field.getAnnotationFieldValue(_manyToManyField, "foreignKeyNameOther") + ") REFERENCES " + _tableTwo.get_tableName() + " (" + __Field.getAnnotationFieldValue(_primaryKeyTwo, "fieldName") + "))";
            System.out.println(linkManyToMany);
            get_connection().prepareStatement(linkManyToMany).execute();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static String changeTypeForSQL(__Field _field){
        String sqlType = "";
        if(_field.is_field()) {
            Type type = (Type) _field.get_fieldType();
            if(type.equals(Integer.class)){
                sqlType = "int";
            }
            if(type.equals(String.class)){
                sqlType = "varchar";
            }
            if(type.equals(Boolean.class)){
                sqlType = "boolean";
            }
        }
        return sqlType;
    }
}