package OrmFramework.metamodel;

import OrmFramework.annotations.Table;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This class holds the table name and fields contained in the object that has been passed
 */
public class __TableObject {

    /**
     * Table name
     */
    @Getter
    @Setter
    private String _tableName;

    /**
     * Fields in object
     */
    @Getter
    @Setter
    private ArrayList<__Field> _fields;

    /**
     * Creates a new table object
     * @param obj Type Object
     */
    public __TableObject(Object obj) {
        _fields = new ArrayList<>();
        if((obj.getClass().getAnnotation(Table.class) == null) || (obj.getClass().getAnnotation(Table.class).equals(""))){
            _tableName = obj.toString().replace("class", "").trim().toLowerCase(Locale.ROOT);
        } else {
            _tableName = obj.getClass().getAnnotation(Table.class).tableName();
        }
        //System.out.println("Class: " + _tableName);
        for(Field field : obj.getClass().getFields()){
            __Field _field = new __Field(field, obj);
            _fields.add(_field);
        }
        //System.out.println("--------------------------------------------------------------------------");
    }

    /**
     * Gets the primary key field of the table object
     * @param _tableObject Type __TableObject
     * @return __Field object or null if no primary key exists
     */
    public static __Field getPkField(__TableObject _tableObject){
        for(__Field _field : _tableObject._fields){
            if(_field.is_primaryKey()){
                return _field;
            }
        }
        return null;
    }
}
