package org.kie.server.swarm.ml;


import java.util.Arrays;
import java.util.HashMap;

import org.kie.server.swarm.AbstractKieServerMain;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.config.logging.Level;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.logging.LoggingFraction;

public class KieServerMain extends AbstractKieServerMain {
    
    public static void main(String[] args) throws Exception {
    	Swarm container = new Swarm();

        System.out.println("\tBuilding kie server deployable...");
        JAXRSArchive deployment = createDeployment(container);

        container.fraction(
            new LoggingFraction()
                .consoleHandler("CONSOLE", c -> {
                    c.level(Level.INFO);
                    c.formatter("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n");
                })
                .rootLogger(Level.INFO, "CONSOLE")
        );
        
        System.out.println("\tStaring Wildfly Swarm....");
        container.start();    
        
        System.out.println("\tConfiguring kjars to be auto deployed to server " + Arrays.toString(args));
        installKJars(args);
        
        System.out.println("\tDeploying kie server ....");
        container.deploy(deployment);
    }
}
