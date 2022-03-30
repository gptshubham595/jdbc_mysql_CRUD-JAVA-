package tutorpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

public class db {
	public static void main(String arg[]) {
		Connection con = null;

		HashMap<Integer, String> map = new HashMap<>();
		map.put(3, "Decimal");
		map.put(12, "Varchar");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/ddl";
			Properties info = new Properties();
			info.put("user", "root");
			info.put("password", "");

			con = DriverManager.getConnection(url, info);

			if (con != null) {
				System.out.println("Successfully connected to MySQL database.");
			}
			Scanner input = new Scanner(System.in);
			boolean mainLoop = true;
			int choice;
			Statement stmt = con.createStatement();

			while (mainLoop) {
				System.out.println("Main Menu: ");
				System.out.println("1. Print Data of a table: ");
				System.out.println("2. Insert Data into a table: ");
				System.out.println("3. Update budget data in department: ");
				System.out.println("4. Delete a department: ");
				System.out.println("5. Exit: ");
				choice = input.nextInt();

				switch (choice) {
				case 1:
					StringBuilder query = new StringBuilder();
					query.append("select * from ");
					System.out.println("Enter table name to print data: ");
					Scanner sc = new Scanner(System.in);
					String str = sc.nextLine();
					query.append(str);

					try (ResultSet rs = stmt.executeQuery(query.toString())) {
						ResultSetMetaData rsmd = rs.getMetaData();
						int columnCount = rsmd.getColumnCount();
						System.out.println("Data of " + str + " TABLE");
						while (rs.next()) {
							StringBuilder tableData = new StringBuilder();
							for (int colIdx = 1; colIdx <= columnCount; colIdx++) {
								tableData.append(rs.getObject(colIdx));
								if (colIdx != columnCount) {
									tableData.append("  ,  ");
								}
							}
							System.out.println(tableData);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 2:
					Scanner s1 = new Scanner(System.in);
					System.out.println("Enter Table Name:");
					String q1 = s1.nextLine();
					ResultSet rs = stmt.executeQuery("select * from " + q1);
					ResultSetMetaData rsMetaData = rs.getMetaData();
					int count = rsMetaData.getColumnCount();
					System.out.println("Column names of the table " + q1 + " are:");
					for (int i = 1; i <= count; i++) {
						System.out.print(
								rsMetaData.getColumnName(i) + " (" + map.get(rsMetaData.getColumnType(i)) + "), ");
					}
					System.out.println("\nEnter Values (ex: 100, 'String', 10.20, 'Apple'):");
					String q2 = s1.nextLine();
					String sql = "INSERT INTO " + q1 + " VALUES (" + q2 + ")";
					System.out.println(sql);
					stmt.executeUpdate(sql);

					break;
				case 3:
					System.out.println("Enter dept_name to change budget");
					Scanner sc1 = new Scanner(System.in);
					String str1 = sc1.nextLine();

					System.out.println("Enter new budget");
					Scanner sc2 = new Scanner(System.in);
					String str2 = sc2.nextLine();

					String sql1 = "UPDATE department SET budget = " + str2 + " where dept_name= '" + str1 + "'";

					System.out.println(sql1);
					stmt.executeUpdate(sql1);
					break;
				case 4:
					System.out.println("Enter dept_name to delete:");
					Scanner sc3 = new Scanner(System.in);
					String str3 = sc3.nextLine();

					System.out
							.println("Are you sure you want to delete row with dept_name='" + str3 + "':(ex: y or n)");
					Scanner sc4 = new Scanner(System.in);
					String str4 = sc4.nextLine();

					if (str4.toLowerCase().equals("yes") || str4.toLowerCase().equals("y")) {
						System.out.println("Deleted");
						String sql2 = "DELETE FROM department WHERE dept_name='" + str3 + "'";
						stmt.executeUpdate(sql2);
					} else {
						System.out.println("Cancelled");
					}
					break;
				case 5:
					con.close();
					mainLoop = false;
					System.exit(0);
					break;
				default:
					con.close();
					mainLoop = false;
					System.exit(0);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
