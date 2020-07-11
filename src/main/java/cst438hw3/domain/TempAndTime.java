package cst438hw3.domain;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneOffset;

public class TempAndTime {
	public double temp;
	public long time;
	public int timezone;

	public TempAndTime(double temp, long time, int timezone){
		this.temp = temp;
		this.time = time;
		this.timezone = timezone;
	}

	public double getTemp() { return temp; }
	public void setTemp(double temp) { this.temp = temp; }

	public double getFarTemp() {
		Double farTemp = (getTemp()-273.15) * (9.0 / 5.0) + 32;
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(farTemp));
	}


	public long getTime() { return time; }
	public void setTime(long time) { this.time = time; }

	public String getStringTime() {
		// adjust for timezone
		long timeOffset = getTime() + getTimezone();
		Instant unixTime = Instant.ofEpochSecond(timeOffset);
		int hour = unixTime.atZone(ZoneOffset.UTC).getHour();
		int minute = unixTime.atZone(ZoneOffset.UTC).getMinute();
		String strTime = hour + ":" + minute;
		return strTime;
	}


	public int getTimezone() { return timezone; }
	public void setTimezone(int timezone) { this.timezone = timezone; }

 }
