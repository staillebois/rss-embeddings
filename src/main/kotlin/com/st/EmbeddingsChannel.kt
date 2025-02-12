package com.st

import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Incoming

class EmbeddingsChannel(
    private val embeddingsService: EmbeddingsService
) {

    @Channel("rss-embeddings")
    var emitter: Emitter<RssEmbeddings>? = null

    @Incoming("rss-feed")
    fun rssFeed(rss: Rss){
        val rssEmbeddings = embeddingsService.convert(rss)
        send(rssEmbeddings)
    }

    fun send(rssEmbeddings: RssEmbeddings){
        emitter!!.send(rssEmbeddings)
    }
}