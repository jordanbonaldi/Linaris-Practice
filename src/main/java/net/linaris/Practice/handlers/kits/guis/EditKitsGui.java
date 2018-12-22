package net.linaris.Practice.handlers.kits.guis;

import java.util.*;

import net.linaris.Practice.database.*;
import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.kits.items.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.specialitems.*;

public class EditKitsGui extends VirtualMenu
{
    private OmegaPlayer player;
    private List<BuildModel> builds;
    private GameType type;
    
    public EditKitsGui(final OmegaPlayer player, final GameType type) {
        super("Menu des builds", 1);
        this.type = type;
        this.builds = DatabaseConnector.getBuildsDao().getBuilds(player.getData().getId(), type.getId());
        int slot = 0;
        for (final BuildModel build : this.builds) {
            this.addAndRegisterItem(new EditBuildItem(type, build), slot);
            ++slot;
        }
        if (this.builds.size() < 5) {
            if (player.getData().getRank().getVipLevel() >= 1 || this.builds.isEmpty()) {
                this.addAndRegisterItem(new AddBuildItem(type), slot);
            }
            else {
                this.addAndRegisterItem(new AddNoDonatorBuildItem(type), slot);
            }
        }
    }
}
