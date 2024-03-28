package com.cars4u.sample.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MaskParam {

	 public String mask(String target, String param) {
	        String uuid = UUID.randomUUID().toString();
	        Map<String, String> map = new HashMap<>();
	        if (target == null) {
	            target = "none";
	        }
	        map.put(target, param);
	        return uuid;
	    }
}
