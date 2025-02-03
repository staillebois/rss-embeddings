package com.st

import dev.langchain4j.data.document.Metadata
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class Embeddings(
    private val embeddingModel: EmbeddingModel
) {

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