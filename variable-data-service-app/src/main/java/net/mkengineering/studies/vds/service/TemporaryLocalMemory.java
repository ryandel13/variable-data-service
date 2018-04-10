package net.mkengineering.studies.vds.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import net.mkengineering.studies.vds.DataResponse;
import net.mkengineering.studies.vds.Value;
import net.mkengineering.studies.vds.remote.VdsFeign;

@Component
@ConditionalOnProperty(name = "execution.location", havingValue = "local")
public class TemporaryLocalMemory implements AttributesRepository {

	private Map<String, Map<String, DataEntity>> repository = new ConcurrentHashMap<String, Map<String, DataEntity>>();
	private Map<String, Long> lastConnection = new HashMap<String, Long>();
	private Map<Integer, DataEntity> forwarder = new HashMap<>();

	@Autowired
	private VdsFeign vdsFeign;

	public TemporaryLocalMemory() {
		Thread deliverThread = new Thread(new DeliveryThread());
		deliverThread.start();
	}

	@Override
	public DataResponse getAllData(String vin) {
		Map<String, DataEntity> data = repository.get(vin);

		DataResponse dResp = new DataResponse();
		for (DataEntity dataE : data.values()) {
			dResp.getValues().add(dataE.toResponseEntity());
		}
		return dResp;
	}

	@Override
	public DataResponse getDataForAttribute(String vin, String attributeName) {
		DataResponse dResp = new DataResponse();
		if (!repository.containsKey(vin)) {
			return null;
		}
		dResp.getValues().add(repository.get(vin).get(attributeName).toResponseEntity());

		return dResp;
	}

	@Override
	public DataResponse getHistoryForAttribute(String vin, String attributeName) {
		DataResponse dResp = new DataResponse();

		DataEntity dataE = repository.get(vin).get(attributeName);
		if (dataE instanceof HistoricalDataEntity) {
			dResp.getValues().addAll(((HistoricalDataEntity) dataE).getAllData());
		} else {
			dResp.getValues().add(dataE.toResponseEntity());
		}

		return dResp;
	}

	@Override
	public void putData(String vin, String attributeName, String value, Long timestamp) {
		lastConnection.put(vin, System.currentTimeMillis());

		if (!repository.containsKey(vin)) {
			repository.put(vin, new HashMap<String, DataEntity>());
		}

		DataEntity dataE;
		if (!repository.get(vin).containsKey(attributeName) && isHistorical(attributeName)) {
			dataE = new HistoricalDataEntity();
		} else {
			dataE = new DataEntity();
		}
		dataE.setName(attributeName);
		dataE.setValue(value);
		dataE.setTimestamp(timestamp);
		dataE.setType(String.class.getName());
		dataE.setVin(vin);

		if (repository.get(vin).get(attributeName) instanceof HistoricalDataEntity) {
			((HistoricalDataEntity) repository.get(vin).get(attributeName)).add(dataE);
		} else {
			repository.get(vin).put(attributeName, dataE);
		}
		addDataToForwarder(vin, attributeName, value, timestamp);
	}
	
	private void addDataToForwarder(String vin, String attributeName, String value, Long timestamp) {
		DataEntity dataE = new DataEntity();
		
		dataE.setName(attributeName);
		dataE.setValue(value);
		dataE.setTimestamp(timestamp);
		dataE.setType(String.class.getName());
		dataE.setVin(vin);
		forwarder.put(dataE.hashCode(), dataE);
	}

	private boolean isHistorical(String attributeName) {
		if (attributeName.equalsIgnoreCase("temperature_inside"))
			return true;
		if (attributeName.equalsIgnoreCase("temperature_engine"))
			return true;
		return false;
	}

	@Override
	public Long lastConnection(String vin) {
		Long out = 0l;
		if (lastConnection.containsKey(vin)) {
			out = lastConnection.get(vin);
		}
		return out;
	}

	private class DeliveryThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				Set<Integer> forwardedOrOutdated = new HashSet<>();
				for (Entry<Integer, DataEntity> entry : forwarder.entrySet()) {
					if (forwardToCloud(entry.getValue())) {
						entry.getValue().setDelivered(true);
					} else if ((entry.getValue().getTimestamp() + 60 * 1000) < System.currentTimeMillis()) {
						entry.getValue().setDelivered(true);
					}

					if (entry.getValue().getDelivered()) {
						forwardedOrOutdated.add(entry.getKey());
					}
				}

				for (Integer i : forwardedOrOutdated) {
					System.out.println("Removing data with code " + i);
					forwarder.remove(i);
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private Boolean forwardToCloud(DataEntity data) {
			try {
				System.out.println("Forwarding data for attribute " + data.getName());
				
				Value v = new Value();
				v.setTimestamp(data.getTimestamp());
				v.setType(data.getType());
				v.setValue(data.getValue());
				vdsFeign.putData(data.getVin(), data.getName(), v);

				return true;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return false;
		}

	}

}
