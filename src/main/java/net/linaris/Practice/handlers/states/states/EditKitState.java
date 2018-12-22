package net.linaris.Practice.handlers.states.states;

import org.bukkit.inventory.*;

import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.kits.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.utils.*;

public class EditKitState extends State
{
    private BuildModel build;
    
    public EditKitState(final OmegaPlayer player, final BuildModel build) {
        super("EditKit", player);
        this.build = build;
    }
    
    @Override
    public void onEnter() {
        this.getPlayer().sendMessage("§aVous commencez l'\u00e9dition du build: " + this.build.getName());
        this.getPlayer().sendMessage("§aPour confirmer l'\u00e9dition du kit \u00e9crivez \"save\" dans le chat");
        this.getPlayer().sendMessage("§aSi vous \u00eates §dVIP§a \u00e9crivez \"rename [nom]\" dans le chat pour renommer votre kit !");
        this.getPlayer().reset();
        final BuildItem[] items = BuildUtils.buildToItem(this.build);
        for (int i = 0; i < 36; ++i) {
            final BuildItem item = items[i];
            if (item != null) {
                final ItemStack itemStack = item.getItem().clone();
                itemStack.setAmount(1);
                this.getPlayer().getInventory().setItem(i, new NBTItem(itemStack).setInteger("build_id", item.getId()).getItem());
            }
        }
    }
    
    @Override
    public void onExit() {
    }
    
    public BuildModel getBuild() {
        return this.build;
    }
}
