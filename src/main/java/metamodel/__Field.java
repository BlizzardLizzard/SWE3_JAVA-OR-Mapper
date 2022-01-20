package metamodel;

import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class __Field {
    @Getter
    @Setter
    private Object _fieldType;

    @Getter
    @Setter
    private Object _fieldName;

    @Getter
    @Setter
    private Object _fieldValue;

    @Getter
    @Setter
    private boolean _field = false;

    @Getter
    @Setter
    private boolean _primaryKey = false;

    @Getter
    @Setter
    private boolean _foreignKey = false;

    @Getter
    @Setter
    private boolean _oneToMany = false;

    @Getter
    @Setter
    private boolean _manyToMany = false;

    @Getter
    @Setter
    private ArrayList<__AnnotationData> _annotationList;

    public __Field(Field field, Object obj) {
        _annotationList = new ArrayList<>();
        field.setAccessible(true);
        _fieldType = field.getType();
        _fieldName = field.getName();
        try {
            _fieldValue = field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(field.isAnnotationPresent(annotations.Field.class)){
            _field = true;
        }
        if(field.isAnnotationPresent(annotations.PrimaryKey.class)){
            _primaryKey = true;
        }
        if(field.isAnnotationPresent(annotations.ForeignKey.class)){
            _foreignKey = true;
        }
        if(field.isAnnotationPresent(annotations.OneToMany.class)){
            _oneToMany = true;
        }
        if(field.isAnnotationPresent(annotations.ManyToMany.class)){
            _manyToMany = true;
        }
        System.out.println(_fieldType + " " + _fieldName + " = " + _fieldValue + " and is PrimaryKey: " + _primaryKey + ", ForeignKey: " +  _foreignKey + ", OneToMany: " + _oneToMany + " and ManyToMany: " + _manyToMany);
        for(Annotation annotation : field.getAnnotations()){
            __AnnotationData _annotationData = new __AnnotationData(annotation, field);
            _annotationList.add(_annotationData);
        }
    }

    public static Object getAnnotationFieldValue(__Field _field, String _fieldName){
        for(__AnnotationData _annotationData : _field._annotationList){
            return _annotationData.get_annotationPairs().get(_fieldName);
        }
        return null;
    }

}
