package OrmFramework.metamodel;

import org.junit.Assert;
import org.junit.Before;

public class __TableObjectTest {

    __TableObject __table;

    @Before
    public void __TestObjectCreation(){
        Test __test = new Test();
        __table = new __TableObject(__test);
    }

    @org.junit.Test
    public void tableNameTest(){
        Assert.assertEquals("test", __table.get_tableName());
    }

    @org.junit.Test
    public void tableFieldSizeTest(){
        Assert.assertEquals(5, __table.get_fields().size());
    }

    @org.junit.Test
    public void tableGetPkFieldTest(){
        __Field __pkField = null;
        for(__Field _field : __table.get_fields()){
            if(_field.is_primaryKey()){
                __pkField = _field;
            }
        }
        Assert.assertEquals(__pkField, __TableObject.getPkField(__table));
    }
}
