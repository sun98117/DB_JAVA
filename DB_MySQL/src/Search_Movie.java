import java.sql.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
public class Search_Movie {
	public static void main(String[] args) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null; 
		Scanner sc = new Scanner(System.in);
		try{
			//Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver"); 

			//Open a connection 
			System.out.println("Connecting to database..."); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "root", "zjarhk159!");
			stmt = conn.createStatement();
			String table = "create_table.txt";
			String data = "movie_data.txt";
			String name = "movie";
			String create = "";
			String str1 = null;
			String[] words = {"","","","","","","","",""} ;
			StringTokenizer t = null;
			
			int num;
			while(true) {
				System.out.println("============================");
				System.out.println("(0) 종료");
				System.out.println("(1) 릴레이션 생성 및 데이터 삽입");
				System.out.println("(2) 제목을 이용한 검색");
				System.out.println("(3) 관객수를 이용한 검색");
				System.out.println("(4) 개봉일을 이용한 검색"); 
				System.out.println("============================");
				System.out.print("원하는 번호를 입력하시오 ");
				num = sc.nextInt();
				if(num == 0) {
					break;
				}
				else {
					switch(num) {
					case 1:
						stmt.executeUpdate("drop table movie;");
						File file1 = new File(table);
						File file2 = new File(data);
						try {
							Scanner sc1 = new Scanner(file1);
							while(sc1.hasNextLine()) {
								String s = sc1.nextLine();
								create = create.concat(s);	
							}
							stmt.executeUpdate(create);
							sc1.close();
							Scanner sc2 = new Scanner(file2);
							while(sc2.hasNextLine()) {
								str1 = sc2.nextLine();
								t = new StringTokenizer(str1,"|");
								for(int i=0;t.hasMoreElements();i++) {
									words[i] = t.nextToken();
								}
								stmt.executeUpdate("insert into "+name+" values('"+
								words[0]+"','"+words[1]+"','"+words[2]+"','"+
										words[3]+""+ "','"+words[4]+"',"+words[5]+","+
								words[6]+","+words[7]+",'"+words[8]+"')");
							}	
							sc2.close();
							System.out.println(name+" table이 입력되었습니다.");
						}
						catch(FileNotFoundException e) {
							System.out.println("파일을 불러올 수 없습니다.");
						}
						break;
					case 2: //제목
						System.out.print("사용자 입력: ");
						String str = sc.next();
						//Execute a query  
						rs = stmt.executeQuery("SELECT * FROM "+name+" WHERE title like '%"+str+"%'");
						
						//Print results
			            while(rs.next()) {
			            	
			            	System.out.println("|"+ rs.getString(1) + "|" +
			            			rs.getString(2) + "|" + rs.getString(3) + "|" + 
			            			rs.getDate(4)+"|"+rs.getString(5)+"|"+
			            			rs.getInt(6)+"|"+rs.getBigDecimal(7)+"|"+
			            			rs.getInt(8)+"|"+rs.getString(9));
			            }	
						break;
					case 3: //관객수
						System.out.print("사용자 입력: ");
						int audienceNum = sc.nextInt();
						rs = stmt.executeQuery("SELECT * FROM "+name+" WHERE totalnum > "+audienceNum);
			            while(rs.next()) {
			            	System.out.println(rs.getString(1) + "|" +
			            			rs.getString(2) + "|" + rs.getString(3) + "|" + 
			            			rs.getDate(4)+"|"+rs.getString(5)+"|"+
			            			rs.getInt(6)+"|"+rs.getBigDecimal(7)+"|"+
			            			rs.getInt(8)+"|"+rs.getString(9));
			            }	
						break;
					case 4:	//개봉일
						System.out.print("사용자 입력: ");
						String s = sc.nextLine();
						String strData = sc.nextLine();
						String[] date = {"",""};
						t = new StringTokenizer(strData,", ");
						for(int i=0;t.hasMoreElements();i++) {
							date[i] = t.nextToken();
						}
						Date d1 = Date.valueOf(date[0]);
						Date d2 = Date.valueOf(date[1]);
						rs = stmt.executeQuery("SELECT * FROM "+name+" WHERE releasedate >= '"+d1
								+"' and releasedate <= '"+d2+"'");
			            while(rs.next()) {
			            	System.out.println(rs.getString(1) + "|" +
			            			rs.getString(2) + "|" + rs.getString(3) + "|" + 
			            			rs.getDate(4)+"|"+rs.getString(5)+"|"+
			            			rs.getInt(6)+"|"+rs.getBigDecimal(7)+"|"+
			            			rs.getInt(8)+"|"+rs.getString(9));
			            }
						break;
					default:
						System.out.print("다른 번호를 입력하세요");
						break;
					}
				}
			}

            // close a connection
            stmt.close();
            conn.close();

		}catch (SQLException ex) {
			//Handle errors for JDBC
			ex.printStackTrace();
		} catch (Exception e){
		    //Handle errors for Class.forName
			e.printStackTrace();
		}
	}
}
