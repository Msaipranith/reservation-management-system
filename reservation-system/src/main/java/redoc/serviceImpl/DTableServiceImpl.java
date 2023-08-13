package redoc.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redoc.entity.DTable;
import redoc.repository.DTableRepo;
import redoc.service.DTableService;

@Service
public class DTableServiceImpl implements DTableService {

	@Autowired
	DTableRepo dTableRepo;

	@Override
	public List<DTable> getListTables() {
		// TODO Auto-generated method stub
		return dTableRepo.findAll();
	}

	@Override
	public void saveTableDetails(DTable table) {
		dTableRepo.save(table);

	}

	@Override
	public DTable getTableDetailsById(Long id) {
		// TODO Auto-generated method stub
		return dTableRepo.findById(id).get();
	}

	@Override
	public void updateDTable(DTable dTable) {
		// TODO Auto-generated method stub
		dTableRepo.save(dTable);

	}

	@Override
	public void deleteTableById(Long id) {
		// TODO Auto-generated method stub
		dTableRepo.deleteById(id);
	}

	@Override
	public void saveTableData(DTable table) {
		// TODO Auto-generated method stub
		dTableRepo.save(table);

	}

	@Override
	public void deleteUserTableById(Long id) {
		// TODO Auto-generated method stub
		DTable data = dTableRepo.findById(id).get();
		DTable dTable = new DTable(data.getId(), data.getTableNumber(), data.getSeatingCapacity(), null, "available",
				null);
		dTableRepo.save(dTable);
	}

	@Override
	public List<DTable> saveTable() {
		return dTableRepo.findAll();
	}

}
