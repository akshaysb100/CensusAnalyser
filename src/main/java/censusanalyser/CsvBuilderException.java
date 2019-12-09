package censusanalyser;

public class CsvBuilderException extends Exception {


    enum ExceptionType {
        CENSUS_FILE_PROBLEM,UNABLE_TO_PARSE
    }

    CensusAnalyserException.ExceptionType type;

    public CsvBuilderException(String message, CensusAnalyserException.ExceptionType type) {
        super(message);
        this.type = type;
    }

    public CsvBuilderException(String message, CensusAnalyserException.ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
