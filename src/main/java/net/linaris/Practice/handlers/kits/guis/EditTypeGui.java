package net.linaris.Practice.handlers.kits.guis;

import java.util.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.kits.items.*;
import net.linaris.Practice.utils.specialitems.*;

public class EditTypeGui extends VirtualMenu
{
    public EditTypeGui() {
        super("Mode de jeu ?", 1);
        for (final GameType type : GameTypesManager.getTypes().values()) {
            this.addAndRegisterItem(new EditGameTypeItem(type), type.getId());
        }
    }
}
