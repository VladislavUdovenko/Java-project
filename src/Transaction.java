public class Transaction {
    private String period;
    private double data_value;
    private String units;

    public String getPeriod() {
        return period;
    }

    public double getData_value(){
        return data_value;
    }

    public String getUnits() {
        return units;
    }

    public Transaction(String period, double data_value, String units){
        this.period = period;
        this.data_value = data_value;
        this.units = units;
    }
}
