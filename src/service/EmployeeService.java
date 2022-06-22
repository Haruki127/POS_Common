package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
import shared.mapper.EmployeeMapper;

public class EmployeeService {
    private final EmployeeMapper employeeMapper;
	private final config.DBconfig dbConfig;

	//def constructor
	public EmployeeService() {
	    this.employeeMapper = new EmployeeMapper();
		this.dbConfig = new config.DBconfig();//connection create
	}

	//INSERT
	public void createEmployee(Employee employee) {
		try {
			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("INSERT INTO emp (name, address, salary) VALUES (?, ?, ?)");

			ps.setString(1, employee.getName());
			ps.setString(2, employee.getAddress());
			ps.setInt(3, (int) employee.getSalary());
			ps.executeUpdate();//IMP! run query
			ps.close();//close connection

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//findEmployeeById
	public Employee findEmployeeById(String id) {
		Employee employee = new Employee();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM emp WHERE emp_id = " + id + ";";

			ResultSet rs = st.executeQuery(query);
			//if rs has data
			if (rs.next()) {
				this.employeeMapper.mapToEmployee(employee, rs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		//return selected row's data
		return employee;
	}
	
	//UPDATE
    public void updateEmployee(String id, Employee employee) {
        try {
            PreparedStatement ps = this.dbConfig.getConnection()
                    .prepareStatement("UPDATE emp SET name=?, address=?, salary=? WHERE emp_id=?");

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getAddress());
            ps.setLong(3, employee.getSalary());
            ps.setString(4, id);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {

        	e.printStackTrace();
        }
    }
    
    //DELETE
    public void deleteEmployee(String id, Employee employee) {
        try {
            PreparedStatement ps = this.dbConfig.getConnection()
            .prepareStatement("delete from emp where emp_id=?");

            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {

        	e.printStackTrace();
        }
    }
    
    public List<Employee> findAllEmployees(){
    	List<Employee> employeeList = new ArrayList<>();
    	try (Statement st = this.dbConfig.getConnection().createStatement()){
    		String query = "SELECT * FROM emp";
    		ResultSet rs = st.executeQuery(query);
    		while(rs.next()) {
    			Employee employee = new Employee();
    			employeeList.add(this.employeeMapper.mapToEmployee(employee, rs));
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return employeeList;
    }


}
