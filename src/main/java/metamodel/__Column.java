package metamodel;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

public class __Column {


    /** Table */
    @Getter
    private __Table _table;

    /** Column name */
    @Getter
    private String _name;

    /** Get method */
    @Getter
    private Method _get;

    /** Set method */
    @Getter
    private Method _set;

    /** Column type */
    @Getter
    @Setter
    private Class _columnType;

    /** Column name */
    @Getter
    @Setter
    private String _columnName;

    /** Primary key declaration */
    @Getter
    @Setter
    private boolean _isPK = false;

    /** Foreign key declaration */
    @Getter
    @Setter
    private boolean _isFK = false;

    /** Field type */
    @Getter
    @Setter
    private Class _fieldType;

    /** Nullable declaration */
    @Getter
    @Setter
    private boolean _isNullable = false;

    /** Creates new Column
     *
     * @param table Parent table
     * @param name Column name
     */
    public __Column(__Table table, String name){
        _table = table;
        _name = name;
    }

    /** Set get method
     *
     * @param value Method
     */
    public void setGetMethod(Method value){
        _get = value;
        _get.setAccessible(true);
    }

    /** Set set method
     *
     * @param value Method
     */
    public void setSetMethod(Method value){
        _set = value;
        _set.setAccessible(true);
    }

    public Class getType(){
        if(_get != null) {
            return _get.getReturnType();
        }
        return _set.getParameters()[0].getType();
    }

}
