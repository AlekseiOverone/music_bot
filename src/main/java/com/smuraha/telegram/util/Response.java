package com.smuraha.telegram.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response {

    private boolean ok;
    private Result result;

    public Response(boolean ok, Result result) {
        this.ok = ok;
        this.result = result;
    }
}
