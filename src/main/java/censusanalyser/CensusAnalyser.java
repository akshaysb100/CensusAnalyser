package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser<E> {

    List<IndiaCensusDAO> censusList;

    public CensusAnalyser() {
        this.censusList = new ArrayList<IndiaCensusDAO>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusList = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            while (censusList.hasNext()) {
                this.censusList.add(new IndiaCensusDAO(censusList.next()));
            }
            return this.censusList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndiaSateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndianStateCodeCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndianStateCodeCSV.class);
            return getCount(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int getCount(Iterator eIterator) {
        Iterable<E> iterable = () -> eIterator;
        int namOfEateries = (int) StreamSupport.stream(iterable.spliterator(), false).count();
        return namOfEateries;
    }

    public String getSortedDataBasedOnState() {

        Comparator<IndiaCensusDAO> csvComparable = Comparator.comparing(census -> census.state);
        this.sort(csvComparable);
        String sortStateData = new Gson().toJson(censusList);
        return sortStateData;
    }

    private void sort(Comparator<IndiaCensusDAO> censusCSVComparator) {
        for (int i = 0; i < censusList.size(); i++) {
            for (int j = 0; j < censusList.size() - i - 1; j++) {
                IndiaCensusDAO censusCSV = censusList.get(j);
                IndiaCensusDAO censusCSV1 = censusList.get(j + 1);
                if (censusCSVComparator.compare(censusCSV, censusCSV1) > 0) {
                    censusList.set(j, censusCSV1);
                    censusList.set(j + 1, censusCSV);
                }
            }
        }
    }
}
