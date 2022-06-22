package shared.mapper;

import java.sql.ResultSet;

import model.Employee;

public class EmployeeMapper {

	//SET Employee
    public Employee mapToEmployee(Employee employee, ResultSet rs) {
        try {
            employee.setEmp_id(rs.getInt("emp_id"));
            employee.setName(rs.getString("name"));
            employee.setAddress(rs.getString("address"));
            employee.setSalary(rs.getInt("salary"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }
}
