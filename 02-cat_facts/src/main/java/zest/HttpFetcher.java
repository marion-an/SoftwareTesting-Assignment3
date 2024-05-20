package zest;

import java.io.IOException;

public interface HttpFetcher {
    String get(String url) throws IOException;
}
