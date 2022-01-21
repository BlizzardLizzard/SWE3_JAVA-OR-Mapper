package OrmFramework.metamodel;

import OrmFramework.annotations.ForeignKey;
import OrmFramework.annotations.ManyToMany;
import OrmFramework.annotations.OneToMany;
import OrmFramework.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * This class holds all the metadata of a field except for the annotation declarations
 */
public class __Field {

    /**
     * Field type
     */
    @Getter
    @Setter
    private Object _fieldType;

    /**
     * Field name
     */
    @Getter
    @Setter
    private Object _fieldName;

    /**
     * Field value
     */
    @Getter
    @Setter
    private Object _fieldValue;

    /**
     * Field annotated as field flag
     */
    @Getter
    @Setter
    private boolean _field = false;

    /**
     * Field annotated as primary key flag
     */
    @Getter
    @Setter
    private boolean _primaryKey = false;

    /**
     * Field annotated as foreign key flag
     */
    @Getter
    @Setter
    private boolean _foreignKey = false;

    /**
     * Field annotated as one to many flag
     */
    @Getter
    @Setter
    private boolean _oneToMany = false;

    /**
     * Field annotated as many to many flag
     */
    @Getter
    @Setter
    private boolean _manyToMany = false;

    /**
     * List of annotations for field
     */
    @Getter
    @Setter
    private ArrayList<__AnnotationData> _annotationList;

    /**
     * Creates a new field instance
     * @param field Type Field
     * @param obj Type Object
     */
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
        if(field.isAnnotationPresent(OrmFramework.annotations.Field.class)){
            _field = true;
        }
        if(field.isAnnotationPresent(PrimaryKey.class)){
            _primaryKey = true;
        }
        if(field.isAnnotationPresent(ForeignKey.class)){
            _foreignKey = true;
        }
        if(field.isAnnotationPresent(OneToMany.class)){
            _oneToMany = true;
        }
        if(field.isAnnotationPresent(ManyToMany.class)){
            _manyToMany = true;
        }
        //System.out.println(_fieldType + " " + _fieldName + " = " + _fieldValue + " and is PrimaryKey: " + _primaryKey + ", ForeignKey: " +  _foreignKey + ", OneToMany: " + _oneToMany + " and ManyToMany: " + _manyToMany);
        for(Annotation annotation : field.getAnnotations()){
            __AnnotationData _annotationData = new __AnnotationData(annotation, field);
            _annotationList.add(_annotationData);
        }
    }

    /**
     * Gets value of certain declaration of annotation in given field
     * @param _field
     * @param _fieldName
     * @return Object of found field else null
     */
    public static Object getAnnotationFieldValue(__Field _field, String _fieldName){
        for(__AnnotationData _annotationData : _field._annotationList){
            return _annotationData.get_annotationPairs().get(_fieldName);
        }
        return null;
    }

}
