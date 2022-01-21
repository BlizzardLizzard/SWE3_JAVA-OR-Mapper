package OrmFramework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This marks a one-to-many class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {

    /**
     * Table name
     */
    String tableName() default "";

    /**
     * Name of foreign key in foreign key table
     */
    String foreignKeyName() default "";

    /**
     * Class of object used in array list
     */
    Class classObject() default Void.class;
}
