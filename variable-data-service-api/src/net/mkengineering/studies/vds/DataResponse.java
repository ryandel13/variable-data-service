package net.mkengineering.studies.vds;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DataResponse {

	private Long timestamp;
	
	public Long getTimestamp() {
		if(timestamp == null) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}
	
	List<ResponseEntity> values = new ArrayList<>();
	
}
