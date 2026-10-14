package persistence;

/** Caller-selected conversion, including parsers that report physical file line numbers. */
@FunctionalInterface
public interface DataParser<T> {
    T parse(String text) throws DataFormatException;
}
