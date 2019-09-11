package jsoncrud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonCRUD {

    static Connection conn = null;
    static Statement stm = null;
    static ResultSet rs = null;
    static String countryId, countryName, regionId;

    static JSONObject connError = new JSONObject();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String ans;

        do {

            System.out.println("===== Menu =====");
            System.out.println("1.  Show List");
            System.out.println("2.  Insert record");
            System.out.println("3.  Update record");
            System.out.println("4.  Delete record");
            System.out.println("5.  Show single record");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    getDataList();
                    break;
                case 2:
                    insertData();
                    break;
                case 3:
                    updateData();
                    break;
                case 4:
                    deleteData();
                    break;
                case 5:
                    getSingleList();
                    break;
                default:
                    System.out.println("Please choose appropriate number");
            }

            System.out.print("Would you like to run again(Y/N)?");

            sc.nextLine();
            ans = sc.nextLine();

        } while (ans.equalsIgnoreCase("Y"));
    }

    public static Connection getConnection() {

        try {

            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JsonCRUD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JsonCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
    }

    public static void getDataList() {

        JSONArray mainarrList = new JSONArray();
        JSONObject singleobjList = new JSONObject();

        conn = getConnection();

        if (conn != null) {
            try {
                String sql = "SELECT COUNTRY_ID,COUNTRY_NAME,REGION_ID FROM COUNTRIES";
                stm = conn.createStatement();
                int i = stm.executeUpdate(sql);

                rs = stm.executeQuery(sql);

                while (rs.next()) {

                    countryId = rs.getString("country_id");
                    countryName = rs.getString("country_name");
                    regionId = rs.getString("region_id");
                    singleobjList.accumulate("countryId", countryId);
                    singleobjList.accumulate("countryName", countryName);
                    singleobjList.accumulate("regionId", regionId);
                    mainarrList.add(singleobjList);
                    singleobjList.clear();

                }
                System.out.println(mainarrList);
            } catch (SQLException ex) {
                Logger.getLogger(JsonCRUD.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {

            connError.accumulate("message", "Connection Error");
            System.out.println(connError);
        }
    }

    public static void insertData() {

        conn = getConnection();
        JSONObject singleInsert = new JSONObject();

        String sql = "INSERT INTO COUNTRIES(COUNTRY_ID,COUNTRY_NAME,REGION_ID) VALUES('BH','BHARAT',1)";

        if (conn != null) {
            try {
                stm = conn.createStatement();
                int i = stm.executeUpdate(sql);

                if (i > 0) {
                    singleInsert.accumulate("message", "Record inserted");
                    System.out.println(singleInsert);

                } else {
                    singleInsert.accumulate("message", "Record Not inserted");
                    System.out.println(singleInsert);
                }

            } catch (SQLException ex) {
                Logger.getLogger(JsonCRUD.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {

            connError.accumulate("message", "Connection Error");
            System.out.println(connError);

        }
    }

    public static void updateData() {

        conn = getConnection();
        JSONObject singleupdate = new JSONObject();
        String sql = "UPDATE COUNTRIES SET COUNTRY_NAME='HINDUSTAN' WHERE COUNTRY_ID='BH'";

        if (conn != null) {
            try {
                stm = conn.createStatement();

                int i = stm.executeUpdate(sql);

                if (i > 0) {
                    singleupdate.accumulate("message", "Record Updated");
                    System.out.println(singleupdate);
                } else {
                    singleupdate.accumulate("message", "Record Not Updated");
                    System.out.println(singleupdate);
                }

            } catch (SQLException ex) {
                Logger.getLogger(JsonCRUD.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {
            connError.accumulate("message", "Connection Error");
            System.out.println(connError);
        }
    }

    public static void deleteData() {

        conn = getConnection();
        JSONObject singleDelete = new JSONObject();

        String sql = "DELETE FROM COUNTRIES WHERE COUNTRY_ID = 'BH'";

        if (conn != null) {
            try {
                stm = conn.createStatement();
                int i = stm.executeUpdate(sql);
                if (i > 0) {
                    singleDelete.accumulate("message", "Record Deleted");
                    System.out.println(singleDelete);
                } else {
                    singleDelete.accumulate("message", "Record not Deleted");
                    System.out.println(singleDelete);
                }

            } catch (SQLException ex) {
                Logger.getLogger(JsonCRUD.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {
            connError.accumulate("message", "Connection Error");
            System.out.println(connError);
        }
    }

    public static void getSingleList() {

        conn = getConnection();
        JSONObject singleList = new JSONObject();

        String sql = "SELECT * FROM COUNTRIES WHERE COUNTRY_ID='BH'";

        if (conn != null) {
            try {
                stm = conn.createStatement();
                int i = stm.executeUpdate(sql);

                rs = stm.executeQuery(sql);

                while (rs.next()) {

                    countryId = rs.getString("country_id");
                    countryName = rs.getString("country_name");
                    regionId = rs.getString("region_id");
                    singleList.accumulate("countryId", countryId);
                    singleList.accumulate("countryName", countryName);
                    singleList.accumulate("regionId", regionId);
                }
                System.out.println(singleList);

            } catch (SQLException ex) {
                Logger.getLogger(JsonCRUD.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {
            connError.accumulate("message", "Connection Error");
            System.out.println(connError);
        }
    }

    public static void closeConnection() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
    }
}
