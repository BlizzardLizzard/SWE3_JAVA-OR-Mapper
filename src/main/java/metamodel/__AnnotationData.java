package metamodel;

import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

public class __AnnotationData {
    @Getter
    @Setter
    private HashMap<Object, Object> _annotationPairs;

    @Getter
    @Setter
    private Class<? extends Annotation> _annotationType;

    public __AnnotationData(Annotation annotation, Field field) {
        _annotationPairs = new HashMap<>();
        _annotationType = annotation.annotationType();
        //TODO: Delete duplicates getting into field and primary key for example
        if(field.isAnnotationPresent(annotations.Field.class)){
            _annotationPairs.put("fieldName", field.getAnnotation(annotations.Field.class).fieldName());
        }
        if(field.isAnnotationPresent(annotations.ForeignKey.class)){
            _annotationPairs.put("fkTableName", field.getAnnotation(annotations.ForeignKey.class).fKTableName());
            _annotationPairs.put("foreignClass", field.getAnnotation(annotations.ForeignKey.class).foreignClass());
        }
        if(field.isAnnotationPresent(annotations.OneToMany.class)){
            _annotationPairs.put("tableName", field.getAnnotation(annotations.OneToMany.class).tableName());
            _annotationPairs.put("foreignKeyName", field.getAnnotation(annotations.OneToMany.class).foreignKeyName());
            _annotationPairs.put("classObject", field.getAnnotation(annotations.OneToMany.class).classObject());
        }
        System.out.println(_annotationType + " with fields: " + _annotationPairs + "\n");
    }
}
