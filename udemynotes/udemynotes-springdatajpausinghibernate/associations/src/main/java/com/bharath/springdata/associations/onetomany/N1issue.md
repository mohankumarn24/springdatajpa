```java
@Entity
public class Department {

    @Id
    private Integer id;

    private String departmentName;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();
}

@Entity
public class Employee {

    @Id
    private Integer id;

    private String employeeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}

@Data
public class DepartmentDTO {

    private Integer id;

    private String departmentName;

    private List<EmployeeDTO> employees;
}

@Data
public class EmployeeDTO {

    private Integer id;

    private String employeeName;
}

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /*
     * Not recommended:
     * - returns entities directly
     * - may trigger lazy loading during serialization
     * - may cause recursion / N+1
     */
    /*
    @GetMapping("/entities")
    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }
    */

    /*
     * Demonstrates N + 1 problem:
	 * - findAll() loads only departments
	 * - department.getEmployees() triggers lazy loading per department
     */
    @GetMapping("/n1")
    public List<DepartmentDTO> getDepartmentsN1() {
        return departmentService.getDepartmentsDtoN1();
    }
	
    /*
     * Optimized version:
     * - uses DTOs
     * - uses fetch join
     * - avoids N + 1 problem
     */
    @GetMapping
    public List<DepartmentDTO> getDepartments() {
        return departmentService.getDepartmentsDto();
    }
}


@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<Department> getDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments;
    }

	public List<DepartmentDTO> getDepartmentsDtoN1() {
        List<Department> departments = departmentRepository.findAll();
		return departments.stream()
						  .map(Utils::toDto)						// N + 1 problem
						  .collect(Collectors.toList());
    }
	
	public List<DepartmentDTO> getDepartmentsDto() {
		List<Department> departments = departmentRepository.findAllDepartmentsWithEmployees();
		return departments.stream()
						  .map(Utils::toDto)
						  .collect(Collectors.toList());
	}
}

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	@Query("""
		SELECT DISTINCT d
		FROM Department d
		LEFT JOIN FETCH d.employees
	""")
	List<Department> findAllDepartmentsWithEmployees();
}


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}


public class Utils {

    private Utils() {
    }

    public static DepartmentDTO toDto(
            Department department) {

        DepartmentDTO dto = new DepartmentDTO();

        dto.setId(department.getId());
        dto.setDepartmentName(
                department.getDepartmentName());

        List<EmployeeDTO> employeeDTOs =
                department.getEmployees()
                          .stream()
                          .map(Utils::toEmployeeDto)
                          .toList();

        dto.setEmployees(employeeDTOs);

        return dto;
    }

    public static EmployeeDTO toEmployeeDto(
            Employee employee) {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setId(employee.getId());
        dto.setEmployeeName(
                employee.getEmployeeName());

        return dto;
    }
}
```