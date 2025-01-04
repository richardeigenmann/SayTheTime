module org.richinet {
    requires java.base;
    requires info.picocli;
    requires java.sql;
    exports org.richinet to info.picocli;
    opens org.richinet to info.picocli;
}