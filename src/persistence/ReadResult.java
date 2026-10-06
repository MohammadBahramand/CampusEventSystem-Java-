package persistence;

import java.io.IOException;

public record ReadResult(FileStatus status, String content, IOException cause)
{
    public ReadResult {
        if (status == null || (status == FileStatus.SUCCESS ? content == null || cause != null
                                               : content != null || cause == null))
        {
            throw new IllegalArgumentException("Inconsistent read result.");
        }
    }

    public boolean isSuccess()
    {
        return status == FileStatus.SUCCESS;
    }

    public static ReadResult success(String content)
    {
        return new ReadResult(FileStatus.SUCCESS, content, null);
    }

    public static ReadResult failure(FileStatus status, IOException cause)
    {
        if (status == FileStatus.SUCCESS || cause == null)
        {
            throw new IllegalArgumentException("A failure status and cause are required.");
        }
        return new ReadResult(status, null,cause);
    }
}
