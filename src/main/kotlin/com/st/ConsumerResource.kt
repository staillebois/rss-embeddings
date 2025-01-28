package com.st

import io.smallrye.mutiny.Multi
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.reactive.messaging.Channel
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestStreamElementType

@ApplicationScoped
@Path("/rss-feeds")
class ConsumerResource {
    @Channel("rss-feeds")
    var rssFeeds: Multi<Rss>? = null

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    fun stream(): Multi<String> {
        LOGGER.info("Waiting stream")
        return rssFeeds!!.map { rss: Rss ->
            java.lang.String.format(
                "'%s' from %s",
                rss.title,
                rss.pubDate
            )
        }
    }

    companion object {
        private val LOGGER: Logger = Logger.getLogger(ConsumerResource::class.java)
    }
}