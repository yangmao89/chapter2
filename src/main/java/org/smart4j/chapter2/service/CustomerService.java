package org.smart4j.chapter2.service;

import com.mysql.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.util.PropsUtil;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CPR014 on 2017-04-11.
 */
public class CustomerService {


    /**
     * 根据条件获取客户列表
     * @param keyword
     * @return
     */
    public List<Customer> getCustomerList(String keyword){
        // TODO
        Connection conn = null;
        List<Customer> customerList = new ArrayList<>();
        try {
            conn = DatabaseHelper.getConnection();
            String sql = "select * from customer";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContact(rs.getString("contact"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setRemark(rs.getString("remark"));
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closeConnection();
        }
        return customerList;
    }

    public List<Customer> getCustomerList(){
        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql, null);
    }

    /**
     * 根据id获取客户详情
     * @param id
     * @return
     */
    public Customer getCustomer(Long id){
        // TODO
        String sql = "select * from customer where id=?";
        return DatabaseHelper.queryEntity(Customer.class, sql, new Object[]{id});
    }

    /**
     * 创建客户
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String, Object> fieldMap){
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    /**
     * 更新客户
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(Long id, Map<String, Object> fieldMap){
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    /**
     * 删除客户
     * @param id
     * @return
     */
    public boolean deleteCustomer(Long id){
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
