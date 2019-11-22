package org.fhi360.lamis.model;

import org.springframework.http.HttpStatus;

public class ServerResponseStatus {

	
	 public final static HttpStatus OK = HttpStatus.OK;

	    public final static HttpStatus CREATED = HttpStatus.CREATED;

	    public final static HttpStatus UPDATED = HttpStatus.ACCEPTED;

	    public final static HttpStatus DELETED = HttpStatus.ACCEPTED;

	    public final static HttpStatus FAILED = HttpStatus.BAD_REQUEST;

	    public final static HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

	    public final static HttpStatus NOT_MODIFIED = HttpStatus.NOT_MODIFIED;

	    public final static HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;

	    public final static HttpStatus NO_CONTENT = HttpStatus.NO_CONTENT;
	    public final static HttpStatus INTERNAL_SERVER_ERROR=HttpStatus.INTERNAL_SERVER_ERROR;

	    private ServerResponseStatus() {
	    }
}
