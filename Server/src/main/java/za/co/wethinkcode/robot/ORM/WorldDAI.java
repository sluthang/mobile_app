package za.co.wethinkcode.robot.ORM;

import java.util.List;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;

public interface WorldDAI extends BaseQuery {

    /**
     * fetches a the size and data of a world that was stored in the DB.
     * @param name String
     * @return WorldDO
     */
    @Select(
        "SELECT * "
                + "FROM worlds "
                + "WHERE name = ?{1}"
    )
    WorldDO getWorldSize(String name);
}
