package persistence;

/** Outcomes of a storage operation; invalid requests throw IllegalArgumentException. */
public enum FileStatus {
    SUCCESS,
    NOT_FOUND,
    NOT_REGULAR_FILE,
    ACCESS_DENIED,
    ATOMIC_MOVE_NOT_SUPPORTED,
    IO_ERROR
}
