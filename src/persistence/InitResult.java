package persistence;

import java.io.IOException;
import java.nio.file.Path;


public record InitResult(FileStatus status, Path failedPath, IOException cause) {
    public InitResult {
        if (status == null) {
            throw new IllegalArgumentException("Status is required.");
        }
        if (status == FileStatus.SUCCESS) {
            if (failedPath != null || cause != null) {
                throw new IllegalArgumentException("Successful initialization must not have a failure.");
            }
        } else if (failedPath == null || cause == null) {
            throw new IllegalArgumentException("Failed initialization requires a path and cause.");
        }
    }

    public boolean isSuccess() {
        return status == FileStatus.SUCCESS;
    }

    public static InitResult success() {
        return new InitResult(FileStatus.SUCCESS, null, null);
    }

    public static InitResult failure(FileStatus status, Path failedPath, IOException cause) {
        if (status == FileStatus.SUCCESS) {
            throw new IllegalArgumentException("A failure status is required.");
        }
        return new InitResult(status, failedPath, cause);
    }
}
