package OrmFramework.metamodel;

import OrmFramework.Orm;
import OrmFramework.annotations.Table;
import OrmFramework.queryEnums.QueryComparison;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * This class generates a custom query
 */
public class __Query{

    /**
     * Type
     */
    @Getter
    private Class<?> cls;

    /**
     * Query string
     */
    @Getter
    private StringBuilder queryString = new StringBuilder();

    /**
     * ArrayList of objects fetched from database
     */
    @Getter
    @Setter
    private ArrayList<Object> objectList = new ArrayList<>();

    /**
     * Counts the times when has been used to avoid more than one where
     */
    private int whereCount= 0;

    /**
     * Starts a SELECT statement
     * @return Type __Query
     */
    public __Query SELECT(){
        queryString.append("SELECT * ");
        return this;
    }

    /**
     * Defines which table is called
     * @param cls Type Class<?>
     * @return Type __Query
     */
    public __Query FROM(Class<?> cls){
        this.cls = cls;
        queryString.append("FROM " + cls.getAnnotation(Table.class).tableName() + " ");
        return this;
    }

    /**
     * Starts a where comparison
     * @param condition Type String
     * @return Type __Query
     */
    public __Query WHERE(String condition){
        if(whereCount == 0){
            queryString.append("WHERE ");
        }
        whereCount++;
        queryString.append(condition);
        return this;
    }

    /**
     * Finishes the comparison equal to the defined enum
     * @param _queryComparison Type QueryComparison (ENUM)
     * @param value Type Object
     * @return Type __Query
     */
    public __Query compare(QueryComparison _queryComparison, Object value){
        switch (_queryComparison){
            case EQUAL -> queryString.append(" = ");
            case GREATER -> queryString.append(" > ");
            case LESS -> queryString.append(" < ");
            case GREATER_OR_EQUAL -> queryString.append(" >= ");
            case LESS_OR_EQUAL -> queryString.append(" <= ");
            case NOT_EQUAL -> queryString.append(" <> ");
        }
        whereCount++;
        queryString.append("'" + value + "'");
        return this;
    }

    /**
     * Adds an and operation to the query
     * @return Type __Query
     */
    public __Query AND(){
        queryString.append(" AND ");
        return this;
    }

    /**
     * Add a not operation to the query
     * @return Type __Query
     */
    public __Query NOT(){
        queryString.append(" NOT ");
        return this;
    }

    /**
     * Adds an or operation to the query
     * @return Type __Query
     */
    public __Query OR(){
        queryString.append(" OR ");
        return this;
    }

    /**
     * Adds a between operation to the query
     * @param begin Type Object
     * @param end Type Object
     * @return Type __Query
     */
    public __Query BETWEEN(Object begin, Object end){
        queryString.append(" BETWEEN ");
        queryString.append("'" + begin + "' AND '" + end + "'");
        return this;
    }

    /**
     * Adds a like operation to the query
     * @param pattern Type String
     * @return Type __Query
     */
    public __Query LIKE(String pattern){
        queryString.append(" LIKE ");
        queryString.append("'" + pattern + "'");
        return this;
    }

    /**
     * Executes the query and calls teh query which puts the objects into the ArrayList
     * @return Object of wanted class
     */
    public <T> T executeQuery(){
        Orm.executeQuery(this);
        return (T) objectList;
    }
}
