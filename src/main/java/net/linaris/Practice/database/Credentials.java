package net.linaris.Practice.database;

public final class Credentials
{
    private final String host;
    private final String username;
    private final String password;
    private final int database;
    
    public Credentials(final String host, final String password, final int database) {
        this(host, null, password, database);
    }
    
    public Credentials(final String host, final String username, final String password) {
        this(host, username, password, 0);
    }
    
    public Credentials(final String host, final String username, final String password, final int database) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public int database() {
        return this.database;
    }
}
