package com.example.rqchallenge;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/employees")
public class IEmployeeController {
	@Autowired
	private EmployeeRepository employeeRepository;
    @PostMapping("/createEmployee")
    public ResponseEntity<String> createEmployee(@RequestBody Map<String, String> employeeInput) {
        for ( Map.Entry<String, String> entry : employeeInput.entrySet()) {
			String name = entry.getKey();
			String values = entry.getValue();
		Employee n = new Employee();
		n.setName(name);
		String tab[] = values.split(",");
		System.out.printf("%s,%s, %s\n", name, tab[0], tab[1]);
		int sal = 0;
		if (tab.length >= 1){
			try {
				sal = Integer.parseInt(tab[0]);
			} catch (NumberFormatException e) {
				System.out.println("Invalid salary format");
				e.printStackTrace();
				return new ResponseEntity<String>("Failed to create employee", HttpStatus.NOT_FOUND);
			}
			n.setSalary(sal);
		}
		if (tab.length >= 2){
			n.setAge(tab[1]);
		}
		employeeRepository.save(n);
		}
		return new ResponseEntity<String>("Success", HttpStatus.OK);
    };


	@GetMapping(path="/getAll")
    public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees =  employeeRepository.findAll();
		if (employees.isEmpty()) {
			return new ResponseEntity<>(employees, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(employees, HttpStatus.OK);
	};

	@GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id){
		int employeeId = 0;
		try {
			employeeId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number format");
			e.printStackTrace();
		}
		Employee employee = new Employee();
		Optional<Employee> em = employeeRepository.findById(employeeId);
		if (em.isPresent()){
			employee = em.get();
			return new ResponseEntity<Employee>(employee, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(employee, HttpStatus.NOT_FOUND);
		}
	};

	@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id){
		int employeeId = 0;
		try {
			employeeId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number format");
			e.printStackTrace();
			return new ResponseEntity<>("Invalid employee id", HttpStatus.NOT_FOUND);
		}
		Employee employee = new Employee();
		String name;
		Optional<Employee> em = employeeRepository.findById(employeeId);
		if (em.isPresent()){
			employee = em.get();
			name = employee.getName();
		} else {
			return new ResponseEntity<>("Employee id not found", HttpStatus.NOT_FOUND);
		}
		employeeRepository.deleteById(employeeId);
		return new ResponseEntity<>(name, HttpStatus.OK);
	};

	@GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
		List<Employee> employees = employeeRepository.findByNameContaining(searchString);
		if (employees.isEmpty()) {
			return new ResponseEntity<>(employees, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(employees, HttpStatus.OK);
	};

	public List<Employee> getSalaries(int size) {
		Sort sort = Sort.by(Sort.Direction.DESC, "salary");
        List<Employee> employees = employeeRepository.findAll(sort);
		if (employees.size() < size) {
			return employees;
		}
		return employees.subList(0, size);
    }

	@GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		List<Employee> employees = getSalaries(1);
		if (!employees.isEmpty()) {
			int highestSalary = 0;
			try {
				highestSalary = employees.get(0).getSalary();
			} catch (NumberFormatException e) {
				System.out.println("Invalid salary format");
				e.printStackTrace();
				return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(highestSalary, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
		}
	};

	@GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		List<Employee> employees = getSalaries(10);
		if (!employees.isEmpty()) {
			ListIterator<Employee> iterator = employees.listIterator();
			List<String> employeeNames = new ArrayList<>();
			while (iterator.hasNext()) {
				employeeNames.add(iterator.next().getName());
			}
			return new ResponseEntity<>(employeeNames, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	};
}
