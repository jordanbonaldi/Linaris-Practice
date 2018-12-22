package net.linaris.Practice.handlers.kits;

import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.games.*;

public class BuildUtils
{
    public static BuildItem[] buildToItem(final BuildModel build) {
        final BuildItem[] items = new BuildItem[36];
        final GameType type = GameTypesManager.getIdToTypes().get(build.getGametype());
        if (type == null) {
            return items;
        }
        for (int i = 0; i < 36; ++i) {
            if (build.getItems()[i] != null) {
                items[i] = type.getItem(build.getItems()[i]);
            }
        }
        return items;
    }
}
