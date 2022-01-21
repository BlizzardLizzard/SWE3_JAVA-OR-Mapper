package OrmFramework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This marks a field class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {

    /**
     * Holds field name
     */
    String fieldName() default "";

    /**
     * Declares field null or not
     */
    boolean notNull() default false;

    /**
     * Declares a field unique or not
     */
    boolean unique() default false;
}
