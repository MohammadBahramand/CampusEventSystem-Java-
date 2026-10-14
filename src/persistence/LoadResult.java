package persistence;

/** Successful loads have no failure; failed loads leave current business data unchanged. */
public record LoadResult(boolean isSuccess, LoadFailure failure) {
    public LoadResult {
        if (isSuccess == (failure != null)) {
            throw new IllegalArgumentException("Inconsistent load result.");
        }
    }

    public static LoadResult success() {
        return new LoadResult(true, null);
    }

    public static LoadResult failure(LoadFailure failure) {
        return new LoadResult(false, failure);
    }
}
