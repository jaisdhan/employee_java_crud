import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static javax.swing.JOptionPane.*;

public class Employee {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtPhone;
    private JButton SAVEButton;
    private JTable table1;
    private JButton UPDATEButton;
    private JButton DELETEButton;
    private JButton SEARCHButton;
    private JTextField txtID;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;
    public void connect()
    {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/ramenterprises", "root", "");
            System.out.println("Connection successful");
        }
        catch(ClassNotFoundException cnf){
            cnf.printStackTrace();
        }
        catch(SQLException sql){
            sql.printStackTrace();
        }
    }
    void table1_load(){
        try{
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    public Employee() {
        connect();
        table1_load();
    SAVEButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String empname, salary, phone;
            empname=txtName.getText();
            salary=txtSalary.getText();
            phone=txtPhone.getText();
            try{
                pst = con.prepareStatement("insert into employee(empname, salary, phone) values(?, ?, ?)");
                pst.setString(1, empname);
                pst.setString(2, salary);
                pst.setString(3, phone);
                pst.executeUpdate();
                showMessageDialog(null,"Record added Successfully");
                table1_load();
                txtName.setText("");
                txtSalary.setText("");
                txtPhone.setText("");
                txtName.requestFocus();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    });
    UPDATEButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String empname, salary, phone, empid;
            empname = txtName.getText();
            salary = txtSalary.getText();
            phone = txtPhone.getText();
            empid = txtID.getText();
            try{
                pst = con.prepareStatement("update employee set empname = ?, salary = ?, phone = ? where id =?");
                pst.setString(1, empname);
                pst.setString(2, salary);
                pst.setString(3, phone);
                pst.setString(4, empid);


                pst.executeUpdate();
                table1_load();
                JOptionPane.showMessageDialog(null,"Record Updated");
                txtName.setText("");
                txtSalary.setText("");
                txtPhone.setText("");
                txtName.requestFocus();

            }catch(SQLException exc){
                exc.printStackTrace();
            }
        }
    });
    DELETEButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            String empid;
            empid = txtID.getText();

            try{
                pst = con.prepareStatement("delete from employee where id = ?");
                pst.setString(1, empid);
                pst.executeUpdate();
                table1_load();
                txtName.setText("");
                txtSalary.setText("");
                txtPhone.setText("");
                txtName.requestFocus();
                txtID.setText("");
            }catch(SQLException e2){
                e2.printStackTrace();
            }










        }
    });
    SEARCHButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                String empid = txtID.getText();

                pst = con.prepareStatement("select empname, salary, phone from employee where id = ?");
                pst.setString(1, empid);
                ResultSet rs = pst.executeQuery();

                if (rs.next() == true){
                    String empname=rs.getString(1);
                    String empsalary = rs.getString(2);
                    String empphone = rs.getString(3);

                    txtName.setText(empname);
                    txtSalary.setText(empsalary);
                    txtPhone.setText(empphone);
                }else{
                    txtName.setText("");
                    txtSalary.setText("");
                    txtPhone.setText("");
                    JOptionPane.showMessageDialog(null,"Invalid Employee ID");
                }

            }catch (SQLException e1){
                e1.printStackTrace();
            }

        }
    });
}
}
