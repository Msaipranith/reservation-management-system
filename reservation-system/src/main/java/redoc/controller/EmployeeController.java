package redoc.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
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
public class EmployeeController {

	@Autowired
	private DTableService dTableService;
	
	@Autowired
	 private DTableRepo repo;

	
	// 1st api to call adminTables
	// handler method to handle list students and return mode and view
	@GetMapping("/adminTableList")
	public String listOfTables(Model model) {
		model.addAttribute("tables", dTableService.getListTables());
		// in tables list of data is stored
		return "adminTableList";
	}

	@GetMapping("/add/table")
	public String createTableForm(Model model) {

		// create student object to hold student form data
		DTable dTable = new DTable();
		model.addAttribute("tableData", dTable);
		return "adminTable";

	}

	@PostMapping("/saveTable")
	public String saveTableDetails(@ModelAttribute("tableData") DTable table) throws ParseException {
//		// 08:00 AM -10:00 AM, 2023-08-10, Wednesday
//		String dateWithTime = table.getDuration();
//		// Split the duration string based on the comma
//		String[] parts = dateWithTime.split(", ");
//
//		// Extract the time part
//		String timeRange = parts[0];
//
//		// Split the time range into start and end times
//		String[] times = timeRange.split("-");
//		String sTime = times[0];
//		String eTime = times[1];
//
//		System.out.println("Start Time: " + sTime);
//		System.out.println("End Time: " + eTime);
//
//		String startTimeStr = "08:00 AM";
//		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//
//		Date startTime = sdf.parse(times[0]);
//
//		// Define the time range
//		Date rangeStartTime = sdf.parse("10:00 AM");
//		Date rangeEndTime = sdf.parse("06:00 PM");
//
//		// Check if the startTime is within the range
//		if (startTime.after(rangeStartTime) && startTime.before(rangeEndTime)) {
//			System.out.println("Time is within range.");
//		} else {
//			System.out.println("Time is not within range.");
//		}

		dTableService.saveTableDetails(table);

		return "redirect:/Tables";
	}

	@PostMapping("/saveAdminTable")
	public String saveAdminTableDetails(@ModelAttribute("tableData") DTable table) throws ParseException {
//		//08:00 AM -10:00 AM, 2023-08-10, Wednesday
//		String dateWithTime=table.getDuration();
//		// Split the duration string based on the comma
//        String[] parts = dateWithTime.split(", ");
//        
//        // Extract the time part
//        String timeRange = parts[0];
//        
//        // Split the time range into start and end times
//        String[] times = timeRange.split("-");
//               String sTime = times[0];
//        String eTime = times[1];
//      
//       System.out.println("Start Time: " + sTime);
//       System.out.println("End Time: " + eTime);
//        
//       String startTimeStr = "08:00 AM";
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//        
//        Date startTime = sdf.parse(times[0]);
//        
//        // Define the time range
//        Date rangeStartTime = sdf.parse("10:00 AM");
//        Date rangeEndTime = sdf.parse("06:00 PM");
//        
//        // Check if the startTime is within the range
//        if (startTime.after(rangeStartTime) && startTime.before(rangeEndTime)) {
//            System.out.println("Time is within range.");
//        } else {
//            System.out.println("Time is not within range.");
//        }
		DTable dTable = new DTable(table.getId(), table.getTableNumber(), table.getSeatingCapacity(),
				table.getDuration(), "available", table.getUserName());
		dTableService.saveTableDetails(dTable);

		return "redirect:/adminTableData";
	}

	@GetMapping("/adminTableData")
	public String adminTableList(Model model) {
		model.addAttribute("tables", dTableService.getListTables());
		return "adminTableList";
	}

	
	@GetMapping("/edit/tables/{id}")
	public String editTable(@PathVariable Long id, Model model) {
		model.addAttribute("table", dTableService.getTableDetailsById(id));

		String time = dTableService.getTableDetailsById(id).getDuration();
		if (time == null) {

			return "edit_admin";
		} else {

			return "redirect:/adminTableData";
		}

	}

	@GetMapping("/edit/userTable/{id}")
	public String editUserTable(@PathVariable Long id, Model model) {
		model.addAttribute("table", dTableService.getTableDetailsById(id));
		String value = dTableService.getTableDetailsById(id).getStatus();
		System.out.println(value);
		if (value.equals("available")) {
			return "editUser";
		} else {
			// model.addAttribute("tables", "table can't be edited because its already
			// reserved");
			return "redirect:/userTableList?reserved";
		}

	}

	@PostMapping("/updateTableById/{id}")
	public String updateAdminTable(@PathVariable Long id, @ModelAttribute("table") DTable dTableData) {

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
		return "redirect:/adminTableData";
	}

	@PostMapping("/updateUserTableById/{id}")
	public String updateUserTable(@PathVariable Long id, @ModelAttribute("table") DTable dTableData) {

		// get student from database by id
		DTable dTable = dTableService.getTableDetailsById(id);
	
		dTable.setId(id);
		dTable.setTableNumber(dTableData.getTableNumber());
		dTable.setSeatingCapacity(dTableData.getSeatingCapacity());
		dTable.setDuration(dTableData.getDuration());
		dTable.setStatus(dTableData.getStatus());
		dTable.setUserName(dTableData.getUserName());
	
		// save updated student object
		dTableService.updateDTable(dTable);
		return "redirect:/userTableList";
	}

	// handler method to handle delete student request
	@GetMapping("/table/{id}")
	public String deleteTable(@PathVariable Long id) {

		String duration = repo.findById(id).get().getDuration();

		if (duration == null) {
			dTableService.deleteTableById(id);
			return "redirect:/adminTableData";
		} else {
			return "redirect:/adminTableData";
		}

	}

	@GetMapping("/usertable/{id}")
	public String deleteUserTable(@PathVariable Long id) {
		dTableService.deleteUserTableById(id);
		return "redirect:/userTableList";
	}

	@PostMapping("/saveTableData")
	public void saveTableData(@RequestBody DTable table) {

		dTableService.saveTableData(table);
	}

	@GetMapping("/userTableList")
	public String getUserTableList(Model model) {

		model.addAttribute("tables", dTableService.getListTables());
		return "userTableList";
	}
}
