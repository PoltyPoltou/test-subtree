package org.poltou.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus
public class BadIdException extends ResponseStatusException {

    public BadIdException(String reason) {
        super(HttpStatus.BAD_REQUEST, "Invalid Id. " + reason);
    }
    public BadIdException() {
        super(HttpStatus.BAD_REQUEST, "Invalid Id.");
    }

}
