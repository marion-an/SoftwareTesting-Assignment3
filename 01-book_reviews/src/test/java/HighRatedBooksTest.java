import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HighRatedBooksTest {

    @Mock BookRatingsFetcher ratingsFetcher;
    @Mock BookAuthorsFetcher authorsFetcher;

    @Test
    void testEmptyDatabase() {
        BookManager ListOfBooks = new BookManager(ratingsFetcher, authorsFetcher);

        List<Book> books = new ArrayList<Book>();
        when(ratingsFetcher.all()).thenReturn(books);

        books = ListOfBooks.highRatedBooks();

        assertEquals(0, books.size());
        verify(ratingsFetcher, times(1)).all();
    }

    @Test
    void testLowRatingElementInDatabase() {
        BookManager ListOfBooks = new BookManager(ratingsFetcher, authorsFetcher);

        Book book1 = new Book("book 1", 1, "Author1");
        Book book2 = new Book("book 2", 2, "Author2");
        Book book3 = new Book("book 3", 3, "Author3");

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        when(ratingsFetcher.all()).thenReturn(books);

        books = ListOfBooks.highRatedBooks();

        assertEquals(0, books.size());
        verify(ratingsFetcher, times(1)).all();
    }

    @Test
    void testHighRatingElementInDatabase() {
        BookManager ListOfBooks = new BookManager(ratingsFetcher, authorsFetcher);

        Book book4 = new Book("book 4", 4, "Author4");
        Book book5 = new Book("book 5", 5, "Author5");

        List<Book> books = new ArrayList<>();
        books.add(book4);
        books.add(book5);
        when(ratingsFetcher.all()).thenReturn(books);

        books = ListOfBooks.highRatedBooks();

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book4);
        expectedBooks.add(book5);

        assertTrue(books.size() == expectedBooks.size() && books.containsAll(expectedBooks) && expectedBooks.containsAll(books));
        verify(ratingsFetcher, times(1)).all();
    }

    @Test
    void testMixedElementInDatabase() {
        BookManager ListOfBooks = new BookManager(ratingsFetcher, authorsFetcher);

        Book book1 = new Book("book 1", 1, "Author1");
        Book book2 = new Book("book 2", 2, "Author2");
        Book book3 = new Book("book 3", 3, "Author3");
        Book book4 = new Book("book 4", 4, "Author4");
        Book book5 = new Book("book 5", 5, "Author5");


        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        books.add(book5);
        when(ratingsFetcher.all()).thenReturn(books);

        books = ListOfBooks.highRatedBooks();

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book4);
        expectedBooks.add(book5);

        assertTrue(books.size() == expectedBooks.size() && books.containsAll(expectedBooks) && expectedBooks.containsAll(books));
        verify(ratingsFetcher, times(1)).all();
    }





}

