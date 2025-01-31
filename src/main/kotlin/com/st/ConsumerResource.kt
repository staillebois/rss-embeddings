package com.st

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.jboss.logging.Logger

@ApplicationScoped
class ConsumerResource {

    @Incoming("rss-feed")
    fun rssFeed(rss: Rss){
        LOGGER.info(java.lang.String.format(
            "'%s' from %s",
            rss.title,
            rss.pubDate
        ))
    }

    companion object {
        private val LOGGER: Logger = Logger.getLogger(ConsumerResource::class.java)
    }
}