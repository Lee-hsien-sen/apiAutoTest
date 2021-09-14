package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySqlUtil {
	
	public static String sql;
	public static Connection conn =null;
	public static Statement stmt=null;
	public static ResultSet result = null;
	
	public static void main(String[] args) {
		//update  xyu.xyu_room   set  status=4   where  room_num=201808310045;
//		int a=updateOrderStatus("xyu.xyu_room","status=4", "room_num=201808310045");
//		System.out.println(a);
		select( " xyu_room " , " room_name = '中国特长房间名' ");
	}
	
	public static void closed(){
		try {
			result.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void select(String tableName, String condition){
		//数据库连接
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.10.222:3306/xyu?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull","root"
					,"offcn.com");
//					 Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.10.222:3306/xyu?user=root&amp;password=offcn.com&amp;useUnicode=true&amp;characterEncoding=UTF8");
			stmt = conn.createStatement();
//			 sql = "select * from " + " xyu_users;" ;
			 sql = "select * from " + tableName + " where " + condition;
			 System.out.println(sql);
//						 System.out.println("select_sql="+sql);
			 result  = stmt.executeQuery(sql);
			 result.last();
			 System.out.println(result.getRow());
				if (result.getRow() != 1) {
					System.out.println(result.getRow());
				} else{
//					String act_idNo = String.valueOf(result.getString(result.findColumn("mobile")));
//					System.out.println("name:"+act_idNo);
//							String act_idNo = String.valueOf(rs_cust_info.getString(rs_cust_info.findColumn("id_no")));
				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closed();
		}
	}
	
	//更改表
	public static Integer updateOrderStatus(String fromName,String setCondition, String whereCondition) {

		Integer result1=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.10.222:3306/xyu","root"
					,"offcn.com");
//					 Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.10.222:3306/xyu?user=root&amp;password=offcn.com&amp;useUnicode=true&amp;characterEncoding=UTF8");
			stmt = conn.createStatement();
			String sql = "update  "+  fromName  +"  set  "+  setCondition  +"  where  "+  whereCondition;
			 System.out.println(sql);
			result1 = stmt.executeUpdate(sql);//
			if (result1 == -1) {
				System.out.println("update order states failed!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("update order states failed!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result1;
	}
}
