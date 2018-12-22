package net.linaris.Practice.handlers.players.procedure;

import gnu.trove.procedure.*;

public interface OmegaProcedure<T> extends TObjectProcedure<T>
{
    boolean execute(T p0);
}
