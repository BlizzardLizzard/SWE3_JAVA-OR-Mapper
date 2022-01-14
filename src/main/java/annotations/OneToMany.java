package annotations;

public @interface OneToMany {
    String tableName() default "";
}
