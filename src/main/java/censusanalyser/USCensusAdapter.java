package censusanalyser;

import java.util.HashMap;
import java.util.Map;

public class USCensusAdapter extends CensusAdapter {

    @Override
    public Map<String, CSVCensusDAO> loadCensus(String... filePath) throws CensusAnalyserException {
        Map<String, CSVCensusDAO> censusDAOMap = new HashMap<>();
        censusDAOMap = super.loadCensusCSVFileData(USCensusData.class, filePath[0]);
        return censusDAOMap;
    }
}
