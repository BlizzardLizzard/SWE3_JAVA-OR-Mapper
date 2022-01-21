package OrmFramework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This marks a foreign key class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {

    /**
     * Foreign key table name
     */
    String fKTableName() default "";

    /**
     * Foreign key class
     */
    Class foreignClass() default Void.class;
}
