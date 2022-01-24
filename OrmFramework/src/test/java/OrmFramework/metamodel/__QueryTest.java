package OrmFramework.metamodel;

import OrmFramework.queryEnums.QueryComparison;
import org.junit.Assert;
import org.junit.Before;

public class __QueryTest {
    __Query _query;

    @Before
    public void __QueryCreation(){
        _query = new __Query();
    }

    @org.junit.Test
    public void whereTest(){
        Assert.assertEquals("WHERE id AND name", _query.WHERE("id").AND().WHERE("name").getQueryString().toString());
    }

    @org.junit.Test
    public void compareTest(){
        Assert.assertEquals(" = 'test'", _query.compare(QueryComparison.EQUAL, "test").getQueryString().toString());
        _query.getQueryString().delete(0, _query.getQueryString().toString().length());
        Assert.assertEquals(" > 'test'", _query.compare(QueryComparison.GREATER, "test").getQueryString().toString());
        _query.getQueryString().delete(0, _query.getQueryString().toString().length());
        Assert.assertEquals(" >= 'test'", _query.compare(QueryComparison.GREATER_OR_EQUAL, "test").getQueryString().toString());
        _query.getQueryString().delete(0, _query.getQueryString().toString().length());
        Assert.assertEquals(" < 'test'", _query.compare(QueryComparison.LESS, "test").getQueryString().toString());
        _query.getQueryString().delete(0, _query.getQueryString().toString().length());
        Assert.assertEquals(" <= 'test'", _query.compare(QueryComparison.LESS_OR_EQUAL, "test").getQueryString().toString());
        _query.getQueryString().delete(0, _query.getQueryString().toString().length());
        Assert.assertEquals(" <> 'test'", _query.compare(QueryComparison.NOT_EQUAL, "test").getQueryString().toString());
    }
}
