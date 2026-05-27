```java
@Entity
public class Department {

    @Id
    private Integer id;

    private String departmentName;
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
     * Returns entities directly.
     * Safer than bidirectional mapping because
     * there is no employees collection inside Department.
     */
    @GetMapping("/entities")
    public List<Department> getDepartmentsEntities() {
        return departmentService.getDepartments();
    }

    /*
     * DTO version.
     * No N + 1 problem because employees are fetched
     * separately in single query.
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
    private final EmployeeRepository employeeRepository;

    /*
     * Returns entities directly.
     * Since Department does not contain employees collection,
     * no lazy loading issue here.
     */
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    /*
     * DTO version without @OneToMany.
     *
     * Flow:
     * 1. Load all departments
     * 2. Load all employees
     * 3. Match employees to departments manually
     * 4. Convert to DTOs
     *
     * No N + 1 problem.
     * Only 2 SQL queries.
     */
    public List<DepartmentDTO> getDepartmentsDto() {
        List<Department> departments = departmentRepository.findAll();
        List<Employee> employees = employeeRepository.findAll();
        List<DepartmentDTO> result = new ArrayList<>();

        for (Department department : departments) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(department.getId());
            departmentDTO.setDepartmentName(department.getDepartmentName());
            
			List<EmployeeDTO> employeeDTOs = new ArrayList<>();
            for (Employee employee : employees) {
                if (employee.getDepartment().getId().equals(department.getId())) {
                    EmployeeDTO employeeDTO = Utils.toEmployeeDto(employee);
                    employeeDTOs.add(employeeDTO);
                }
            }
            departmentDTO.setEmployees(employeeDTOs);
            result.add(departmentDTO);
        }

        return result;
    }
}

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}

public class Utils {

    private Utils() {
    }

    public static DepartmentDTO toDto(Department department, List<EmployeeDTO> employeeDTOs) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setEmployees(employeeDTOs);
        return dto;
    }

    public static EmployeeDTO toEmployeeDto(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmployeeName(employee.getEmployeeName());
        return dto;
    }
}
```