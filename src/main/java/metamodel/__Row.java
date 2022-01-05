package metamodel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class __Row {

    /** Table */
    @Getter
    @Setter
    private __Table _table;

    @Getter
    @Setter
    private HashMap<Object, Object> _pairs;

    @Getter
    private HashSet<__Field> fields;

    public __Row(HashMap<Object,Object> pairs, Class cls){
        _pairs = pairs;
        fields = new HashSet<>();
        for(Object key : pairs.keySet()){
            __Field field = new __Field(key.toString(), pairs.get(key).toString(), cls);
            fields.add(field);
        }
    }

}
