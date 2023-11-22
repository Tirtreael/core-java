package org.ignis.driver.minebench;

import org.ignis.driver.api.*;

public class Driver {
    public static void main(String[] args) throws InterruptedException {
        String blocksFile = "50k_blocks.csv";
        int minePartitions = 0;
        int cores = 1;

        if (args.length > 0)
            blocksFile = args[0];
        if (args.length > 1)
            minePartitions = Integer.parseInt(args[1]);
        if (args.length > 2)
            cores = Integer.parseInt(args[2]);

        //Initialization of the framework
        Ignis.getInstance().start();
        // Resources/Configuration of the cluster
        IProperties props = new IProperties();
        props.set("ignis.executor.image", "ignishpc/java");
        props.set("ignis.executor.instances", "1");
        props.set("ignis.executor.cores", Integer.toString(cores));
        props.set("ignis.executor.memory", "1GB");
        props.set("ignis.modules.load.type", "false");
        props.set("ignis.transport.minimal", "1GB");
        // Construction of the cluster
        ICluster cluster = new ICluster(props, "");
        // Initialization of a Python Worker in the cluster
        IWorker worker = new IWorker(cluster, "java", "", 0, 1);

        ISource iSourceMap = new ISource("ignis-core-java-1.0-minebenchFunctions.jar:org.ignis.driver.minebench.Minebench");


        // Task 1 - Tokenize text into pairs ('word', 1)
        IDataFrame text = worker.textFile(blocksFile, minePartitions);
        // words = text.flatmap(lambda line: [(word, 1) for word in line.split()])
        IDataFrame words = text.map(iSourceMap);
        // Print results to file
        //wordsPair = words.toPair()
        //worker.partitionTextFile("words.txt")
        words.saveAsTextFile("output.txt");

//        words.saveAsTextFile("output2.txt");
        //text.saveAsJsonFile("text.json")
        // Stop the framework
        Ignis.getInstance().stop();
    }

}
