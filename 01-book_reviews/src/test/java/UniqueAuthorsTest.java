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
class UniqueAuthorsTest {

    @Mock BookRatingsFetcher ratingsFetcher;
    @Mock BookAuthorsFetcher authorsFetcher;

    @Test
    void testEmptyDatabase() {
        BookManager ListOfBooks = new BookManager(ratingsFetcher, authorsFetcher);

        List<String> authors = new ArrayList<String>();
        when(authorsFetcher.all()).thenReturn(authors);

        authors = ListOfBooks.uniqueAuthors();

        assertEquals(0, authors.size());
        verify(authorsFetcher, times(1)).all();
    }

    @Test
    void testOneAuthor() {
        BookManager ListOfBooks = new BookManager(ratingsFetcher, authorsFetcher);

        List<String> authors = new ArrayList<String>();
        authors.add("Author1");
        when(authorsFetcher.all()).thenReturn(authors);


        authors = ListOfBooks.uniqueAuthors();

        List<String> expectedAuthors = new ArrayList<String>();
        expectedAuthors.add("Author1");

        assertEquals(1, authors.size());
        assertTrue(authors.containsAll(expectedAuthors));
        verify(authorsFetcher, times(1)).all();
    }

    @Test
    void testMultipleAuthor() {
        BookManager ListOfBooks = new BookManager(ratingsFetcher, authorsFetcher);

        List<String> authors = new ArrayList<String>();
        authors.add("Author1");
        authors.add("Author2");
        when(authorsFetcher.all()).thenReturn(authors);


        authors = ListOfBooks.uniqueAuthors();

        List<String> expectedAuthors = new ArrayList<String>();
        expectedAuthors.add("Author1");
        expectedAuthors.add("Author2");

        assertEquals(2, authors.size());
        assertTrue(authors.containsAll(expectedAuthors));
        verify(authorsFetcher, times(1)).all();
    }

    @Test
    void testDuplicateAuthor() {
        BookManager ListOfBooks = new BookManager(ratingsFetcher, authorsFetcher);

        List<String> authors = new ArrayList<String>();
        authors.add("Author1");
        authors.add("Author1");
        when(authorsFetcher.all()).thenReturn(authors);


        authors = ListOfBooks.uniqueAuthors();

        List<String> expectedAuthors = new ArrayList<String>();
        expectedAuthors.add("Author1");

        assertEquals(1, authors.size());
        assertTrue(authors.containsAll(expectedAuthors));
        verify(authorsFetcher, times(1)).all();
    }


}
