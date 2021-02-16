package com.github.veresdavid.reactor.basics.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.test.StepVerifier;

/**
 * This class provides helper methods that can be used in tests.
 */
public class TestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestUtil.class);

	private static final String SEPARATOR_LINE = "--------------------------------";

	/**
	 * Helper method to log a line of dashes as a separator.
	 * Mostly used for separating publisher logs coming from manual tries and {@link StepVerifier} steps in
	 * unit tests.
	 */
	public static void logSeparatorLine() {
		LOGGER.info("{}", SEPARATOR_LINE);
	}

}
