import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataAdapter implements IDataAdapter{

    public static final int PRODUCT_SAVED_OK = 0;
    public static final int PRODUCT_DUPLICATE_ERROR = 1;

    public static final int Customer_SAVED_OK = 0;
    public static final int Customer_DUPLICATE_ERROR = 1;

    public static final int PURCHASE_SAVED_OK = 0;
    public static final int PURCHASE_DUPLICATE_ERROR = 1;

    Connection conn = null;

    public int connect(String dbfile) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + dbfile;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_OPEN_FAILED;
        }
        return CONNECTION_OPEN_OK;
    }

    @Override
    public int disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_CLOSE_FAILED;
        }
        return CONNECTION_CLOSE_OK;
    }

    public ProductModel loadProduct(int productID) {
        ProductModel product = new ProductModel();

        try {
            String sql = "SELECT ProductId, Name, Price, Quantity FROM Products WHERE ProductId = " + productID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            product.mProductID = rs.getInt("ProductId");
            product.mName = rs.getString("Name");
            product.mPrice = rs.getDouble("Price");
            product.mQuantity = rs.getDouble("Quantity");


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return product;
    }

    public CustomerModel loadCustomer(int CustomerID) {
        CustomerModel Customer = new CustomerModel();

        try {
            String sql = "SELECT CustomerId, Name, Address, Phone FROM Customer WHERE CustomerId = " + CustomerID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Customer.mCustomerID = rs.getInt("CustomerId");
            Customer.mName = rs.getString("Name");
            Customer.mAddress = rs.getString("Address");
            Customer.mPhone = rs.getString("Phone");


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Customer;
    }

    /*public PurchaseModel loadPurchase(int PurchaseID, int CustomerID, int ProductID) {
        PurchaseModel Purchase = new PurchaseModel();

        try {
            String sql = "SELECT PurchaseID, CustomerID, ProductID, Quantity FROM Purchase WHERE PurchaseID = " + PurchaseID;
            String sqlCustomer = "SELECT Name FROM Product WHERE ProductID = " + ProductID;
            String sqlProduct = "SELECT Name FROM Customer WHERE CustomerID = " + CustomerID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSet rsCustomer = stmt.executeQuery(sqlCustomer);
            ResultSet rsProduct = stmt.executeQuery(sqlProduct);
            Purchase.mPurchaseID = rs.getInt("PurchaseID");
            Purchase.mCustomerID = rs.getInt("CustomerID");
            Purchase.mProductID = rs.getInt("ProductID");
            Purchase.mQuantity = rs.getDouble("Quantity");
            Purchase.mTotalCost = rs.getDouble("Total Cost");
            Purchase.mDateTime = rs.getString("Date/Time");
            Purchase.mCustomerName = rsCustomer.getString("Name");
            Purchase.mProductName = rsProduct.getString("Name");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Purchase;
    }*/

    public int saveProduct(ProductModel product) {
        try {
            String sql = "INSERT INTO Products(ProductId, Name, Price, Quantity) VALUES " + product;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PRODUCT_DUPLICATE_ERROR;
        }

        return PRODUCT_SAVED_OK;
    }

    public int saveCustomer(CustomerModel Customer) {
        try {
            String sql = "INSERT INTO Customer(CustomerId, Name, Address, Phone) VALUES " + Customer;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return Customer_DUPLICATE_ERROR;
        }

        return Customer_SAVED_OK;
    }

    public int savePurchase(PurchaseModel Purchase) {
        try {
            String sql = "INSERT INTO Purchases(PurchaseID, CustomerID, ProductID, \"Date/Time\", Quantity, \"Total Cost\", Price, Cost, Tax) VALUES " + Purchase;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PURCHASE_DUPLICATE_ERROR;
        }

        return PURCHASE_SAVED_OK;
    }
}

