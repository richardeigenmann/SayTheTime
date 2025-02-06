module org.richinet {
    requires java.base;
    requires info.picocli;
    requires system.lambda;
    requires org.json; // SystemLambda for testing
    exports org.richinet to info.picocli;

    opens org.richinet to info.picocli, org.junit.platform.commons; // Allow JUnit and picocli reflection
}