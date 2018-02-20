package net.mkengineering.studies.vds.service;

import lombok.Getter;
import lombok.Setter;
import net.mkengineering.studies.vds.ResponseEntity;

@Getter
@Setter
public class DataEntity {
	
		private String name;
		
		private String value;
		
		private Long timestamp;
		
		private String type;
		
		public ResponseEntity toResponseEntity() {
			ResponseEntity dataR = new ResponseEntity();
			dataR.setName(name);
			dataR.setValue(this.getValue());
			dataR.setTimestamp(this.getTimestamp());
			dataR.setType(type);
			
			return dataR;
		}
}
