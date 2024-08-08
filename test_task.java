import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 * -------------------------------------------------------------------------------------------------
 * @author Ruslan Chekina
 * @version 0.1
 * @since 2024-08-06
 */
public class DocumentManager {
    private final AtomicLong idCounter = new AtomicLong();
    private final List<Document> documents = new ArrayList<>();
    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() = null) {
            document.setId(String.valueOf(idCounter.incrementAndGet()));
            document.setCreated(Instant.now())
        } else {
            documents.removeIf(doc -> doc.getId().equals(document.getId()));
        }
        documents.add(document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        return documents.stream()
                .filter(document ->
                        (request.getTitlePrefixes() == null || request.getTitlePrefixes().stream().anyMatch(prefix -> document.getTitle().startsWith(prefix))) &&
                                (request.getContainsContents() == null || request.getContainsContents().stream().anyMatch(content -> document.getContent().contains(content))) &&
                                (request.getAuthorIds() == null || request.getAuthorIds().contains(document.getAuthor().getId())) &&
                                (request.getCreatedFrom() == null || !document.getCreated().isBefore(request.getCreatedFrom())) &&
                                (request.getCreatedTo() == null || !document.getCreated().isAfter(request.getCreatedTo()))
                ).collect(Collectors.toList());
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return documents.stream()
                .filter(document -> document.getId().equals(id))
                .findFirst();
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}