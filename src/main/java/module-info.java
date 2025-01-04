module org.richinet {
    requires java.base;
    requires info.picocli;
    requires java.sql;
    requires system.lambda; // SystemLambda for testing
    exports org.richinet to info.picocli;

    opens org.richinet to info.picocli, org.junit.platform.commons; // Allow JUnit and picocli reflection
}