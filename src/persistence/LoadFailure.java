package persistence;

import java.nio.file.Path;

public record LoadFailure(Path file,
        Integer lineNumber,String reason,
        FileStatus fileStatus,Exception cause) {
            
    public LoadFailure {
        if (file == null) {
            throw new IllegalArgumentException("File path must not be null.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Failure reason must not be blank.");
        }

        if (cause == null) {
            throw new IllegalArgumentException("Failure cause must not be null.");
        }

        // File-level failure
        if (lineNumber == null) {
            if (fileStatus == null || fileStatus == FileStatus.SUCCESS) {
                throw new IllegalArgumentException("File-level failure requires a valid failure status.");
            }
        }
        // Line-level failure
        else {
            if (lineNumber <= 0) {
                throw new IllegalArgumentException("Line number must be positive.");
            }

            if (fileStatus != null) {
                throw new IllegalArgumentException("Line-level failure must not have a file status.");
            }
        }
    }
}
