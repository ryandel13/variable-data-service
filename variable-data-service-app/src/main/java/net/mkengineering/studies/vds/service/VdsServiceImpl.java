package net.mkengineering.studies.vds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.mkengineering.studies.vds.DataResponse;
import net.mkengineering.studies.vds.Value;

@RestController
public class VdsServiceImpl implements ServiceInterface {

	@Autowired private AttributesRepository repo;
	
	@Override
	public ResponseEntity<DataResponse> getAllData(@PathVariable("vin") String vin) {
		return new ResponseEntity<DataResponse>(repo.getAllData(vin), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DataResponse> getDataForAttribute(@PathVariable("vin") String vin, @PathVariable("attribute") String attribute) {
		DataResponse dataR = repo.getDataForAttribute(vin, attribute);
		if(dataR == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<DataResponse>(dataR, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DataResponse> getHistoryForAttribute(@PathVariable("vin") String vin, @PathVariable("attribute") String attribute) {
		return new ResponseEntity<DataResponse>(repo.getHistoryForAttribute(vin, attribute), HttpStatus.OK);
	}

	@Override
	public void putData(@PathVariable("vin") String vin, @PathVariable("attribute") String attribute, @RequestBody Value value) {
		this.repo.putData(vin, attribute, value.getValue());
	}

	public ResponseEntity<DataResponse> getLastConnection(@PathVariable String vin) {
		DataResponse dR = new DataResponse();
		dR.setTimestamp(this.repo.lastConnection(vin));
		return new ResponseEntity<DataResponse>(dR, HttpStatus.OK);
	}

}
