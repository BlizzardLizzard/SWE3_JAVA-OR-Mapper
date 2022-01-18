package metamodel;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

public class __TableObject {

    @Getter
    @Setter
    private String _tableName;

    @Getter
    @Setter
    private ArrayList<__Field> _fields;

    public __TableObject(Object obj) {
        _fields = new ArrayList<>();
        if((obj.getClass().getAnnotation(annotations.Table.class) == null) || (obj.getClass().getAnnotation(annotations.Table.class).equals(""))){
            _tableName = obj.toString().replace("class", "").trim().toLowerCase(Locale.ROOT);
        } else {
            _tableName = obj.getClass().getAnnotation(annotations.Table.class).tableName();
        }
        System.out.println("Class: " + _tableName);
        for(Field field : obj.getClass().getFields()){
            __Field _field = new __Field(field, obj);
            _fields.add(_field);
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    public static __Field getPkField(__TableObject _tableObject){
        for(__Field _field : _tableObject._fields){
            if(_field.is_primaryKey()){
                return _field;
            }
        }
        return null;
    }
}
