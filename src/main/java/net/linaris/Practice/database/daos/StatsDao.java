package net.linaris.Practice.database.daos;

import java.sql.SQLException;
import java.util.Optional;

import net.linaris.Practice.database.CachedDao;
import net.linaris.Practice.database.models.StatsModel;
import net.linaris.Practice.database.redis.orm.NVField;

public class StatsDao extends CachedDao<StatsModel>
{
    public StatsDao() throws SQLException {
        super(StatsModel.class);
    }
    
    public StatsModel getStats(final Long userId, final int gameId) {
        final Optional<StatsModel> model = this.findOne(new NVField("userId", userId), new NVField("gametype", gameId));
        if (!model.isPresent()) {
            final StatsModel stats = new StatsModel(userId, gameId);
            this.save(stats);
            return stats;
        }
        return model.get();
    }
}
