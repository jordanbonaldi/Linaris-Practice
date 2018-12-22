package net.linaris.Practice.handlers.kits;

public enum WeaponName
{
    EZ("Ez :p");
    
    private String name;
    
    private WeaponName(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
