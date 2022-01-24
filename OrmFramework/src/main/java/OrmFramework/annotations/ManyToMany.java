package OrmFramework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This marks a many-to-many class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {

    /**
     * Table name
     */
    String tableName() default "";

    /**
     * Name of own row from called table in junction table
     */
    String foreignKeyNameOwn() default "";

    /**
     * Name of other class row in junction table
     */
    String foreignKeyNameOther() default "";

    /**
     * Class of object in arraylist
     * @return
     */
    Class classObject() default Void.class;

    /**
     * Name of junction table
     */
    String manyToManyTableName() default "";
}
