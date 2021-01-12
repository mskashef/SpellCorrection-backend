import java.util.concurrent.atomic.AtomicInteger;

public class Document
{
    private static final AtomicInteger lastId = new AtomicInteger(0);

    private final int docId;
    private final String body;
    private final String name;

    public Document(String name, String body) {
        this.name = name;
        this.body = body;
        this.docId = lastId.incrementAndGet();
    }

    public int getDocId() {
        return docId;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Document{" +
                "docId=" + docId +
                ", name='" + name + '\'' +
                '}';
    }
}
