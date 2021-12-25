
import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class Parser {
    public static List<Transaction> transactionList = new ArrayList<>();

    public static void Parse()
    {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("Переводы.csv"))) {
            String line = br.readLine();
            while ((line = br.readLine())!=null){
                String[] lines = line.split(",");
                double data_value;
                if (lines[2].isEmpty())
                {
                    data_value =0;
                }
                else{
                    data_value = Double.parseDouble(lines[2]);
                }
                transactionList.add(new Transaction(lines[1],data_value,lines[5]));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CreateTables(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:transactions.db");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE transactions (id INTEGER PRIMARY KEY AUTOINCREMENT,period TEXT,data_value DOUBLE,units TEXT)");
            for (Transaction transaction : transactionList){
                statement.execute("INSERT INTO transactions (period,data_value,units) " +
                        "VALUES("+transaction.getPeriod()+","+transaction.getData_value()+", '"+transaction.getUnits()+"')");
            }
        }catch (Exception t){
            t.printStackTrace();
        }

    }

    public static void FirstTask(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:transactions.db");
            Statement statement = connection.createStatement();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            ResultSet resultSet = statement.executeQuery("SELECT substr(period,6,9) as t,sum(data_value) FROM transactions WHERE substr(period,0,5) = '2020' AND units = 'Dollars' GROUP BY t");
            while (resultSet.next())
            {
                dataset.addValue(resultSet.getDouble(2),resultSet.getString(1),"месяц");
            }
            JFreeChart jFreeChart = ChartFactory.createBarChart("График","месяц","Сумма",dataset);
            ChartFrame chartFrame = new ChartFrame("График",jFreeChart);
            chartFrame.setVisible(true);
        }
        catch (Exception t){
            t.printStackTrace();
        }
    }

    public static void SecondTask(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:transactions.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT period,COUNT(data_value),ROUND(AVG(data_value),2) FROM transactions WHERE units = 'Dollars' GROUP BY period");
            while(resultSet.next()){
                System.out.println(resultSet.getString(1)+", COUNT:"+resultSet.getString(2)+", AVG:"+resultSet.getString(3));

            }
        }
        catch(Exception t){
            t.printStackTrace();
        }

    }
    public static void ThirdTask(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:transactions.db");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(data_value),MIN(data_value) FROM transactions WHERE substr(period,0,5) IN ('2020','2014','2016') AND units = 'Dollars' AND data_value!=0");
            while(resultSet.next())
            {
                System.out.println("MAX: "+resultSet.getString(1)+", MIN: "+resultSet.getString(2));
            }
        }catch(Exception t){
            t.printStackTrace();
        }
    }



}