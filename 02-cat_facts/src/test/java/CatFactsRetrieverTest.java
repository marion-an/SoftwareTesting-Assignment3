import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zest.CatFactsRetriever;
import zest.HttpFetcher;
import zest.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CatFactsRetrieverTest {

    @Mock
    private HttpFetcher httpFetcher;

    @Test
    void testRetrieveRandom() throws IOException {

        CatFactsRetriever retriever = new CatFactsRetriever(httpFetcher);
        String retrievedFact = "{\"fact\":\"Cats have 3 eyelids.\",\"length\":20}";
        when(httpFetcher.get(anyString())).thenReturn(retrievedFact);

        String fact = retriever.retrieveRandom();

        assertEquals("Cats have 3 eyelids.", fact);
        verify(httpFetcher).get("https://catfact.ninja/fact");
    }

    @Test
    void testRetrieveLongest() throws IOException {

        CatFactsRetriever retriever = new CatFactsRetriever(httpFetcher);
        String jsonResponse = "{\"data\":[{\"fact\":\"Cats walk on their toes.\", \"length\":24}, {\"fact\":\"Cats have 3 eyelids.\", \"length\":20}]}";
        when(httpFetcher.get("https://catfact.ninja/facts?limit=2")).thenReturn(jsonResponse);

        String longestFact = retriever.retrieveLongest(2);

        assertEquals("Cats walk on their toes.", longestFact);
        verify(httpFetcher).get("https://catfact.ninja/facts?limit=2");
    }

    @Test
    void testRetrieveRandomEmpty() throws IOException {

        CatFactsRetriever retriever = new CatFactsRetriever(httpFetcher);
        String retrievedFact = "{}";
        when(httpFetcher.get(anyString())).thenReturn(retrievedFact);

        Exception exception = assertThrows(JSONException.class, retriever::retrieveRandom);

        assertTrue(exception.getMessage().contains("JSONObject[\"fact\"] not found"));
        verify(httpFetcher).get("https://catfact.ninja/fact");
    }

    @Test
    void testRetrieveLongestEmpty() throws IOException {

        CatFactsRetriever retriever = new CatFactsRetriever(httpFetcher);
        String emptyResponse = "{\"data\":[]}";
        when(httpFetcher.get("https://catfact.ninja/facts?limit=10")).thenReturn(emptyResponse);

        String longestFact = retriever.retrieveLongest(10);

        assertTrue(longestFact.isEmpty());
        verify(httpFetcher).get("https://catfact.ninja/facts?limit=10");
    }

    @Test
    void testRetrieveRandomNetworkError() throws IOException {

        CatFactsRetriever retriever = new CatFactsRetriever(httpFetcher);
        when(httpFetcher.get("https://catfact.ninja/fact")).thenThrow(new IOException("Failed to connect"));

        Exception exception = assertThrows(IOException.class, retriever::retrieveRandom);

        assertEquals("Failed to connect", exception.getMessage());
        verify(httpFetcher).get("https://catfact.ninja/fact");
    }

    @Test
    void testRetrieveLongestNetworkError() throws IOException {

        CatFactsRetriever retriever = new CatFactsRetriever(httpFetcher);
        when(httpFetcher.get("https://catfact.ninja/facts?limit=10")).thenThrow(new IOException("Failed to connect"));

        Exception exception = assertThrows(IOException.class, () -> retriever.retrieveLongest(10));

        assertEquals("Failed to connect", exception.getMessage());
        verify(httpFetcher).get("https://catfact.ninja/facts?limit=10");
    }

}
