package org.example.minebench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class DriverSpark {
    private static final Logger LOGGER = LogManager.getLogger();

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
//        if (args.length > 3)
//            Minebench.setDefaultBits(Integer.decode(args[3]));


        LOGGER.info("Blocks file: {}", blocksFile);
        LOGGER.info("minPartitions: {}", minePartitions);
        LOGGER.info("cores: {}", cores);
        LOGGER.info("difficulty: {}", Minebench.getDefaultBits());

        /*//Initialization of the framework
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
        // Initialization of a Java Worker in the cluster
        IWorker worker = new IWorker(cluster, "java", "", 0, 1);
        worker.start();
        // Iniciar contador
        long startTime = System.nanoTime();
        // Load MineBench funcion
        ISource iSourceMap = new ISource("ignis-core-java-1.0-minebenchFunctions.jar:org.ignis.driver.minebench.Minebench");
        // Load blocks file
        IDataFrame text = worker.textFile(blocksFile, minePartitions);
        // Launch maps
        IDataFrame words = text.map(iSourceMap);
        // Print results to file
        words.saveAsTextFile("output.txt");
        // Parar contador
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        LOGGER.info("Elapsed time: {}", (float) (elapsedTime / 1000000000));
        // Stop the framework
        Ignis.getInstance().stop();*/


        SparkConf sparkConf = new SparkConf().setAppName("SparkExample").setMaster("local[" + cores + "]");
        JavaSparkContext sc;
        try {
            sc = new JavaSparkContext(sparkConf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Iniciar contador
        long startTime = System.nanoTime();

        // Read input data from a text file
        JavaRDD<String> lines = sc.textFile("/home/miguelr/Documents/ignis-deploy/50k_blocks.csv", minePartitions);

        // Perform transformations and actions
        JavaRDD<String> minebenchMap = lines.map(Minebench::call);

        minebenchMap.saveAsTextFile("output.txt");

        // Parar contador
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        LOGGER.info("Elapsed time: {}", (float) (elapsedTime / 1000000000));

        // Stop Spark context
        sc.stop();
    }

}
