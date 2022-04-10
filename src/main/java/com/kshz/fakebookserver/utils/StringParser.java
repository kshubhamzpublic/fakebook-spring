package com.kshz.fakebookserver.utils;

public class StringParser {
	/**
	 * Extract field name from LocalizedMessage of Root Cause of
	 * {@code DuplicateKeyException}
	 * 
	 * @param error => LocalizedMessage of Root Cause
	 * @return {@code String[]} of 2 elements. 1st element=field, 2nd
	 *         element=message
	 */
	public static String[] parseDuplicateKeyErrorRootCause(String error) {
		// splitting by "Error{" => 2nd element will be returned json object
		// and deleting terminating "}."
		String errorJson = error.split("Error\\{")[1].replace("\\}\\.", "");

		// getting message property and deleting collection name
		String mongoErrorMessage = errorJson.split(",")[1]
				.replace("message=", "")
				.replace("'", "")
				.split("dup key")[1]
				.trim();

		// extracting field
		String field = mongoErrorMessage.split("\\{")[1].split(":")[0].trim();

		// return array => 1st element=field, 2nd element=message
		return new String[] { field, "Duplicate" + mongoErrorMessage };
	}

	/**
	 * Extract {@code HttpMessageNotReadableException}
	 * MostSpecificCause(LocalizedMessage) into readable format
	 * 
	 * @param error => LocalizedMessage of MostSpecificCause
	 * @return Parsed Message
	 */
	public static String parseHttpMessageNotReadableMostSpecificCause(String error) {
		String[] cause = error.split("\\[");
		String position = cause[1].split(";")[1].replace("]", "").trim();
		return cause[0] + " " + position;
	}
}
