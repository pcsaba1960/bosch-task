package com.bosch.service;

import com.bosch.data.Product;
import com.bosch.data.Production;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;

import static java.util.stream.Collectors.toList;

public class DataService {

    private static final String STR_MYSQL_JDBC_CONN = "jdbc:mysql://localhost:3306/cs_beugro?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String STR_COMMA = ",";
    private static final String STR_DATE_FORMAT = "yyyy.MM.dd HH:mm";
    private static final String STR_FILE_NAME = "puffer.txt";

    private static DataService dataService;

    private final List<Product> productList = new ArrayList<>();
    private final List<Production> productionList = new ArrayList<>();

    private Integer minId = 0;
    private Integer maxId = 0;

    private DataService() { }

    public static DataService getInstance() {
        if (dataService == null) {
            dataService = new DataService();
        }

        return dataService;
    }

    public void loadDataFromDB () {
       try (final Connection conn = DriverManager.getConnection(STR_MYSQL_JDBC_CONN, "root", "admin");
            final Statement stmt = conn.createStatement())
       {
           final String strSelect = "select * from products";

           final ResultSet rSet = stmt.executeQuery(strSelect);

           productList.clear();

           while(rSet.next()) {
               final Integer id = rSet.getInt("id");
               final  Product product = new Product(id, rSet.getString("pcb"));
               minId = id < minId ? id : minId;
               maxId = id > maxId ? id : maxId;
               productList.add(product);
           }

       } catch(SQLException ex) {
                ex.printStackTrace();
       }
   }

   public void createProductionList() {
       productionList.clear();
       Set<Integer> rIds = new HashSet<>();

       for(int i = 0; i < 10;) {
           Integer rId = getRandomValue(minId, maxId);

           if (!rIds.contains(rId)) {
               rIds.add(rId);
               final Product product = findById(rId);
               if (product != null) {
                   final Production production = new Production();

                   production.setPcbId(product.getId());
                   production.setQuantity(getRandomValue(1, 1000));

                   final int startDateAddingMinutes = getRandomValue(10, 20);

                   production.setStartDate(addMinutes(startDateAddingMinutes));

                   production.setEndDate(addMinutes(getRandomValue(startDateAddingMinutes+1,
                           startDateAddingMinutes+15)));

                   productionList.add(production);
                   i++;
               }
           }
       }
   }

   public void saveProductionListToFile() {
        try (final FileOutputStream fos = new FileOutputStream(STR_FILE_NAME)) {
            productionList.stream().forEach(p -> {
                try {
                    fos.write(p.toString().getBytes());;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
   }

   public void readProductionListFromFile() {
       productionList.clear();
       File file = new File("puffer.txt");

       try (BufferedReader br = new BufferedReader(new FileReader(file))) {
           String line;
           while ((line = br.readLine()) != null) {
               final String[] fields = line.split("\\|");

               final Production production = new Production();

               production.setPcbId(Integer.valueOf(fields[0]));
               production.setQuantity(Integer.valueOf(fields[1]));
               production.setStartDate(new SimpleDateFormat(STR_DATE_FORMAT).parse(fields[2]));
               production.setEndDate(new SimpleDateFormat(STR_DATE_FORMAT).parse(fields[3]));

               productionList.add(production);
           }

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } catch (ParseException e) {
           e.printStackTrace();
       }

   }

   public void saveProductionListToTable() {
        if (productionList.size() > 4) {
            productionList.remove(3);
        }
        final ListIterator<Production> pIterator = productionList.listIterator();
        while(pIterator.hasNext()) {
            final Production production = pIterator.next();
            if (production.getPcbId() == 4) {
                pIterator.remove();
            }
        }

       try (
               final Connection conn = DriverManager.getConnection(
                       "jdbc:mysql://localhost:3306/cs_beugro?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                       "root", "admin");
               final Statement stmt = conn.createStatement();)
       {
           productionList.stream().forEach(p -> {
               final String sqlInsert = new StringBuilder("insert into (pcb_id, quantity, startDate, endDate) values (")
                       .append(p.getPcbId())
                       .append(STR_COMMA)
                       .append(p.getQuantity())
                       .append(STR_COMMA)
                       .append(p.getStartDate())
                       .append(STR_COMMA)
                       .append(p.getEndDate())
                       .append(");")
                       .toString();
               try {
                   stmt.executeQuery(sqlInsert);
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           });
       } catch(SQLException ex) {
           ex.printStackTrace();
       }
   }

   private Product findById(Integer id) {
        final List<Product> pList =  productList.stream().filter(p -> p.getId() == id)
                .collect(toList());
        return pList.size() == 1 ? pList.get(0) : null;
   }

   private Date addMinutes(int minutes) {
       final LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(minutes, ChronoUnit.MINUTES));
       return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
   }

   private Integer getRandomValue(int min, int max) {
        final int v = max-min+1;
        return (int)Math.floor(Math.random()*v+min);
   }
}
