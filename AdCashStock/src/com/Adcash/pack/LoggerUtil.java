package com.Adcash.pack;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class LoggerUtil {
	public static void initLogger() {
	    try {
	      String filePath = "D:/AdCash/Stocklog.log";
	      PatternLayout layout = new PatternLayout("%-4r [%t] %-5p %c %x - %m%n");
	      RollingFileAppender appender = new RollingFileAppender(layout, filePath);
	      appender.setName("myFirstLog");
	      appender.setMaxFileSize("1MB");
	      appender.activateOptions();
	      Logger.getRootLogger().addAppender(appender);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
}
