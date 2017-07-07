package de.chris.usbupdater.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.chris.usbupdater.listener.CopyListener;

public class CopyUtil {
	
	private CopyListener copyListener;
	
//	public void copyFolder(File src, File dest) {
//	    // checks
//	    if(src==null || dest==null)
//	        return;
//	    if(!src.isDirectory())
//	        return;
//	    if(dest.exists()){
//	        if(!dest.isDirectory()){
//	            return;
//	        }
//	    } else {
//	        dest.mkdir();
//	    }
//
//	    if(src.listFiles()==null || src.listFiles().length==0)
//	        return;
//
//	    for(File fileSrc: src.listFiles()){
//	        File fileDest = new File(dest, fileSrc.getName());
////	        boolean test = this.hasFileChanged(fileSrc, fileDest);
////	        
////	        if (test) {
//	        	
//	        	if(fileSrc.isDirectory()){
//		            copyFolder(fileSrc, fileDest);
//		        }else{
//	            	this.copyListener.updateFileInfo(fileSrc.getAbsolutePath());
//
//		            try {
//		            	Files.copy(fileSrc.toPath(), fileDest.toPath(), StandardCopyOption.REPLACE_EXISTING);
//		            	
//		            } catch (IOException e) {
//		                e.printStackTrace();
//		            }
//		        }
////	        }
//	        
//	    }
//	}
	
	public List<String> CompareOldAndNew(final File src, final File dest) {
		List<String> comparedNonExistingPaths = null;
		
		try (Stream<Path> localstream = Files.walk(src.toPath());
			Stream<Path> externalStream = Files.walk(dest.toPath())){
			
			List<String> existingPaths = localstream.map(path -> (dest.toPath().relativize(path)).toString().replaceAll("..\\\\", "\\\\")).collect(Collectors.toList());
			List<String> localPaths = externalStream.map(path -> path.toString()).collect(Collectors.toList());
			comparedNonExistingPaths = existingPaths.stream()
					.filter(listEntry -> localPaths.stream()
		                    .filter(checkedEntry -> !listEntry.contains(checkedEntry)).findFirst().orElse(null) == null)
		            .collect(Collectors.toList());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comparedNonExistingPaths;
	}
	
	public boolean removeNonExisting(final List<String> comparedNonExistingPaths) {
		List<String> filePaths = comparedNonExistingPaths.stream().filter(path -> new File(path).isFile()).collect(Collectors.toList());
		List<String> dirPaths = comparedNonExistingPaths.stream().filter(path -> new File(path).isDirectory()).collect(Collectors.toList());
		
		//delete files
		filePaths.forEach(path -> {
			try {
				File file = new File(path);
				this.copyListener.updateFileInfo(file.getAbsolutePath());
				Files.delete(file.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		dirPaths.forEach(path -> {
			try {
				File file = new File(path);
				this.copyListener.updateFileInfo(file.getAbsolutePath());
				Files.delete(file.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return true;
	}
	
	private boolean hasFileChanged(final File src, final File dest) {
		
		if (dest.isDirectory()) return false;
		if (!dest.exists()) return true;
		
		InputStream is1 = null;
		InputStream is2 = null;
		MessageDigest md1;
		MessageDigest md2;
		try {

			is1 = new FileInputStream(src);
			is2 = new FileInputStream(dest);
			md1 = MessageDigest.getInstance("MD5");
			md2 = MessageDigest.getInstance("MD5");
			is1 = new DigestInputStream(is1, md1);
			is2 = new DigestInputStream(is2, md2);
			byte[] digest1 = md1.digest();
			byte[] digest2 = md2.digest();
			
			if (digest1.equals(digest2))
				return true;

		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is1.close();
				is2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public  void copyFolder(File src, File dest) throws IOException{
    	
        try (Stream<Path> stream = Files.walk(src.toPath())) {
            stream.forEach(sourcePath -> {

            	String correctedPath = (dest.toPath().resolve(dest.toPath().relativize(sourcePath))).toString();
            	Path target = new File(correctedPath.replaceAll("\\\\..\\\\", "\\\\")).toPath();
            	
                try {
                	
                	if (!new File(sourcePath.toString()).isDirectory())
                		this.copyListener.updateFileInfo(sourcePath.toString());
                	
                	boolean isDirectory = new File(sourcePath.toString()).isDirectory();
                	boolean hasChanged = this.hasFileChanged(new File(sourcePath.toString()) , new File(target.toString()));
                	boolean isFile = new File(sourcePath.toString()).isFile();
                	boolean exists = new File(target.toString()).exists();
                	                	
                	if((isFile && exists && hasChanged) || (isFile && !exists) || (isDirectory && !exists)) {
            			Files.copy(sourcePath, target, StandardCopyOption.REPLACE_EXISTING);	
                	}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

    }

	public int getFilesCount(final File sourceLocation) {
		int count = 0;
		
		for (File file : sourceLocation.listFiles()) {
			
			if (file.isFile()) {
				count++;
			}
			if (file.isDirectory()) {
				count += getFilesCount(file);
			}
		}
		return count;
	}
	
	public void setCopyListener(final CopyListener copyListener) {
		this.copyListener = copyListener;
	}
}
