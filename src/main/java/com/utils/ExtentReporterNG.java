package com.utils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExtentReporterNG implements IReporter {

        private ExtentReports extent;

        public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
            extent = new ExtentReports(outputDirectory + File.separator + "extent-report.html", true);

            for (ISuite suite : suites) {
                System.out.println(suites);
                Map<String, ISuiteResult> result = suite.getResults();
                System.out.println(suite.getResults());
                for (ISuiteResult r : result.values()) {
                    ITestContext context = r.getTestContext();
                    System.out.println(context);
                    buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                    buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                    buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
                }
            }

            extent.flush();
            extent.close();
        }

        private void buildTestNodes(IResultMap tests, LogStatus status) {
            ExtentTest test;

            if (tests.size() > 0) {
                for (ITestResult result : tests.getAllResults()) {
                    test = extent.startTest(result.getMethod().getMethodName());
                    test.setStartedTime(getTime(result.getStartMillis()));
                    test.setEndedTime(getTime(result.getEndMillis()));
                    if (result.getParameters() != null && result.getParameters().length > 0) {
                        test.setDescription(result.getParameters()[0].toString());
                    } else {
                        test.setDescription(result.getMethod().getMethodName());
                    }
                    for (String group : result.getMethod().getGroups())
                        test.assignCategory(group);

                    if (result.getThrowable() != null) {
                        test.log(status, result.getThrowable());
                    } else {
                        test.log(status, "Test " + test.getDescription() + " " + status.toString().toLowerCase() + "ed");
                    }

                    extent.endTest(test);
                }
            }
        }

        private Date getTime(long millis) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar.getTime();
        }

    }
