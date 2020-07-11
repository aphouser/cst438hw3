package cst438hw3.service;

import java.util.List;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cst438hw3.domain.*;

@Service
public class CityService {
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private WeatherService weatherService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private FanoutExchange fanout;


	public CityInfo getCityInfo(String cityName) {

		List<City> city = cityRepository.findByName(cityName);
		// check to see if there is even a return
		if (city.size() == 0) {
			return null;
		}
		City tempCity = city.get(0);
		Country country = countryRepository.findByCode(tempCity.getCountryCode());
		TempAndTime weather = weatherService.getTempAndTime(cityName);

		return new CityInfo(tempCity, country.getName(), weather.getFarTemp(), weather.getStringTime());
	}

	public void requestReservation(
			String cityName,
			String level,
			String email) {
		String msg  = "{\"cityName\": \""+ cityName +
				"\" \"level\": \""+level+
				"\" \"email\": \""+email+"\"}" ;
		System.out.println("Sending message:"+msg);
		rabbitTemplate.convertSendAndReceive(
				fanout.getName(),
				"",   // routing key none.
				msg);
	}


}
