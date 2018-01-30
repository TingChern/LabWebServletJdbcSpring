package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.CustomerBean;
import model.CustomerDAO;

@Repository
public class CustomerDAOJdbc implements CustomerDAO {
	@Autowired
	private DataSource dataSource;

	private static final String SELECT_BY_CUSTID = "select * from customer where custid=?";
	@Override
	public CustomerBean select(String custid) {
		CustomerBean result = null;
		
		ResultSet rset = null;
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CUSTID);) {
			
			stmt.setString(1, custid);
			rset = stmt.executeQuery();
			if(rset.next()) {
				result = new CustomerBean();
				result.setCustid(rset.getString("custid"));
				result.setPassword(rset.getBytes("password"));
				result.setEmail(rset.getString("email"));
				result.setBirth(rset.getDate("birth"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rset!=null) {
				try {
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
		}
		
		return result;
	}

	private static final String UPDATE =
			"update customer set password=?, email=?, birth=? where custid=?";
	@Override
	public boolean update(byte[] password, String email, java.util.Date birth, String custid) {
		
		try(Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(UPDATE);) {

			stmt.setBytes(1, password);
			stmt.setString(2, email);
			if(birth!=null) {
				long time = birth.getTime();
				stmt.setDate(3, new java.sql.Date(time));
			} else {
				stmt.setDate(3, null);
			}
			stmt.setString(4, custid);
			
			int i = stmt.executeUpdate();
			if(i==1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
