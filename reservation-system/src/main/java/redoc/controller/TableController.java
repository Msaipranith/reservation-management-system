package redoc.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import redoc.entity.DTable;
import redoc.repository.DTableRepo;
import redoc.service.DTableService;

@Controller
public class TableController {

	private static final String ADMIN_TABLE_LIST = "adminTableList";
	private static final String TABLES = "tables";
	private static final String USER_TABLE_LIST = "userTableList";
	

	private final DTableService dTableService;

	private final DTableRepo dTableRepo;

	public TableController(DTableService dTableService, DTableRepo repo) {
		this.dTableService = dTableService;
        this.dTableRepo = repo;

	}

	// 1st api to call adminTables
	// handler method to handle list students and return mode and view
	@GetMapping("/adminTableList")
	public String listOfTables(Model model) {
		model.addAttribute(TABLES , dTableService.getListTables());
		// in tables list of data is stored

		return ADMIN_TABLE_LIST;
	}

	@GetMapping("/userTableList")
	public String userTableList(Model model) {
		model.addAttribute(TABLES , dTableService.getListTables());
		return USER_TABLE_LIST ;
	}

	@GetMapping("/add/table")
	public String createTableForm(Model model) {

		// create student object to hold student form data
		DTable dTable = new DTable();
		model.addAttribute("tableData", dTable);
		return "adminTable";

	}

	@PostMapping("/saveTable")
	public String saveTableDetails(@ModelAttribute("tableData") DTable table) {
		dTableService.saveTableDetails(table);

		return "redirect:/Tables";
	}

	@PostMapping("/saveAdminTable")
	public String saveAdminTableDetails(@ModelAttribute("tableData") DTable table, Model model) {
		DTable dTable = new DTable(table.getId(), table.getTableNumber(), table.getSeatingCapacity(),
				table.getDuration(), "available", table.getUserName());
		dTableService.saveTableDetails(dTable);

		model.addAttribute(TABLES , dTableService.getListTables());

		return ADMIN_TABLE_LIST;
	}

	@GetMapping("/edit/tables/{id}")
	public String editTable(@PathVariable Long id, Model model) {
		model.addAttribute("table", dTableService.getTableDetailsById(id));

		String time = dTableService.getTableDetailsById(id).getDuration();
		if (time == null) {

			return "edit_admin";
		} else {

			model.addAttribute(TABLES , dTableService.getListTables());
			model.addAttribute("reserveMsg", "can't update the table because its already reserved");
			return "ADMIN_TABLE_LIST";
		}

	}

	@GetMapping("/edit/userTable/{id}")
	public String editUserTable(@PathVariable Long id, Model model) {
		DTable table = dTableService.getTableDetailsById(id);
		model.addAttribute("table", table);
		String status = table.getStatus();

		if ("available".equals(status)) {
			return "editUser";
		} else {
			model.addAttribute(TABLES , dTableService.getListTables());
			model.addAttribute("message", "already registered, go for some other table");

			return USER_TABLE_LIST ;

		}
	}

	@PostMapping("/updateTableById/{id}")
	public String updateAdminTable(@PathVariable Long id, @ModelAttribute("table") DTable dTableData, Model model) {

		// get student from database by id
		DTable dTable = dTableService.getTableDetailsById(id);

		System.out.println("data by id" + dTable);
		System.out.println("data from form " + dTableData);
		dTable.setId(id);
		dTable.setTableNumber(dTableData.getTableNumber());
		dTable.setSeatingCapacity(dTableData.getSeatingCapacity());
//		dTable.setDuration(dTableData.getDuration());
		// dTable.setStatus("available");
		System.out.println("data after updated " + dTable);
		// save updated student object
		dTableService.updateDTable(dTable);
		model.addAttribute(TABLES , dTableService.getListTables());

		return ADMIN_TABLE_LIST;
	}

	@PostMapping("/updateUserTableById/{id}")
	public Object updateUserTable(@PathVariable Long id, @ModelAttribute("table") DTable dTableData, Model model) {

		// get student from database by id
		DTable dTable = dTableService.getTableDetailsById(id);

		String timev = dTableData.getDuration();

		// Parse the time string to a LocalTime object
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		LocalTime time = LocalTime.parse(timev, formatter);

		// Define the time range for comparison
		LocalTime startTime = LocalTime.of(10, 0); // 10:00
		LocalTime endTime = LocalTime.of(18, 0); // 18:00

		// Check if the time is within the specified range
		if (time.isAfter(startTime) && time.isBefore(endTime)) {
			dTable.setId(id);
			dTable.setTableNumber(dTableData.getTableNumber());
			dTable.setSeatingCapacity(dTableData.getSeatingCapacity());
			dTable.setDuration(dTableData.getDuration());
			dTable.setStatus(dTableData.getStatus());
			dTable.setUserName(dTableData.getUserName());

			// save updated student object
			dTableService.updateDTable(dTable);
			model.addAttribute(TABLES , dTableService.getListTables());

			return USER_TABLE_LIST ;

		} else {

			model.addAttribute(TABLES , dTableService.getListTables());
			model.addAttribute("message", "reserve table with in the time");

			return USER_TABLE_LIST ;

		}

	}

	// handler method to handle delete student request
	@GetMapping("/table/{id}")
	public String deleteTable(@PathVariable Long id, Model model) {

		String duration = dTableRepo.findById(id).get().getDuration();

		if (duration == null) {
			dTableService.deleteTableById(id);

			model.addAttribute(TABLES , dTableService.getListTables());



		} else {
			model.addAttribute(TABLES , dTableService.getListTables());
			model.addAttribute("errMsg", "can't delete the table because its already reserved ");

		}
		return ADMIN_TABLE_LIST;
	}

	@GetMapping("/usertable/{id}")
	public String deleteUserTable(@PathVariable Long id, Model model) {
		dTableService.deleteUserTableById(id);
		model.addAttribute("cancelReservation", "reservation cancelled");
		return USER_TABLE_LIST ;
	}

	@PostMapping("/saveTableData")
	public void saveTableData(@RequestBody DTable table) {

		dTableService.saveTableData(table);
	}

}
