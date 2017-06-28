package de.milchreis;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.milchreis.cps.CPS;

public class Main {

	public static void main(String[] args) throws Exception {
		
		if(args.length == 0) {
			new SimpleGui();
			
		} else if(args.length == 1 && (!new File(args[0]).exists() || args[0].equals("--help"))) {	
			
			System.out.println("usage: chronologicalpicturesort.jar /path/to/images/ [/path/to/another/director ...]");
			System.out.println("");
			System.out.println("  The Chronological Picture Sort tool reads the exif-data of your image files and ");
			System.out.println("  renames the files with leading numbers to sort them by the capture time. ");
			System.out.println("  Its helpful if you have a bunch of images from different cameras of the same event.");
			
		} else {

			List<File> directories = Arrays.asList(args).stream()
				.map(a -> new File(a))
				.filter(f -> f.exists())
				.collect(Collectors.toList());
			
			CPS.sort(directories);
		}
	}

}
