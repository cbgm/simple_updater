package de.chris.usbupdater.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.filechooser.FileSystemView;

import de.chris.usbupdater.model.Settings;

public class DriveUtil {

	public static String[] detectUSBDrives() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File[] drives = File.listRoots();
		List<String> driveLetters = new ArrayList<>();

		Arrays.asList(drives).forEach(d -> {
			
			if(fsv.isDrive(d) /*&& fsv.getSystemTypeDescription(drive).equals("removeable")*/ && d.canWrite()) {
				driveLetters.add(d.getAbsolutePath());
			}
		});
		
//		for(File drive : drives) {
//
//			if(fsv.isDrive(drive) /*&& fsv.getSystemTypeDescription(drive).equals("removeable")*/ && drive.canWrite()) {
//				driveLetters.add(drive.getAbsolutePath());
//			}
//		}
		
		return driveLetters.size() > 0 ? driveLetters.toArray(new String[driveLetters.size()]) : null;
	}
	
	public static List<String> detectDrives() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File[] drives = File.listRoots();
		
		return Arrays.stream(drives).map(d -> d.getAbsolutePath()).collect(Collectors.toList());
	}
	
	public static HashMap<String, String> detectPreparedUSBDrives() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File[] drives = File.listRoots();
		HashMap<String, String> driveLetters = new HashMap<>();
		
		for(File drive : drives) {
			
			if(fsv.isDrive(drive) && new File(drive + "Users\\SA_Admin\\Desktop\\" + "key.txt").exists() && drive.canWrite()) {
				
				try {
					List<String> lines= Files.readAllLines(Paths.get(drive.getAbsolutePath() + "Users\\SA_Admin\\Desktop\\" + "key.txt"));
				
					if (lines.size() < 2) {
						driveLetters.put(lines.get(0), drive.getAbsolutePath()); 
					} else {
						String lastUpdated = lines.get(1);
						Date test = new Date();
						if (!lastUpdated.equals(test.toString()) && Settings.getInstance(false).getUpdateInfos().stream().anyMatch((i -> i.getKey().equals(lines.get(0))))) {
							driveLetters.put(lines.get(0), drive.getAbsolutePath()); 
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
				
			}
		}
		
		return driveLetters.size() > 0 ? driveLetters : null;
	}
	
	public static void setDriveLastUpdated(final String driveLetter, final String key) {
		
		try (PrintWriter pw = new PrintWriter(new FileWriter(driveLetter + "Users\\SA_Admin\\Desktop\\" + "key.txt"));) {
			pw.println(key);
			pw.println(new Date().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		
	}
	
	public static void perpareDrive(final String driveLetter, final String key) {
		
		try (PrintWriter pw = new PrintWriter(new FileWriter(driveLetter + "Users\\SA_Admin\\Desktop\\" + "key.txt"));) {
			pw.println(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
	}
}
