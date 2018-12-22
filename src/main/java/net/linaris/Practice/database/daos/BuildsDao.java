package net.linaris.Practice.database.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import net.linaris.Practice.database.CachedDao;
import net.linaris.Practice.database.models.BuildModel;
import net.linaris.Practice.database.redis.orm.NVField;

public class BuildsDao extends CachedDao<BuildModel>
{
    public BuildsDao() throws SQLException {
        super(BuildModel.class);
    }
    
    public List<BuildModel> getBuilds(final Long userId, final int gameId) {
        return this.find(new NVField("userId", userId), new NVField("gametype", gameId));
    }
    
    public Optional<BuildModel> getBuild(final Long id) {
        return Optional.ofNullable(this.get(id, new String[0]));
    }
    
    public BuildModel createBuild(final Long userId, final String name, final int gameId, final Integer[] items) {
        final BuildModel build = new BuildModel(userId, name, gameId);
        build.setItems(items);
        this.save(build);
        return build;
    }
}
