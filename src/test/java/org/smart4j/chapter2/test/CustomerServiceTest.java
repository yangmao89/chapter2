package org.smart4j.chapter2.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CPR014 on 2017-04-11.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest(){
        this.customerService = new CustomerService();
    }

    @Before
    public void init(){
        DatabaseHelper.executeSqlFile("sql/customer_init.sql");
    }

    //@Test
    public void t1GetCustomerListTest(){
        List<Customer> customerList = customerService.getCustomerList();
        Assert.assertEquals(6, customerList.size());
    }

    //@Test
    public void t2Get2CustomerTest(){
        Customer customer = customerService.getCustomer(1L);
        Assert.assertNotNull(customer);
    }

   // @Test
    public void t3CreateCustomerTest(){
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name", "liu");
        fieldMap.put("contact", "xia");
        fieldMap.put("telephone", "15978945896");
        fieldMap.put("remark", "remark it!");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void updateCustomerTest(){
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name", "liu2");
        boolean result = customerService.updateCustomer(2L, fieldMap);
        Assert.assertTrue(result);
    }

    //@Test
    public void deleteCustomerTest(){
        boolean result = customerService.deleteCustomer(1L);
        Assert.assertTrue(result);
    }
}
