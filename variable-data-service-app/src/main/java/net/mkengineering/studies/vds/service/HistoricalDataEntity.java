package net.mkengineering.studies.vds.service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;

import net.mkengineering.studies.vds.ResponseEntity;
import net.mkengineering.studies.vds.config.VdsConfig;
import net.mkengineering.studies.vds.config.VdsConfigHelper;

public class HistoricalDataEntity extends DataEntity {
			
			private SortedMap<Long, String> map = new TreeMap<>();
			
			@Autowired
			private VdsConfig vdsConfig;
			
			@Override
			public String getValue() {
				return map.get(map.lastKey());
			}
			
			@Override
			public Long getTimestamp() {
				return map.lastKey();
			}
			
			public List<ResponseEntity> getAllData() {
				List<ResponseEntity> out = new ArrayList<>();
				
				for(Long key : map.keySet()) {
					ResponseEntity respE = new ResponseEntity();
					respE.setName(this.getName());
					respE.setType(this.getType());
					respE.setTimestamp(key);
					respE.setValue(map.get(key));
					
					out.add(respE);
				}
				
				return out;
			}
			
			public void add(DataEntity dataE) {
				if(map.size() >= VdsConfigHelper.getConfig().getMaxHistory()) {
					map.remove(map.firstKey());
				}
				this.map.put(System.currentTimeMillis(), dataE.getValue());
			}

}
