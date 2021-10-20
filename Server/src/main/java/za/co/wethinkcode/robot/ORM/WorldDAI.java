package za.co.wethinkcode.robot.ORM;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;

public interface WorldDAI extends BaseQuery {

    /**
     * Reads a row of a world that was stored in the DB if it exists.
     * @param name String
     * @return WorldDO
     */
    @Select(
        "SELECT * "
                + "FROM worlds "
                + "WHERE name = ?{1}"
    ) WorldDO readWorld(String name);

    /**
     * Saves a new row to a table.
     * @param data String
     * @param name String
     * @param size int
     */
    @Update(
            "INSERT INTO worlds (data, name, size) "
                    + "VALUES (?{1}, ?{2}, ?{3})"
    ) void saveWorld(String data, String name, int size);
}
