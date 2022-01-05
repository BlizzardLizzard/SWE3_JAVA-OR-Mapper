package metamodel;

import lombok.Getter;
import lombok.Setter;

public class __Field {
    @Getter
    @Setter
    private String _fieldName;

    @Getter
    @Setter
    private String _field;

    @Getter
    @Setter
    private Class _type;

    @Getter
    @Setter
    private boolean _primaryKey = false;

    @Getter
    @Setter
    private boolean _foreignKey = false;

    public __Field(String fieldName, String field, Class cls) {
        _fieldName = fieldName;
        _field = field;
        try {
            if(cls.getDeclaredField(fieldName).isAnnotationPresent(annotations.PrimaryKey.class)){
                _primaryKey = true;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
