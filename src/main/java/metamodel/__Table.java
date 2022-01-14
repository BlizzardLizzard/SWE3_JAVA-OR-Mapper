package metamodel;

import annotations.PrimaryKey;
import annotations.Table;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class __Table {

    @Getter
    private Class _class;

    @Getter
    @Setter
    private String _tableName;

    @Getter
    @Setter
    private __Row _rows;

    public __Table(Object obj) {
        _class = obj.getClass();
        Table table = (Table) _class.getAnnotation(Table.class);
        if ((table == null) || (table.tableName().equals(""))) {
            _tableName = _class.getSimpleName().toUpperCase(Locale.ROOT);
        } else {
            _tableName = table.tableName();
        }
        HashMap<Object, Object> pairs = new HashMap<>();
        String fieldName;
        System.out.println(Arrays.toString(_class.getDeclaredFields()));
        for (Field name : _class.getDeclaredFields()) {
            if (name.isAnnotationPresent(annotations.Field.class)) {
                name.setAccessible(true);
                fieldName = name.getName();
                //fieldName = name.getAnnotation(annotations.Field.class).fieldName();
                    // get value of field
                    try {
                        pairs.put(fieldName, name.get(obj));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    System.out.println(pairs);
            }
        }
        _rows = new __Row(pairs, _class);
    }
}
