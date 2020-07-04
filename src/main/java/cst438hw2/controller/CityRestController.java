package cst438hw2.controller;

import cst438hw2.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cst438hw2.domain.*;
import cst438hw2.service.CityService;

@RestController
public class CityRestController {
	
	@Autowired
	private CityService cityService;

	@Autowired
	private WeatherService weatherService;
	
	@GetMapping("/api/cities/{city}")
	public ResponseEntity<CityInfo> getWeather(@PathVariable("city") String cityName) {
		CityInfo restCity = cityService.getCityInfo(cityName);

		// look up city info from database.  Might be multiple cities with same name.
		if (restCity == null) {
			// city name not found.  Send 404 return code.
			return new ResponseEntity<CityInfo>( HttpStatus.NOT_FOUND);

		} else {

			// get current weather
			TempAndTime weather = weatherService.getTempAndTime(cityName);
			// convert temp from degrees Kelvin to degrees Fahrenheit
			double tempF = Math.round((weather.getTemp() - 273.15) * 9.0/5.0 + 32.0);
			restCity.setTemp(tempF);

			// return 200 status code (OK) and city information in JSON format
			return new ResponseEntity<CityInfo>(restCity, HttpStatus.OK);
		}
	}
	
}
