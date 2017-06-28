# Chronological Picture Sort
You collect a bunch of pictures of the same event from different cameras and all pictures are not in the correct order? 
This tiny library provides a chronological reorder of your saved image files. It uses the EXIF-data (creation tag) for reordering.

## Usage

### As library
```java
// Simple usage
CPS.sort(new File("/path/to/images")); 

// Advanced mode
CPS.sort(List<File> directories, boolean inlineRename, boolean prefixRename, CPSProgress listener)
```

### As commandline tool
`java -jar bin/ChronicalPictureSort-0.0.1.jar /path/to/images/`


### As desktop tool
Maybe with double click on .jar file
or
`java -jar bin/ChronicalPictureSort-0.0.1.jar`

![alt screenshot](https://github.com/Milchreis/Chronological-Picture-Sort/raw/master/screenshot.png "Screenshot")


## Build
`mvn clean compile assembly:single`
