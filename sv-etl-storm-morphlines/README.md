Configurable ETL Storm topology with a custom Kite SDK Morphlines Bolt 
============================

Current project contains a custom Storm Bolt ([MorphlinesBolt](src/main/java/com/sourcevirtues/etl/storm/morphlines/bolt/MorphlinesBolt.java)) that can execute an arbitrary Morphline configuration file.

[Morphlines](https://github.com/kite-sdk/kite/tree/master/kite-morphlines) (part of [Kite SDK library](https://github.com/kite-sdk/kite)) is an open source framework that reduces the time and skills necessary to build and change Hadoop ETL stream processing applications (copied from its README). Morphlines supports various existing Command implementations, but you can also implement your own Command(s).

MorphlinesBolt gives the opportunity to user to perform custom ETL logic ***without*** writing/changing Storm code. You just need to write a custom [Morphline configuration file](http://kitesdk.org/docs/current/morphlines/morphlines-reference-guide.html).

MorphlinesBolt support various configuration builder methods (method's prefix ***with***).

Currently, MorphlinesBolt supports only one output Record fromm execution of Morphline script, but can be easily extended to support more output Records.

### Dummy test topologies:
- DummyJsonTerminalLogTopology: MorphlinesBolt is configured with a custom morphline configuration file and acts as terminal Bolt.
- DummyJson2StringTopology: MorphlinesBolt is configured with a custom morphline configuration file and acts as an intermediate Bolt that emit a new tuple in default stream.

This PoC is not final yet and need a few minor fixes.

Please feel free to collaborate, share, ask for help or report issues.
