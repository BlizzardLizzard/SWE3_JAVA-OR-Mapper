package OrFramework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
    String tableName() default "";
    String foreignKeyNameOwn() default "";
    String foreignKeyNameOther() default "";
    Class classObject() default Void.class;
    String manyToManyTableName() default "";
}
