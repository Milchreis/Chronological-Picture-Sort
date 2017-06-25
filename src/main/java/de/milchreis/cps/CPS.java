package de.milchreis.cps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;


/**
 * The Chronological Picture Sort (CPS) reads the EXIF-data of your image files and
 * renames the files with leading numbers to sort them by the capture time.
 * 
 * Its helpful if you have a bunch of images from different cameras of the same event.
 * 
 * @author nick
 *
 */
public class CPS {
	
	public static final String[] RAW_FORMATS = {
			"3fr", "ari", "ARW", "bay", "cap", "CR2", "crw", "dcr", "dcs", "dng", "drf", 
			"eip", "erf", "fff", "iiq", "k25", "kdc", "mdc", "mef", "mos", "mrw", "NEF", 
			"nrw", "orf", "pef", "ptx", "pxn", "R3D", "raf", "raw", "rw2", "rwl", "rwz", 
			"sr2", "srf", "srw", "x3f"};
			
	/**
	 * This method sorts the found images in the given directories by the including EXIF
	 * creation data and renames the files to achieve a chronological order by file name.
	 * 
	 * @param directory		expects the directory with the image files
	 * @throws Exception
	 */
	public static void sort(File directory) throws Exception {
		List<File> directories = new ArrayList<>();
		directories.add(directory);
		sort(directories, true, true, null);
	}

	/**
	 * This method sorts the found images in the given directories by the including EXIF
	 * creation data and renames the files to achieve a chronological order by file name.
	 * 
	 * @param directories	expects a list of directories
	 * @throws Exception
	 */
	public static void sort(List<File> directories) throws Exception {
		sort(directories, true, true, null);
	}
	
	/**
	 * This method sorts the found images in the given directories by the including EXIF
	 * creation data and renames the files to achieve a chronological order by file name.
	 * 
	 * @param directories	expects a list of directories
	 * @param inlineRename	true = renames the file, false = creates copies
	 * @param prefixRename	true = sets the order-number in front of the original name, false = saves the prefix only
	 * @param listener		optional: implement to receive information for long processes
	 * @throws Exception
	 */
	public static void sort(List<File> directories, boolean inlineRename, boolean prefixRename, CPSProgress listener) throws Exception {
		
		List<File> files = new ArrayList<>();
		ImageFileFilter fileFilter = new ImageFileFilter();
		
		// Collect all files
		for(File dir : directories) {
			Iterator<File> it = FileUtils.iterateFiles(dir, null, false);
			it.forEachRemaining(f -> {
				if(fileFilter.accept(f)) {
					if(listener != null) {
						listener.onScanStep(f);
					}
					files.add(f);
				}
			});
		}
		
		// Sort all files by EXIF data
		files.sort(new Comparator<File>() {
			public int compare(File f1, File f2) {
				if(listener != null) {
					listener.onSortStep(f1, f2);
				}
				try {
					int value = getCreationDate(f1).compareTo(getCreationDate(f2));
					if(value == 0) {
						return f1.getName().compareTo(f2.getName());
					} else {
						return value;
					}
				} catch(Exception e) {
					return 0;
				}
			}
		});
		
		// Rename files
		int max = files.size();
		int number = 1;
		
		for(File f : files) {

			File dest = getNewFileName(f, prefixRename, number, max);
			File rawSrc = fetchRawFile(f);
			File rawDest = null;
			
			if(rawSrc != null && rawSrc.exists()) {
				rawDest = getNewFileName(rawSrc, prefixRename, number, max);
			}
			
			if(inlineRename) {
				f.renameTo(dest);
				
				if(rawDest != null) {
					rawSrc.renameTo(rawDest);
				}

			} else {
				FileUtils.copyFile(f, dest);

				if(rawDest != null) {
					FileUtils.copyFile(rawSrc, rawDest);
				}
			}
			
			number++;
		}
	}
	
	private static File getNewFileName(File f, boolean prefixRename, int number, int maxFiles) {
		String filename = f.getName();
		int leadingZeros = String.valueOf(maxFiles).length();
		String pattern = "%0" + leadingZeros + "d";
		
		if(prefixRename) {
			filename = pattern + "_" + filename;
		} else {
			filename = pattern + "." + FilenameUtils.getExtension(f.getName());
		}
		
		return new File(f.getParentFile(), String.format(filename, number));
	}

	private static File fetchRawFile(File f) {
		
		String filename = FilenameUtils.getBaseName(f.getName());
		
		for(String ext : RAW_FORMATS) {
			File rawFile = new File(f.getParentFile(), filename + "." + ext);
			if(rawFile.exists()) {
				return rawFile;
			}
		}
		
		return null;
	}

	private static Date getCreationDate(File file) throws IOException, ImageProcessingException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		return directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getDefault());
	}
	
}
