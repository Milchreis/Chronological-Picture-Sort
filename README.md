# Chronological-Picture-Sort
You collect a bunch of pictures of the same event from different cameras and all pictures are not in the correct order? 
This tiny library provides a chronological reorder of a saved image files. It uses the EXIF-data (creation tag) for reorder.

## Usage
```java
// Simple usage
CPS.sort(new File("/path/to/images")); 

// Advanced mode
CPS.sort(List<File> directories, boolean inlineRename, boolean prefixRename, CPSProgress listener)
```

## Build
`mvn clean compile assembly:single`
