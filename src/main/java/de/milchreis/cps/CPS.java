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
					return getCreationDate(f1).compareTo(getCreationDate(f2));
				} catch(Exception e) {
					return 0;
				}
			}
		});
		
		// Rename files
		int max = files.size();
		int leadingZeros = String.valueOf(max).length();
		String pattern = "%0" + leadingZeros + "d";
		int number = 1;
		
		for(File f : files) {
			String filename = f.getName();
			
			if(prefixRename) {
				filename = pattern + "_" + filename;
			} else {
				filename = pattern + "." + FilenameUtils.getExtension(f.getName());
			}
			
			File dest = new File(f.getParentFile(), String.format(filename, number));

			if(inlineRename) {
				f.renameTo(dest);
			} else {
				FileUtils.copyFile(f, dest);
			}
			
			number++;
		}
	}
	
	private static Date getCreationDate(File file) throws IOException, ImageProcessingException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		return directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getDefault());
	}
	
}
