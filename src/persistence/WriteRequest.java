package persistence;

import java.nio.file.Path;

public record WriteRequest(Path relativePath, String content) {
    public WriteRequest {
        relativePath = RequestPath.validate(relativePath);
        if (content == null)
        {
            throw new IllegalArgumentException("Content must not be null.");
        }
    }
}
