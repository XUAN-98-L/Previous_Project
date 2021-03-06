Recent developments of various high-throughput techniques have generated unprecedented quantities of biological data. In parallel with the rapid expansion of omics, researchers have shown a dramatic  interest in predicting the organization of complex biological systems by interpretation of those high-throughput data. Thus, I developed a java-based, lightweight software, NetworkAnalysis 1.0, for researchers to analyze biological networks’ properties. It provides a user-friendly graphical interface. It allows users to add  a new connection to the existed network, and analyze network characteristics, including average degree, degree distribution and hubs in the network with great accuracy.

If you want directly use the software, please open **“Main.java"** (XUAN LIU\src\sample), which is the entry to the application.

**Folder path "XUAN LIU\src" contains all the source code**. Detailed explanation about classes can be found in the report.

|            Classification            |     Classes     |
| :----------------------------------: | :-------------: |
|             Entity class             |   Node. java    |
|                                      |   Edge. java    |
|                                      |  Network. java  |
|           Network analysis           | Operation.java  |
| Utility class (used for file output) | CsvWriter.java  |
|             GUI (javaFX)             | Controller.java |
|                                      |    GUI.fxml     |
|                                      |    Main.java    |

"sample.csv" is the output example using "PPInetwork.txt" as the input file.

"test.txt" can be used to test the result of the software output.

"javacsv2.1.zip" is a archive file, which contains the Java CSV Library. (CsvWriter class came from this library.) The Java CSV Library was downloaded from https://sourceforge.net/projects/javacsv/. The official API for Java CSV can be found from the link (http://javacsv.sourceforge.net/).

“Scene Builder” is a free and open source for design of the user interface (https://gluonhq.com/products/scene-builder/).  In this project, SceneBuilder version 8.5.0 (Windows 64-bit system) was used.

SDK 1.8 (version 1.8.0_282) was used as it includes JavaFX.

