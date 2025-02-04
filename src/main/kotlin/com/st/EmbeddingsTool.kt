package com.st

import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.DocumentSplitter
import dev.langchain4j.data.document.Metadata
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel
import jakarta.enterprise.context.ApplicationScoped
import org.jboss.logging.Logger
import java.lang.String.join


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
        // Join with double lineSeparator (\n\n) in order to leverage DocumentByParagraphSplitter
        val content: String = join(System.lineSeparator() + System.lineSeparator(),
            listOf(rss.title,rss.description))

        val document = Document(content, Metadata(metas))
        val splitter: DocumentSplitter = DocumentByParagraphSplitter(800, 50)

        val segments = splitter.split(document)
        LOGGER.infof("The document was split as %s chunks", segments.size)
        val embeddings: List<Embedding> = embeddingModel.embedAll(segments).content()
        return embeddings.get(0).vectorAsList()
//        val segment = TextSegment(rss.description, Metadata(metas))
//        return embeddingModel.embed(segment).content().vectorAsList()
    }

    companion object {
        private val LOGGER: Logger = Logger.getLogger(EmbeddingsTool::class.java)
    }
}