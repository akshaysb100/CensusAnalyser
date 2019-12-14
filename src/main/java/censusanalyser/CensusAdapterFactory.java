package censusanalyser;

public class CensusAdapterFactory {

    public static CensusAdapter getCensusDataObject(CensusAnalyser.Country country) throws CensusAnalyserException {
        if (country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusAdapter();
        return new USCensusAdapter();
    }
}
