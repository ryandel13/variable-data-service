package net.mkengineering.studies.vds.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.mkengineering.studies.vds.DataResponse;
import net.mkengineering.studies.vds.Value;

public interface ServiceInterface {

	public final String CONTEXT = "vehicle";
	
	@RequestMapping(value = CONTEXT + "/{vin}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<DataResponse> getAllData(@PathVariable("vin") String vin);
	
	@RequestMapping(value = CONTEXT + "/{vin}/{attribute}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<DataResponse> getDataForAttribute(@PathVariable("vin") String vin, @PathVariable("attribute") String attribute);
	
	@RequestMapping(value = CONTEXT + "/{vin}/{attribute}/history", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<DataResponse> getHistoryForAttribute(@PathVariable("vin") String vin, @PathVariable("attribute") String attribute);
	
	@RequestMapping(value = CONTEXT + "/{vin}/{attribute}/", method = RequestMethod.PUT)
	@ResponseBody
	public void putData(@PathVariable("vin") String vin, @PathVariable("attribute") String attribute, @RequestBody Value value);
	
}
