package com.ibm.websphere.svt.gs.gsdb.session.view;

import com.ibm.websphere.svt.gs.gsdb.entities.Customer;
import com.ibm.websphere.svt.gs.wrapper.CustomerWrapper;
import java.util.List;

public interface CustomerSessionLocal {

	String createCustomer(Customer customer) throws Exception;

	String deleteCustomer(Customer customer) throws Exception;

	String updateCustomer(Customer customer) throws Exception;

	Customer findCustomerByCustId(String custId);

	Customer getNewCustomer();

	List<Customer> getAllCustomers();

	CustomerWrapper getWrapper(Customer customer);

}
