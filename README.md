# Java OR-Mapper for PostgreSQL - Jakob List

## General Info

To use this mapper you first need to create a database and adjust the parameters to connect to it in the **db.properties** file. A demo application is attached to this project which showcases the features of this OR-Mapper and to show how to use it.

This framework supports:

- Features CRUD operations
- 1:1, 1:n, n:m table relationships
- Tailored SQL Statements to call for certain data in the database

## Annotation

### Table

Table declared as `@Table(tableName = String)` is used to  annotate that a class is representing a table and has to be placed above the class specification. It is recommended to fill out the table name  to the desired name you want it to have in the database. If table name  is not declared then the name of the class is used.

### Field

Field declared as `@Field(fieldName = String, notNull =  boolean, unqiue = boolean)` is used to mark a field that should be  represented in the database. Field name is used to declare how the field should be called. If you want to have your fields not null or unique  you have to set notNull or unique to true. Unique and notNull are both  set to false as a standard.

### PrimaryKey

PrimaryKey delcared as `@PrimaryKey` is used to mark the primary key of the represented table.

### ForeignKey

ForeignKey declared as `@ForeignKey(fkTableName = String, foreignClass = Class)` is used to mark a foreign key. The fkTableName references the the table which is being referenced and the foreignClass the Class of the referenced object.

### OneToMany

OneToMany declared as `@OneToMany(tableName = String, foreignKeyName = String, classObject = Class)` is the counterpart to ForeignKey and is used for 1:n relationships. This is the annotation which showcases the relation between the ForeignKey annotated object and the OneToMany annotated object. The tableName references the table from which the object should be taken. The foreignKeyName references the foreign key name in the foreign key table. To create the objects afterwards the classObject is needed.

### ManyToMany

ManyToMany declared as `@ManyToMany(tableName = String, foreignKeyNameOwn = String, foreignKeyNameOther = String, classObject = Class, manyToManyTableName = String)` is needed to show n:m relationships. The tableName references the table of the object you want to get. ForeignKeyOwn and ForeignKeyOther reference the names of the foreign keys which are needed for the junction table. The classObject is used to create the right objects needed and the manyToManyTableName is used to create the junction table.

## Tailored SQL Statements

### Usage

This feature can be used to create custom SQL statements. It uses method chaining to make the statements syntax as closely as possible to a normal SQL statement.

### Example

```java
ArrayList<Student> student = Orm.query().SELECT().FROM(Student.class)
        .WHERE("name").LIKE("J%")
        .AND().WHERE("id").BETWEEN(0, 1)
        .AND().WHERE("inscribed").compare(QueryComparison.EQUAL, true)
        .executeQuery();
```

In this example you can see how the query is constructed. Firstly you have to start with calling the OR-Mapper and declare a new query with `Orm.query()`. Secondly you define a SELECT statement and which class you want to select from with `SELECT().FROM('Desired class')`. Next you can chain your logical operations. In this example we first look for a student which name starts with a "J" with `WHERE("name").LIKE("J%")`. Then we chain another logical operation with AND and look for every entry that has come back and check if the id is between 0 and 1 with `AND().WHERE("id").BETWEEN(0,1)`. Here you can use integers and strings for example to check. As the last logical operation we look if the student is inscribed with `AND().WHERE("inscribed").compare(QueryComparison.EQUAL, true)`. Compare can be used to use comparisons. It supports: 

- EQUAL (=)
- GREATER (>)
- LESS (<)
- GREATER_OR_EQUAL (>=)
- LESS_OR_EQUAL (<=)
- NOT_EQUAL (<>)

To complete the query you have to put in the thing you want to compare it to in this example a boolean. If you are done with the query you write `executeQuery()`.
