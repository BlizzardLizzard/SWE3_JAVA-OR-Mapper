package OrmFramework.metamodel;

import OrmFramework.annotations.ForeignKey;
import OrmFramework.annotations.ManyToMany;
import OrmFramework.annotations.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * This class holds the data of each annotation from the field
 */
public class __AnnotationData {
    /**
     * Holds annotation names and values in a pair
     */
    @Getter
    @Setter
    private HashMap<Object, Object> _annotationPairs;

    /**
     * Type of annotation
     */
    @Getter
    @Setter
    private Class<? extends Annotation> _annotationType;

    /**
     * Creates a new annotation object
     * @param annotation Type Annotaion
     * @param field Type Field
     */
    public __AnnotationData(Annotation annotation, Field field) {
        _annotationPairs = new HashMap<>();
        _annotationType = annotation.annotationType();
        if(field.isAnnotationPresent(OrmFramework.annotations.Field.class)){
            _annotationPairs.put("fieldName", field.getAnnotation(OrmFramework.annotations.Field.class).fieldName());
            _annotationPairs.put("notNull", field.getAnnotation(OrmFramework.annotations.Field.class).notNull());
            _annotationPairs.put("unique", field.getAnnotation(OrmFramework.annotations.Field.class).unique());
        }
        if(field.isAnnotationPresent(ForeignKey.class)){
            _annotationPairs.put("fKTableName", field.getAnnotation(ForeignKey.class).fKTableName());
            _annotationPairs.put("foreignClass", field.getAnnotation(ForeignKey.class).foreignClass());
        }
        if(field.isAnnotationPresent(OneToMany.class)){
            _annotationPairs.put("tableName", field.getAnnotation(OneToMany.class).tableName());
            _annotationPairs.put("foreignKeyName", field.getAnnotation(OneToMany.class).foreignKeyName());
            _annotationPairs.put("classObject", field.getAnnotation(OneToMany.class).classObject());
        }
        if(field.isAnnotationPresent(ManyToMany.class)){
            _annotationPairs.put("tableName", field.getAnnotation(ManyToMany.class).tableName());
            _annotationPairs.put("foreignKeyNameOwn", field.getAnnotation(ManyToMany.class).foreignKeyNameOwn());
            _annotationPairs.put("foreignKeyNameOther", field.getAnnotation(ManyToMany.class).foreignKeyNameOther());
            _annotationPairs.put("classObject", field.getAnnotation(ManyToMany.class).classObject());
            _annotationPairs.put("manyToManyTableName", field.getAnnotation(ManyToMany.class).manyToManyTableName());
        }
        //System.out.println(_annotationType + " with fields: " + _annotationPairs + "\n");
    }
}
