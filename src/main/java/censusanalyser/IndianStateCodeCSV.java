package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class IndianStateCodeCSV {

    @CsvBindByName(column = "SrNo", required = true)
    public String srNo  ;

    @CsvBindByName(column = "StateName", required = true)
    public String stateName;

    @CsvBindByName(column = "TIN", required = true)
    public  String tin;

    @CsvBindByName(column = "StateCode", required = true)
    public String stateCode;

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public String toString() {
        return "IndianStaeCodeCSV{" +
                "srNo='" + srNo + '\'' +
                ", stateName='" + stateName + '\'' +
                ", tin='" + tin + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }
}
