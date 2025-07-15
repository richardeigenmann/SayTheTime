module org.richinet {
    requires java.base;
    requires info.picocli;
    requires system.lambda;
    requires org.json; // SystemLambda for testing
    exports org.richinet to info.picocli;

    opens org.richinet to info.picocli;
    exports org.richinet.saythetime.lib to info.picocli;
    opens org.richinet.saythetime.lib to info.picocli; // Allow picocli reflection
}