package com.st

import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.jboss.logging.Logger

@Path("/embeddings")
class EmbeddingsResource(
    private val embeddingsService: EmbeddingsService
) {

    @Channel("rss-embeddings")
    var emitter: Emitter<RssEmbeddings>? = null

    @POST
    fun enqueueRss(rss: Rss): Response {
        LOGGER.infof("Sending rss %s to Kafka", rss.title)
        val rssEmbeddings = embeddingsService.convert(rss)
        emitter!!.send(rssEmbeddings)
        return Response.accepted().build()
    }

    @Incoming("rss-feed")
    fun rssFeed(rss: Rss){
        val rssEmbeddings = embeddingsService.convert(rss)
        emitter!!.send(rssEmbeddings)
    }

    companion object {
        private val LOGGER: Logger = Logger.getLogger(EmbeddingsResource::class.java)
    }
}