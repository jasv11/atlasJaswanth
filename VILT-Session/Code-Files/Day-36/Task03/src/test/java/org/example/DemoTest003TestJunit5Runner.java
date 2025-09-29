package org.example;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;  
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class DemoTest003TestJunit5Runner {

    public static void main(String[] args) {
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        launcher.execute(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(DemoTest003TestCases.class))
                .build());

        TestExecutionSummary summary = listener.getSummary();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        summary.printTo(printWriter);
        printWriter.flush();
        System.out.println(stringWriter.toString());
    }
}