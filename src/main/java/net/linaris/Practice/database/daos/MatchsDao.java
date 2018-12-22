package net.linaris.Practice.database.daos;

import java.sql.SQLException;
import java.util.List;

import net.linaris.Practice.database.CachedDao;
import net.linaris.Practice.database.models.MatchModel;
import net.linaris.Practice.database.models.PlayerSnapshot;
import net.linaris.Practice.database.redis.orm.NVField;
import net.linaris.Practice.database.redis.orm.RedisHelper;
import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.utils.InventoryStringDeSerializer;

public class MatchsDao extends CachedDao<MatchModel>
{
    public MatchsDao() throws SQLException {
        super(MatchModel.class);
    }
    
    public List<MatchModel> getMatchs(final Long userId) {
        return this.find(new NVField("players", userId));
    }
    
    public List<MatchModel> getMatchs(final Long userId, final int gameType) {
        return this.find(new NVField("players", userId), new NVField("gametype", gameType));
    }
    
    public MatchModel addSoloMatch(final int mapId, final int gameId, final boolean ranked, final OmegaPlayer winner, final OmegaPlayer loser) {
        final MatchModel model = new MatchModel(mapId, gameId, ranked);
        this.save(model);
        final PlayerSnapshot winnerSnap = new PlayerSnapshot(winner.getData().getId(), InventoryStringDeSerializer.toBase64(winner.getInventory().getContents()), (int)winner.getHealth(), winner.getFoodLevel(), true);
        RedisHelper.save(winnerSnap);
        final PlayerSnapshot loserSnap = new PlayerSnapshot(loser.getData().getId(), InventoryStringDeSerializer.toBase64(loser.getInventory().getContents()), 0, 0, false);
        RedisHelper.save(loserSnap);
        model.addPlayer(winnerSnap);
        model.addPlayer(loserSnap);
        return model;
    }
}
