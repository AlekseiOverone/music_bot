package com.smuraha.telegram.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Result {
    private String file_id;
    private String file_unique_id;
    private int file_size;
    private String file_path;

    public Result(String file_id, String file_unique_id, int file_size, String file_path) {
        this.file_id = file_id;
        this.file_unique_id = file_unique_id;
        this.file_size = file_size;
        this.file_path = file_path;
    }
}
