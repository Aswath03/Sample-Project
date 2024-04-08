package com.cars4u.sample.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomUtils {
	
	public static boolean removeFile(String filePath) {
		Path path = Paths.get(filePath);
		try {
			if (Files.exists(path)) {
				Files.delete(path);
			}
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
