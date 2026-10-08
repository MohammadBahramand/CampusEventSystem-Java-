package persistence;

import java.nio.file.Path;

public record ReadRequest(Path relativePath) {
    public ReadRequest {
        relativePath = RequestPath.validate(relativePath);
    }
}
