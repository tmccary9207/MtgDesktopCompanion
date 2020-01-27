package org.magic.services;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.magic.api.beans.GedEntry;
import org.magic.api.interfaces.MTGGedStorage;

public class GedService {

	private static GedService inst;
	protected Logger logger = MTGLogger.getLogger(this.getClass());
	private FileSystem fileSystem ;
	
	private GedService() {
		
		try {
			fileSystem = MTGControler.getInstance().getEnabled(MTGGedStorage.class).getFilesSystem();
			logger.info("Loading FS :" + fileSystem +" (o="+fileSystem.isOpen()+", ro="+fileSystem.isReadOnly()+"), Provider="+fileSystem.provider());
		} catch (IOException e) {
			logger.error("Error init FS ",e);
		}
		
		
	}
	
	public static GedService inst()
	{
		if(inst ==null)
			inst = new GedService();
		
		return inst;
	}
	
	public FileSystem getFileSystem() {
		return fileSystem;
	}
	
	public void store(GedEntry<?> entry) throws IOException
	{
		Path p = getPath(entry);
		logger.info("store :"+ p.toAbsolutePath());
		
		
		if(p.getParent()!=null && !Files.exists(p.getParent()))
			Files.createDirectories(p.getParent());
			
		Files.write(p, SerializationUtils.serialize(entry),StandardOpenOption.CREATE);
		
	}
	
	public List<Path> list(String dir)
	{
		try (Stream<Path> s = Files.list(fileSystem.getPath(dir)))
		{
			return s.collect(Collectors.toList());
		} catch (IOException e) {
			return new ArrayList<>();
		}
		
	}
	
	public List<Path> listRoot()
	{
		return list("/").stream().collect(Collectors.toList());
	}
	
	private Path getPath(GedEntry<?> entry)
	{
		if(entry.getClasse()==null)
			return fileSystem.getPath(entry.getId());
		else
			return fileSystem.getPath(entry.getClasse().getSimpleName(),entry.getId());
	}

	public boolean delete(GedEntry<?> entry) {
		logger.info("removing " + entry);
		
		try {
			Files.delete(getPath(entry));
			return true;
		} catch (IOException e) {
			logger.error(e);
			return false;
		}
		
		
	}
	
	
}