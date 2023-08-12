package redoc.service;

import java.util.List;

import redoc.entity.DTable;

public interface DTableService {

	List<DTable> getListTables();

	void saveTableDetails(DTable table);

	DTable getTableDetailsById(Long id);

	void updateDTable(DTable dTable);

	void deleteTableById(Long id);

	void saveTableData(DTable table);

	void deleteUserTableById(Long id);

	List<DTable> saveTable();

}
