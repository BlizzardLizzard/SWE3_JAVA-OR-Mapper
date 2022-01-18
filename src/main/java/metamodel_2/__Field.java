package metamodel_2;

import lombok.Getter;
import lombok.Setter;

public class __Field {
    @Getter
    @Setter
    private String _fieldName;

    @Getter
    @Setter
    private String _tableFieldName;

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

    @Getter
    @Setter
    private boolean _oneToMany = false;

    public __Field(String fieldName, String field, Class cls) {
        _fieldName = fieldName;
        _field = field;
        _type = cls;
        try {
            _tableFieldName = cls.getDeclaredField(fieldName).getAnnotation(annotations.Field.class).fieldName();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            if(cls.getDeclaredField(fieldName).isAnnotationPresent(annotations.PrimaryKey.class)){
                _primaryKey = true;
            }if(cls.getDeclaredField(fieldName).isAnnotationPresent(annotations.ForeignKey.class)){
                _foreignKey = true;
                _type = cls.getDeclaredField(fieldName).getAnnotation(annotations.ForeignKey.class).foreignClass();
            } if(cls.getDeclaredField(fieldName).isAnnotationPresent(annotations.OneToMany.class)){
                _oneToMany = true;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
