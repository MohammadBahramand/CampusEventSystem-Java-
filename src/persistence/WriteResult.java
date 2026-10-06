package persistence;

import java.io.IOException;

public record WriteResult(FileStatus status, IOException cause) {
    public WriteResult {
        if (status == null || (status == FileStatus.SUCCESS ? cause != null : cause == null))
        {
            throw new IllegalArgumentException("Inconsistent write result.");
        }
    }

    public boolean isSuccess()
    {
        return status == FileStatus.SUCCESS;
    }

    public static WriteResult success()
    {
        return new WriteResult(FileStatus.SUCCESS, null);
    }

    public static WriteResult failure(FileStatus status, IOException cause)
    {
        if (status == FileStatus.SUCCESS || cause == null)
        {
            throw new IllegalArgumentException("A failure status and cause are required.");
        }
        return new WriteResult(status, cause);
    }
}
