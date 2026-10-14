package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/** Reads and writes text relative to one data directory; performs no domain conversion. */
public class FileManager {
    private static final Path STUDENTS_FILE = Path.of("students.txt");
    private static final Path ORGANIZERS_FILE = Path.of("organizers.txt");
    private static final Path EVENTS_FILE = Path.of("events.txt");
    private static final Path REGISTRATIONS_FILE = Path.of("registrations.txt");
    private static final List<Path> DATA_FILES = List.of(
            STUDENTS_FILE, ORGANIZERS_FILE, EVENTS_FILE, REGISTRATIONS_FILE);

    private final Path dataDirectory;

    public FileManager() {
        this(Path.of("data"));
    }

    /** Resolves relative directories now, without creating or opening anything. */
    public FileManager(Path dataDirectory) {
        if (dataDirectory == null || dataDirectory.toString().isEmpty()) {
            throw new IllegalArgumentException("A data directory is required.");
        }
        this.dataDirectory = dataDirectory.toAbsolutePath().normalize();
    }

    public InitResult init() {
        try {
            Files.createDirectories(dataDirectory);
        }
        catch (IOException error){
            return InitResult.failure(statusFor(error), dataDirectory, error);
        }

        for (Path relativePath : DATA_FILES) {

            Path path = dataDirectory.resolve(relativePath);
            try {
                if (!prepareFile(path)) {
                    return InitResult.failure(FileStatus.NOT_REGULAR_FILE, path, notRegularFile(path));
                }
            } catch (IOException error) {
                return InitResult.failure(statusFor(error), path, error);
            }
        }
        return InitResult.success();
    }

    public ReadResult readStudentsFile() {
        return readFile(new ReadRequest(STUDENTS_FILE));
    }

    public ReadResult readOrganizersFile() {
        return readFile(new ReadRequest(ORGANIZERS_FILE));
    }

    public ReadResult readEventsFile() {
        return readFile(new ReadRequest(EVENTS_FILE));
    }

    public ReadResult readRegistrationsFile() {
        return readFile(new ReadRequest(REGISTRATIONS_FILE));
    }


    public ReadResult readFile(ReadRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("A read request is required.");
        }
        Path path = dataDirectory.resolve(request.relativePath());
        try {
            if (!isRegularFile(path)) {
                return ReadResult.failure(FileStatus.NOT_REGULAR_FILE, notRegularFile(path));
            }
            return ReadResult.success(Files.readString(path, StandardCharsets.UTF_8));
        } catch (IOException error) {
            return ReadResult.failure(statusFor(error), error);
        }
    }

    public WriteResult writeFile(WriteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("A write request is required.");
        }
        Path path = dataDirectory.resolve(request.relativePath());
        try {
            try {
                if (!isRegularFile(path)) {
                    return WriteResult.failure(FileStatus.NOT_REGULAR_FILE, notRegularFile(path));
                }
            } catch (NoSuchFileException missingFile) {
                // A missing file may be created by the write; missing parents are not created.
            }
            Files.writeString(path, request.content(), StandardCharsets.UTF_8);
            return WriteResult.success();
        } catch (IOException error) {
            return WriteResult.failure(statusFor(error), error);
        }
    }

    private static boolean prepareFile(Path path) throws IOException {
        try {
            return isRegularFile(path);
        } catch (NoSuchFileException missingFile) {
            try {
                Files.createFile(path);
            } catch (FileAlreadyExistsException alreadyCreated) {
                // Another initializer may have created it; validate without overwriting it.
            }
            return isRegularFile(path);
        }
    }

    private static boolean isRegularFile(Path path) throws IOException {
        return Files.readAttributes(path, BasicFileAttributes.class).isRegularFile();
    }

    private static IOException notRegularFile(Path path) {
        return new FileSystemException(path.toString(), null, "Path is not a regular file.");
    }

    private static FileStatus statusFor(IOException error) {
        if (error instanceof NoSuchFileException) {
            return FileStatus.NOT_FOUND;
        }
        if (error instanceof AccessDeniedException) {
            return FileStatus.ACCESS_DENIED;
        }
        return FileStatus.IO_ERROR;
    }
}
