package cst438hw3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cst438hw3.domain.City;
import cst438hw3.domain.CityInfo;
import cst438hw3.domain.CityRepository;
import cst438hw3.domain.Country;
import cst438hw3.domain.CountryRepository;
import cst438hw3.domain.TempAndTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
 
@SpringBootTest
public class CityServiceTest {

	@MockBean
	private WeatherService weatherService;
	
	@Autowired
	private CityService cityService;
	
	@MockBean
	private CityRepository cityRepository;
	
	@MockBean
	private CountryRepository countryRepository;

	
	@Test
	public void contextLoads() {
	}


	@Test
	public void testCityFound() throws Exception {
		Country country = new Country("TST", "TestCountry");
		City city = new City(1, "TestCity", "TST", "DistrictTest", 123456);

		// Create an empty cities list to be used on the given
		List<City> cities = new ArrayList<City>();
		cities.add(city);

		// create the stub calls and return data for weather service
		//  when the getTempAndTime method is called with name parameter "TestCity",
		//  the stub will return the given temp (in degrees Kelvin) and time.
		given(weatherService.getTempAndTime("TestCity"))
				.willReturn(new TempAndTime(295.37, 1593890000, -14400));

		// stub for the CityRepository.  When given input parm of "TestCity",
		// it will return a list of cities.
		given(cityRepository.findByName("TestCity")).willReturn(cities);

		// stub for the CountryRepository.  When given input parm of "TST",
		// it will return the country created above.
		given(countryRepository.findByCode("TST")).willReturn(country);

		CityInfo cityResult = cityService.getCityInfo("TestCity");
		CityInfo expectedResult = new CityInfo(1, "TestCity", "TST", "TestCountry", "DistrictTest",
				123456, 72.0, "15:13");

		// Assertions
		assertThat(cityResult).isEqualTo(expectedResult);

	}
	
	@Test
	public void  testCityNotFound() {
		Country country = new Country("TST", "TestCountry");
		City city = new City(1, "TestCity", "TST", "DistrictTest", 123456);

		// Create an empty cities list to be used on the given
		List<City> cities = new ArrayList<City>();
		cities.add(city);

		// create the stub calls and return data for weather service
		//  when the getTempAndTime method is called with name parameter "TestCity",
		//  the stub will return the given temp (in degrees Kelvin) and time.
		given(weatherService.getTempAndTime("TestCity"))
				.willReturn(new TempAndTime(295.37, 1593890000, -14400));

		// stub for the CityRepository.  When given input parm of "TestCity",
		// it will return a list of cities.
		given(cityRepository.findByName("TestCity")).willReturn(cities);

		// stub for the CountryRepository.  When given input parm of "TST",
		// it will return the country created above.
		given(countryRepository.findByCode("TST")).willReturn(country);

		// using a bad city here 'badTestCity' so look for a negative test
		CityInfo cityResult = cityService.getCityInfo("badTestCity");
		CityInfo expectedResult = null;

		// Assertions
		assertThat(cityResult).isEqualTo(expectedResult);
	}

	@Test
	public void  testCityMultiple() {
		// create 3 countries to simulate cities with the same name in multiple countries
		Country country = new Country("TST", "TestCountry");
		Country country2 = new Country("TST2", "TestCountry2");
		Country country3 = new Country("TST3", "TestCountry3");

		// create cities for each country
		City city = new City(1, "TestCity", "TST", "DistrictTest", 123456);
		City city2 = new City(1, "TestCity", "TST2", "DistrictTest2", 1234562);
		City city3 = new City(1, "TestCity", "TST3", "DistrictTest3", 1234563);

		// Create an empty cities list to be used on the given
		List<City> cities = new ArrayList<City>();
		cities.add(city);
		cities.add(city2);
		cities.add(city3);

		// create the stub calls and return data for weather service
		//  when the getTempAndTime method is called with name parameter "TestCity",
		//  the stub will return the given temp (in degrees Kelvin) and time.
		given(weatherService.getTempAndTime("TestCity"))
				.willReturn(new TempAndTime(295.37, 1593890000, -14400));

		// stub for the CityRepository.  When given input parm of "TestCity",
		// it will return a list of cities populated above by our multiple cities.
		given(cityRepository.findByName("TestCity")).willReturn(cities);

		// stub for the CountryRepository.  When given input parm of country code,
		// it will return the country created above.
		given(countryRepository.findByCode("TST")).willReturn(country);
		given(countryRepository.findByCode("TST2")).willReturn(country2);
		given(countryRepository.findByCode("TST3")).willReturn(country3);

		CityInfo cityResult = cityService.getCityInfo("TestCity");
		CityInfo expectedResult = new CityInfo(1, "TestCity", "TST", "TestCountry", "DistrictTest",
				123456, 72.0, "15:13");

		// Assertions
		assertThat(cityResult).isEqualTo(expectedResult);
	}
}