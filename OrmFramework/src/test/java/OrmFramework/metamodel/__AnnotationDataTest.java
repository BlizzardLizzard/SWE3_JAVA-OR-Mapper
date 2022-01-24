package OrmFramework.metamodel;

import org.junit.Assert;
import org.junit.Before;

import java.util.ArrayList;

public class __AnnotationDataTest {
    __TableObject __table;

    @Before
    public void __TestObjectCreation(){
        Test __test = new Test();
        __table = new __TableObject(__test);
    }

    @org.junit.Test
    public void annotationTypeTest(){
        int index = 0;
        for(__Field _field : __table.get_fields()){
            for(__AnnotationData _annotationData : _field.get_annotationList()){
                if(index == 0){
                    Assert.assertEquals("interface OrmFramework.annotations.PrimaryKey", _annotationData.get_annotationType().toString());
                }
                if(index == 1 || index == 2 || index == 4){
                    Assert.assertEquals("interface OrmFramework.annotations.Field", _annotationData.get_annotationType().toString());
                }
                if(index == 3){
                    Assert.assertEquals("interface OrmFramework.annotations.ForeignKey", _annotationData.get_annotationType().toString());
                }
                if(index == 5){
                    Assert.assertEquals("interface OrmFramework.annotations.ManyToMany", _annotationData.get_annotationType().toString());
                }
                if(index == 6){
                    Assert.assertEquals("interface OrmFramework.annotations.OneToMany", _annotationData.get_annotationType().toString());
                }
                index ++;
            }
        }
    }
}
