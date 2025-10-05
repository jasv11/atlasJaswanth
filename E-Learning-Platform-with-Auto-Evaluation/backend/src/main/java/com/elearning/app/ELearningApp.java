package com.elearning.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import javax.servlet.MultipartConfigElement;

public class ELearningApp {

    public static void main(String[] args) {
        try {
            Server server = new Server(8080);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");

            ServletHolder uploadHolder = new ServletHolder(new StudentAssignmentServlet());
            context.addServlet(uploadHolder, "/upload");

            uploadHolder.getRegistration().setMultipartConfig(
                new MultipartConfigElement(
                    System.getProperty("java.io.tmpdir"),
                    10485760,  // max file size: 10MB
                    10485760,  // max request size: 10MB
                    262144     // file size threshold: 256KB
                )
            );

            context.addServlet(new ServletHolder(new ScoreServlet()), "/scores");
            context.addServlet(new ServletHolder(new LogServlet()), "/logs");

            context.addServlet(new ServletHolder(new StudentServlet()), "/api/student/*");

            ServletHolder adminHolder = new ServletHolder(new AdminServlet());
            context.addServlet(adminHolder, "/admin");
            context.addServlet(adminHolder, "/admin/*");

            adminHolder.getRegistration().setMultipartConfig(
                new MultipartConfigElement(
                    System.getProperty("java.io.tmpdir"),
                    10485760,  // max file size: 10MB
                    10485760,  // max request size: 10MB
                    262144     // file size threshold: 256KB
                )
            );

            server.setHandler(context);
            server.start();

            System.out.println("E-Learning Platform started on port 8080");
            System.out.println("Student API: http://localhost:8080/api/student");
            System.out.println("Student Upload: http://localhost:8080/upload");
            System.out.println("Teacher Admin: http://localhost:8080/admin");
            System.out.println("Scores: http://localhost:8080/scores");
            System.out.println("Logs: http://localhost:8080/logs");

            server.join();

        } catch (Exception e) {
            System.out.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}