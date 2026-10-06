package persistence;

import java.nio.file.Path;

final class RequestPath {
    private RequestPath() {}

    static Path validate(Path path) {
        if (path == null || path.isAbsolute() || path.getRoot() != null)
        {
            throw new IllegalArgumentException("A relative file path is required.");
        }
        Path normalized = path.normalize();
        if (normalized.toString().isEmpty() || normalized.startsWith(".."))
        {
            throw new IllegalArgumentException("Path must identify a file inside the data directory.");
        }
        return normalized;
    }
}
