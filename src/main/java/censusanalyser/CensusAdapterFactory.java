package censusanalyser;

public class CensusAdapterFactory{

    public static CensusAdapter getCensusDataObject(CensusAnalyser.Country country , String... csvFilepath) throws CensusAnalyserException {
        if(country.equals(CensusAnalyser.Country.INDIA)){
            return new IndiaCensusAdapter();
        }else if(country.equals(CensusAnalyser.Country.USA)){
            return new USCensusAdapter();
        }else {
            throw new CensusAnalyserException("Incorrect Country", CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
        }
    }
}
