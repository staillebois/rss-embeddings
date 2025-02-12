package com.st

import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger

@Path("/embeddings")
class EmbeddingsResource(
    private val embeddingsService: EmbeddingsService,
    private val embeddingsChannel: EmbeddingsChannel
) {

    @POST
    fun enqueueRss(rss: Rss): Response {
        LOGGER.infof("Sending rss %s to Kafka", rss.title)
        val rssEmbeddings = embeddingsService.convert(rss)
        embeddingsChannel.send(rssEmbeddings)
        return Response.accepted().build()
    }

    companion object {
        private val LOGGER: Logger = Logger.getLogger(EmbeddingsResource::class.java)
    }
}