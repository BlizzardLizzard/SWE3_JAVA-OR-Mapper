# Java OR-Mapper for PostgreSQL - Jakob List

## General Info

To use this mapper you first need to create a database and adjust the parameters to connect to it in the **db.properties** file. A demo application is attached to this project which showcases the features of this OR-Mapper and to show how to use it.

This framework supports:

- Creating tables from classes
- Saving objects in the database
- Updating objects in the database
- Loading objects from the database
- Tailored SQL Statements to call for certain data in the database
- 1:1, 1:n, n:m table relationships

## Annotations

### Table

Table declared as "@Table(tableName = String)" is used to annotate that a class is representing a table and has to be placed above the class specification. It is recommended to fill out the table name to the desired name you want it to have in the database. If table name is not declared then the name of the class is used. 

### Field

Field declared as "@Field(fieldName = String, notNull = boolean, unqiue = boolean)" is used to mark a field that should be represented in the database. Field name is used to declare how the field should be called. If you want to have your fields not null or unique you have to set notNull or unique to true. Unique and notNull are both set to false as a standard.

### PrimaryKey

PrimaryKey delcared as "@PrimaryKey" is used to mark the primary key of the represented table. 

### ForeignKey

ForeignKey declared as "@ForeignKey(fkTableName = String, foreignClass = Class)" is used to mark a foreign key. 
