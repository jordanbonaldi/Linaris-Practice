package net.linaris.Practice.database.redis.orm;

public class JOhmException extends RuntimeException
{
    private static final long serialVersionUID = 5824673432789607128L;
    
    public JOhmException(final Exception e) {
        super(e);
    }
    
    public JOhmException(final String message) {
        super(message);
    }
}
