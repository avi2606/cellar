package org.coenraets.cellar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<Employee>();
        Connection c = null;
    	String sql = "SELECT * FROM emp ORDER BY empName";
        try {
            c = ConnectionHelper.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }

    
    public List<Employee> findByName(String name) {
        List<Employee> list = new ArrayList<Employee>();
        Connection c = null;
    	String sql = "SELECT * FROM emp as e " +
			"WHERE UPPER(empName) LIKE ? " +	
			"ORDER BY empName";
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + name.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }
    
    public Employee findById(int id) {
    	String sql = "SELECT * FROM emp WHERE empID = ?";
        Employee emp = null;
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                emp = processRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return emp;
    }

    public Employee save(Employee emp)
	{
		return emp.getId() > 0 ? update(emp) : create(emp);
	}    
    
    public Employee create(Employee emp) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            ps = c.prepareStatement("INSERT INTO emp (empName, empDept, empSalary) VALUES (?, ?, ?)",
                new String[] { "empID" });
            ps.setString(1, emp.getName());
            ps.setString(2, emp.getDept());
            ps.setString(3, emp.getSalary());
           
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            // Update the id in the returned object. This is important as this value must be returned to the client.
            int id = rs.getInt(1);
            emp.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return emp;
    }

    public Employee update(Employee emp) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("UPDATE emp SET empName=?, empDept=?, empSalary=? WHERE empID=?");
            ps.setString(1, emp.getName());
            ps.setString(2, emp.getDept());
            ps.setString(3, emp.getSalary());
           
            ps.setInt(8, emp.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return emp;
    }

    public boolean remove(int id) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM emp WHERE empID=?");
            ps.setInt(1, id);
            int count = ps.executeUpdate();
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
    }

    protected Employee processRow(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("empID"));
        emp.setName(rs.getString("empName"));
        emp.setDept(rs.getString("empDept"));
        emp.setSalary(rs.getString("empSalary"));
        
        return emp;
    }
    
}
