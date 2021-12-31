package metamodel;

import annotations.Table;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class __Table {
    /** Primary key */
    private __Column _primaryKey;

    private Class _class;

    @Getter
    @Setter
    private String _tableName;

    @Getter
    private __Column[] _columns;


    public __Table(Class cls){
        Table table = (Table) cls.getAnnotation(Table.class);
        if((table == null) || (table.tableName().equals(""))){
            _tableName = cls.getSimpleName().toUpperCase(Locale.ROOT);
        } else { _tableName = table.tableName(); }
        _class = cls;

        ArrayList<__Column> fields = new ArrayList<>();
    }

    private ArrayList<Method> _getMethods(Class cls){
        ArrayList<Method> methods = new ArrayList<>();

        methods.addAll(Arrays.asList(cls.getDeclaredMethods()));

        return methods;
    }
}
