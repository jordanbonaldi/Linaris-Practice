package net.linaris.Practice.database.redis.orm;

public class MissingIdException extends RuntimeException
{
    private static final long serialVersionUID = 431576167757996845L;
    
    public MissingIdException() {
    }
    
    public MissingIdException(final String message) {
        super(message);
    }
}
