package connector;

import java.net.InetAddress
import com.typesafe.config.ConfigFactory
import com.websudos.phantom.connectors.{ContactPoint, ContactPoints}
import scala.collection.JavaConversions.asScalaBuffer

object Connector {
  val config = ConfigFactory.load()

  val hosts = config.getStringList("cassandra.host")
  val inets = asScalaBuffer(hosts).map(InetAddress.getByName)

  val keyspace: String = config.getString("cassandra.keyspace")

  lazy val connector = ContactPoints(hosts).withClusterBuilder(
    _.withCredentials(
      config.getString("cassandra.username"),
      config.getString("cassandra.password")
    )
  ).keySpace(keyspace)
}