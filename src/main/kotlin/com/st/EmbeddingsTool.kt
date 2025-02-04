package com.st

import dev.langchain4j.data.document.Metadata
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class EmbeddingsTool(
    private val embeddingModel: EmbeddingModel
) {

    fun convert(rss: Rss): RssEmbeddings {
        val rssEmbeddings = RssEmbeddings.newBuilder()
            .setTitle(rss.title)
            .setDescription(rss.description)
            .setLink(rss.link)
            .setPubDate(rss.pubDate)
            .setCategory(rss.category)
            .setEmbeddings(generate(rss))
            .build()
        return rssEmbeddings
    }

    fun generate(rss: Rss): List<Float> {
        val metas: Map<String, String> = java.util.Map.of(
            "title", rss.title,
            "link", rss.link,
            "pubDate", rss.pubDate,
            "category", rss.category
        )
        val segment = TextSegment(rss.description, Metadata(metas))
        return embeddingModel.embed(segment).content().vectorAsList()
    }
}