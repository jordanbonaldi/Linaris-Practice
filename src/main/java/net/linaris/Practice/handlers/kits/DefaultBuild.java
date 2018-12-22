package net.linaris.Practice.handlers.kits;

public class DefaultBuild
{
    private String name;
    private BuildItem[] items;
    
    public DefaultBuild(final String name) {
        this.name = name;
        this.items = new BuildItem[36];
    }
    
    public Integer[] getItemsId() {
        final Integer[] ids = new Integer[36];
        for (int i = 0; i < this.items.length; ++i) {
            final BuildItem item = this.items[i];
            if (item != null) {
                ids[i] = item.getId();
            }
        }
        return ids;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public BuildItem[] getItems() {
        return this.items;
    }
    
    public void setItems(final BuildItem[] items) {
        this.items = items;
    }
}
