package com.zy.tasksystem.utils;

import java.util.UUID;

import org.junit.Test;

public class UuidUtil {

	@Test
	public static String makeUuid(){
		return UUID.randomUUID().toString();
	}
}
