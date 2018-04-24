package com.mourya.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;

public class GenericRestTemplateApplication {

	public static void main(String[] args) {
		GenericRestTemplateApplication app = new GenericRestTemplateApplication();
		ResponseErrorHandler responseHandler = new ResponseErrorHandler() {

			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {

				if (response.getStatusCode() != HttpStatus.OK) {
					System.out.println(response.getStatusText());
				}
				return response.getStatusCode() == HttpStatus.OK ? false : true;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				// TODO Auto-generated method stub

			}
		};

		try {
			app.getEmployees(responseHandler);

			Employee employee = app.getEmployee("E01", responseHandler);

			employee.setEmpNo("GHK1");
			employee = app.createEmployee(employee, responseHandler);

			app.getEmployees(responseHandler);

			employee.setEmpName("KARIM");
			employee = app.updateEmployee(employee, responseHandler);

			app.getEmployees(responseHandler);

			app.deleteEmployee(employee, responseHandler);

			app.getEmployees(responseHandler);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void getEmployees(ResponseErrorHandler responseHandler) {
		List<Employee> employees = new GenericRestClient<String, Employee>().execute(
				new RequestDetails("http://localhost:8082/employees", HttpMethod.GET), " ", responseHandler,
				new ParameterizedTypeReference<List<Employee>>() {
				});
		System.out.println("\n\ngetEmployees : " + employees + "\n\n");
	}

	private Employee getEmployee(String id, ResponseErrorHandler responseHandler)
			throws ResourceAccessException, Exception {
		Employee employee = new GenericRestClient<String, Employee>().execute(
				new RequestDetails("http://localhost:8082/employee/" + id, HttpMethod.GET), " ", responseHandler,
				Employee.class);
		System.out.println("\n\ngetEmployeeById : " + employee + "\n\n");
		return employee;
	}

	private Employee createEmployee(Employee employee, ResponseErrorHandler responseHandler)
			throws ResourceAccessException, Exception {
		employee.setEmpNo("GHK1");
		employee = new GenericRestClient<Employee, Employee>().execute(
				new RequestDetails("http://localhost:8082/employee", HttpMethod.POST), employee, responseHandler,
				Employee.class);

		System.out.println("\n\ncreateEmployee : " + employee + "\n\n");
		return employee;
	}

	private Employee updateEmployee(Employee employee, ResponseErrorHandler responseHandler)
			throws ResourceAccessException, Exception {
		employee = new GenericRestClient<Employee, Employee>().execute(
				new RequestDetails("http://localhost:8082/employee", HttpMethod.PUT), employee, responseHandler,
				Employee.class);

		System.out.println("\n\nupdateEmployee : " + employee + "\n\n");
		return employee;
	}

	private void deleteEmployee(Employee employee, ResponseErrorHandler responseHandler)
			throws ResourceAccessException, Exception {
		employee = new GenericRestClient<Employee, Employee>().execute(
				new RequestDetails("http://localhost:8082/employee/" + employee.getEmpNo(), HttpMethod.DELETE),
				employee, responseHandler, Employee.class);

		System.out.println("\n\ndeleteEmployee : " + employee + "\n\n");
	}

}
