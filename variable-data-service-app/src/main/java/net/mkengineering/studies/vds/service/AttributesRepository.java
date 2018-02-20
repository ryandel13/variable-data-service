package net.mkengineering.studies.vds.service;

import net.mkengineering.studies.vds.DataResponse;

public interface AttributesRepository {

	public DataResponse getAllData(String vin);

	public DataResponse getDataForAttribute(String vin, String attributeName);

	public DataResponse getHistoryForAttribute(String vin, String attributeName);

	public void putData(String vin, String attributeName, String value, Long timestamp);
	
	public Long lastConnection(String vin);

}
