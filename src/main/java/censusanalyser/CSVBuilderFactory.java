package censusanalyser;

public class CSVBuilderFactory<E> {

    public static ICSVBuilder createCSVBuilder(){
            return new OpenSCVBuilder();
    }
}
