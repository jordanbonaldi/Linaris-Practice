package net.linaris.Practice.database.redis.orm;

public class NVField
{
    private String attributeName;
    private Object attributeValue;
    
    public NVField(final String attributeName, final Object attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
    
    public String getAttributeName() {
        return this.attributeName;
    }
    
    public Object getAttributeValue() {
        return this.attributeValue;
    }
}
