package net.mkengineering.studies.vds.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import net.mkengineering.studies.vds.DataResponse;

@Component
@ConditionalOnProperty(name="execution.location", havingValue="cloud")
public class MockMemory implements AttributesRepository{

	Map<String, Map<String, DataEntity>> repository = new HashMap<String, Map<String, DataEntity>>();
	Map<String, Long> lastConnection = new HashMap<String, Long>();
	
	@Override
	public DataResponse getAllData(String vin) {
		Map<String, DataEntity> data = repository.get(vin);
		
		DataResponse dResp = new DataResponse();
		for(DataEntity dataE : data.values()) {
			dResp.getValues().add(dataE.toResponseEntity());
		}
		return dResp;
	}

	@Override
	public DataResponse getDataForAttribute(String vin, String attributeName) {
		DataResponse dResp = new DataResponse();
		if(!repository.containsKey(vin)) {
			return null;
		}
		dResp.getValues().add(repository.get(vin).get(attributeName).toResponseEntity());
		
		return dResp;
	}

	@Override
	public DataResponse getHistoryForAttribute(String vin, String attributeName) {
		DataResponse dResp = new DataResponse();
		
		DataEntity dataE = repository.get(vin).get(attributeName);
		if(dataE instanceof HistoricalDataEntity) {
			dResp.getValues().addAll(((HistoricalDataEntity) dataE).getAllData());
		}
		else {
			dResp.getValues().add(dataE.toResponseEntity());
		}
		
		return dResp;
	}

	@Override
	public void putData(String vin, String attributeName, String value, Long timestamp) {
		lastConnection.put(vin, System.currentTimeMillis());
		
		if(!repository.containsKey(vin)) {
			repository.put(vin, new HashMap<String, DataEntity>());
		}
		
		DataEntity dataE;
		if(!repository.get(vin).containsKey(attributeName) && isHistorical(attributeName)) {
			dataE = new HistoricalDataEntity();
		}
		else {
			dataE = new DataEntity();
		}
		dataE.setName(attributeName);
		dataE.setValue(value);
		dataE.setTimestamp(timestamp);
		dataE.setType(String.class.getName());
		
		if(repository.get(vin).get(attributeName) instanceof HistoricalDataEntity) {
			((HistoricalDataEntity)repository.get(vin).get(attributeName)).add(dataE);
		} else {
			repository.get(vin).put(attributeName, dataE);
		}
	}

	private boolean isHistorical(String attributeName) {
		if(attributeName.equalsIgnoreCase("temperature_inside")) return true;
		if(attributeName.equalsIgnoreCase("temperature_engine")) return true;
		return false;
	}

	@Override
	public Long lastConnection(String vin) {
		Long out = 0l;
		if(lastConnection.containsKey(vin)) {
			out = lastConnection.get(vin);
		}
		return out;
	}

}
