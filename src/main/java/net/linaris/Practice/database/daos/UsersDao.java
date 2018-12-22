package net.linaris.Practice.database.daos;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import net.linaris.Practice.database.CachedDao;
import net.linaris.Practice.database.models.UserModel;

public class UsersDao extends CachedDao<UserModel>
{
    public UsersDao() throws SQLException {
        super(UserModel.class);
    }
    
    public UserModel getUser(final UUID uuid) {
        return this.getUser(uuid.toString());
    }
    
    public UserModel getUser(final String uuid) {
        final Optional<UserModel> model = this.findOne("uuid", uuid, new String[0]);
        if (!model.isPresent()) {
            final UserModel user = new UserModel(uuid, "Unknown");
            this.save(user);
            return user;
        }
        return model.get();
    }
    
    public UserModel getUserByUser(final String uuid) {
        final Optional<UserModel> model = this.findOne("uuid", uuid, new String[0]);
        if (!model.isPresent()) {
            final UserModel user = new UserModel(uuid, "Unknown");
            this.save(user);
            return user;
        }
        return model.get();
    }
}
