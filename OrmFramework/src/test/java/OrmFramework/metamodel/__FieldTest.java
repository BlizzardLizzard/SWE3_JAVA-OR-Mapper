package OrmFramework.metamodel;

import org.junit.Assert;
import org.junit.Before;

import java.util.ArrayList;

public class __FieldTest {
    __TableObject __table;

    @Before
    public void __TestObjectCreation(){
        Test __test = new Test();
        __test.id = 1;
        __test.courseName = "Test";
        __table = new __TableObject(__test);
    }

    @org.junit.Test
    public void fieldTypeTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 0){
                Assert.assertEquals(Integer.class, _fields.get(i).get_fieldType());
            }
            if(i == 1){
                Assert.assertEquals(String.class, _fields.get(i).get_fieldType());
            }
            if(i == 2){
                Assert.assertEquals(Test.class, _fields.get(i).get_fieldType());
            }
            if(i == 3){
                Assert.assertEquals(ArrayList.class, _fields.get(i).get_fieldType());
            }
            if(i == 4){
                Assert.assertEquals(ArrayList.class, _fields.get(i).get_fieldType());
            }
        }
    }

    @org.junit.Test
    public void isFieldTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 0 || i == 1 || i == 2){
                Assert.assertTrue(_fields.get(i).is_field());
            } else {
                Assert.assertFalse(_fields.get(i).is_field());
            }
        }
    }

    @org.junit.Test
    public void fieldNameTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 0){
                Assert.assertEquals("id", _fields.get(i).get_fieldName());
            }
            if(i == 1){
                Assert.assertEquals("courseName", _fields.get(i).get_fieldName());
            }
            if(i == 2){
                Assert.assertEquals("test", _fields.get(i).get_fieldName());
            }
            if(i == 3){
                Assert.assertEquals("teachers", _fields.get(i).get_fieldName());
            }
            if(i == 4){
                Assert.assertEquals("students", _fields.get(i).get_fieldName());
            }
        }
    }

    @org.junit.Test
    public void fieldValueTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 0){
                Assert.assertEquals(1, _fields.get(i).get_fieldValue());
            }
            if(i == 1){
                Assert.assertEquals("Test", _fields.get(i).get_fieldValue());
            }
        }
    }

    @org.junit.Test
    public void primaryKeyTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 0){
                Assert.assertTrue(_fields.get(i).is_primaryKey());
            } else {
                Assert.assertFalse(_fields.get(i).is_primaryKey());
            }
        }
    }

    @org.junit.Test
    public void foreignKeyTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 2){
                Assert.assertTrue(_fields.get(i).is_foreignKey());
            } else {
                Assert.assertFalse(_fields.get(i).is_foreignKey());
            }
        }
    }

    @org.junit.Test
    public void oneToManyTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 4){
                Assert.assertTrue(_fields.get(i).is_oneToMany());
            } else {
                Assert.assertFalse(_fields.get(i).is_oneToMany());
            }
        }
    }

    @org.junit.Test
    public void manyToManyTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 3){
                Assert.assertTrue(_fields.get(i).is_manyToMany());
            } else {
                Assert.assertFalse(_fields.get(i).is_manyToMany());
            }
        }
    }

    @org.junit.Test
    public void annotationListSizeTest(){
        Assert.assertEquals(2, __table.get_fields().get(0).get_annotationList().size());
        Assert.assertEquals(1, __table.get_fields().get(1).get_annotationList().size());
        Assert.assertEquals(2, __table.get_fields().get(2).get_annotationList().size());
        Assert.assertEquals(1, __table.get_fields().get(3).get_annotationList().size());
        Assert.assertEquals(1, __table.get_fields().get(4).get_annotationList().size());
    }

    @org.junit.Test
    public void getAnnotationFieldValueTest(){
        ArrayList<__Field> _fields = __table.get_fields();
        for(int i = 0; i < _fields.size(); i++){
            if(i == 0){
                Assert.assertEquals("id",__Field.getAnnotationFieldValue(_fields.get(i), "fieldName"));
                Assert.assertTrue((Boolean) __Field.getAnnotationFieldValue(_fields.get(i), "notNull"));
                Assert.assertTrue((Boolean) __Field.getAnnotationFieldValue(_fields.get(i), "unique"));
            }
            if(i == 1){
                Assert.assertEquals("name",__Field.getAnnotationFieldValue(_fields.get(i), "fieldName"));
            }
            if(i == 2){
                Assert.assertEquals("testid",__Field.getAnnotationFieldValue(_fields.get(i), "fieldName"));
                Assert.assertEquals(Test.class, __Field.getAnnotationFieldValue(_fields.get(i), "foreignClass"));
                Assert.assertEquals("test", __Field.getAnnotationFieldValue(_fields.get(i), "fKTableName"));
            }
            if(i == 3){
                Assert.assertEquals("teachers",__Field.getAnnotationFieldValue(_fields.get(i), "tableName"));
                Assert.assertEquals("courseid",__Field.getAnnotationFieldValue(_fields.get(i), "foreignKeyNameOwn"));
                Assert.assertEquals("teacherid",__Field.getAnnotationFieldValue(_fields.get(i), "foreignKeyNameOther"));
                Assert.assertEquals(Test.class ,__Field.getAnnotationFieldValue(_fields.get(i), "classObject"));
                Assert.assertEquals("teacher_course",__Field.getAnnotationFieldValue(_fields.get(i), "manyToManyTableName"));
            }
        }
    }
}
