package form;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import model.Employee;
import service.EmployeeService;

public class EmployeeForm {

	public JFrame frmEmployeeEntry;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtSearch;
	private Employee employee;
	private JTextField txtSalary;
	private JTable tblEmployee;
	private EmployeeService employeeService;
	private DefaultTableModel dtm = new DefaultTableModel();//table
	private List<Employee> employeeList = new ArrayList<>();//for show all
	private List<Employee> filteredemployeeList = new ArrayList<>();//for searching

	//Connection create
	private final config.DBconfig dbConfig = new config.DBconfig();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeForm window = new EmployeeForm();
					window.frmEmployeeEntry.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	//Class's default Constructor
	public EmployeeForm() {
		initialize();
		this.initializeDependency();//service?
		this.setTableDesign();//table design
		this.loadAllEmployees(Optional.empty());//show all data in table
	}

	private void initializeDependency() {
		this.employeeService = new EmployeeService();
	}

	private void setTableDesign() {
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Salary");
		dtm.addColumn("Address");
		this.tblEmployee.setModel(dtm);//add columns to the table
	}

	private void loadAllEmployees(Optional<List<Employee>> optionalEmployees) {
		this.dtm = (DefaultTableModel) this.tblEmployee.getModel();//col names to dtm
		this.dtm.getDataVector().removeAllElements();//?
		this.dtm.fireTableDataChanged();//?
		this.employeeList = this.employeeService.findAllEmployees();//all data(list) into list
		//optionalEmployees: searching data
		//if optionalEmployees has data, use that data. 
		//if no data, take employeeList's data
		//->(stream type)->(change back to List)->filteredemployeeList
		//This is "Anonymous function"!
		this.filteredemployeeList = optionalEmployees.orElseGet(
				() -> this.employeeList).stream().collect(Collectors.toList());
		
		filteredemployeeList.forEach(e -> {
			//take ONE row obj, with 4 columns
			Object[] row = new Object[4];//always create new row array for each e
			row[0] = e.getEmp_id();
			row[1] = e.getName();
			row[2] = e.getSalary();
			row[3] = e.getAddress();
			//add that ONE row
			dtm.addRow(row);
		});
		
		this.tblEmployee.setModel(dtm);//dtm -> table
	}

	private void resetFormData() {
		txtName.setText("");
		txtAddress.setText("");
		txtSalary.setText("");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//FRAME
		frmEmployeeEntry = new JFrame();
		frmEmployeeEntry.setTitle("Employee Entry");
		frmEmployeeEntry.setBounds(50, 100, 800, 500);
		frmEmployeeEntry.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEmployeeEntry.getContentPane().setLayout(null);
		frmEmployeeEntry.setSize(1000, 500);

		//3 LABELS 3 TXTBOXES
		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblName.setBounds(47, 39, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblName);

		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtName.setColumns(10);
		txtName.setBounds(47, 78, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtName);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.LEFT);
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAddress.setBounds(47, 126, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblAddress);

		txtAddress = new JTextField();
		txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtAddress.setColumns(10);
		txtAddress.setBounds(47, 165, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtAddress);
		
		JLabel lblSalary = new JLabel("Salary");
		lblSalary.setHorizontalAlignment(SwingConstants.LEFT);
		lblSalary.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSalary.setBounds(47, 212, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblSalary);

		txtSalary = new JTextField();
		txtSalary.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSalary.setColumns(10);
		txtSalary.setBounds(47, 251, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtSalary);

		//SAVE BTN
		JButton btnSave = new JButton("Save");
		btnSave.setMnemonic('S');
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Employee employee = new Employee();
				try {
				employee.setName(txtName.getText());
				employee.setAddress(txtAddress.getText());
				employee.setSalary(Integer.parseInt(txtSalary.getText()));
				if (!employee.getName().isBlank() && !employee.getAddress().isBlank() && employee.getSalary() != 0) {
					
					employeeService.createEmployee(employee);//call INSERT service
					resetFormData();
					loadAllEmployees(Optional.empty());
				} 
				}
				catch(NumberFormatException ne) {
					JOptionPane.showMessageDialog(null, "Enter Required Field!");
				}
				catch(NullPointerException ne) {
					JOptionPane.showMessageDialog(null, "Enter Required Field!!");
				}
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSave.setBounds(47, 309, 193, 29);
		frmEmployeeEntry.getContentPane().add(btnSave);

		//SEARCH TXTBOX and BTN
		txtSearch = new JTextField();
		txtSearch.setToolTipText("");
		txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSearch.setColumns(10);
		txtSearch.setBounds(669, 78, 165, 29);
		frmEmployeeEntry.getContentPane().add(txtSearch);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyword = txtSearch.getText();
				//filter with Name
				//.filter(c->condition using c)
				//employeeList is a List containing Employee objects
				loadAllEmployees(
						Optional.of(employeeList.stream()
								.filter(emp->emp.getName().toLowerCase().startsWith(keyword.toLowerCase()))
								.collect(Collectors.toList()))
						
						);
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSearch.setBounds(854, 78, 85, 29);
		frmEmployeeEntry.getContentPane().add(btnSearch);

		//SCROLLPANE and TABLE
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(276, 132, 662, 292);
		frmEmployeeEntry.getContentPane().add(scrollPane);

		tblEmployee = new JTable();
		tblEmployee.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setViewportView(tblEmployee);//add Table to ScrollPane
		//When row is selected...
		this.tblEmployee.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblEmployee.getSelectionModel().isSelectionEmpty()) {
				//GET id!!!
				String id = tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 0).toString();

				//selected row's data into employee
				employee = employeeService.findEmployeeById(id);

				txtName.setText(employee.getName());
				txtAddress.setText(employee.getAddress());
				txtSalary.setText(employee.getSalary() + "");

			}
		});

		//UPDATE
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//reset employee with updated data from text boxes(except id)
					employee.setName(txtName.getText());
					employee.setAddress(txtAddress.getText());
					employee.setSalary(Integer.parseInt(txtSalary.getText()));
					if (!employee.getName().isBlank() && !employee.getAddress().isBlank() && employee.getSalary() != 0) {

						employeeService.updateEmployee(String.valueOf(employee.getEmp_id()), employee);
						resetFormData();
						loadAllEmployees(Optional.empty());
						employee = null;

					}
				}
				catch(NullPointerException ne) {
					JOptionPane.showMessageDialog(null, "Select the row to update!");
				}
				catch(NumberFormatException ne) {
					JOptionPane.showMessageDialog(null, "Select the row to update!!");
				}
			}
		});
		btnUpdate.setMnemonic('U');
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnUpdate.setBounds(47, 358, 193, 29);
		frmEmployeeEntry.getContentPane().add(btnUpdate);

		
		//DELETE
		JButton btnDelete = new JButton("Delete");
		btnDelete.setMnemonic('D');
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDelete.setBounds(47, 406, 193, 29);
		frmEmployeeEntry.getContentPane().add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					employee.setName(txtName.getText());
					employee.setAddress(txtAddress.getText());
					employee.setSalary(Integer.parseInt(txtSalary.getText()));
					employee.setAddress(txtAddress.getText());
					if (!employee.getName().isBlank() && !employee.getAddress().isBlank() && employee.getSalary() != 0) {
						employeeService.deleteEmployee(String.valueOf(employee.getEmp_id()), employee);
						resetFormData();
						loadAllEmployees(Optional.empty());
						employee = null;

					}
				}
				catch(NullPointerException ne) {
					JOptionPane.showMessageDialog(null, "Select the row to delete!");
				}
				catch(NumberFormatException ne) {
					JOptionPane.showMessageDialog(null, "Select the row to delete!!");
				}
			}
		});

	}
}
