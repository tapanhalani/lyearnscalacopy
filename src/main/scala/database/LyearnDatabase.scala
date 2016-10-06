package database;

import connector.Connector.connector
import user.UserModel
import com.websudos.phantom.database.DatabaseImpl
import com.websudos.phantom.dsl.KeySpaceDef


class LyearnDatabase(override val connector: KeySpaceDef) extends DatabaseImpl(connector) {
  object userModel extends UserModel with connector.Connector
}

object ProductionDb extends LyearnDatabase(connector)