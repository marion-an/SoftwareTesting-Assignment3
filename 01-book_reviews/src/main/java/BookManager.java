import java.util.List;
import static java.util.stream.Collectors.toList;

public class BookManager {

    private final BookRatingsFetcher ratingsFetcher;
    private final BookAuthorsFetcher authorsFetcher;

    public BookManager(BookRatingsFetcher ratingsFetcher, BookAuthorsFetcher authorsFetcher) {
        this.ratingsFetcher = ratingsFetcher;
        this.authorsFetcher = authorsFetcher; }

    public List<Book> highRatedBooks() {
        List<Book> allBooks = ratingsFetcher.all();
        return allBooks.stream()
                .filter(book -> book.getRating() >= 4)
                .collect(toList());
    }

    public List<String> uniqueAuthors() {
        List<String> allAuthors  = authorsFetcher.all();
        return allAuthors.stream()
                .distinct()
                .collect(toList());
    }
}
