# Chronological-Picture-Sort
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
`java -jar bin/chronologicalpicturesort.jar /path/to/images/`


## Build
`mvn clean compile assembly:single`
