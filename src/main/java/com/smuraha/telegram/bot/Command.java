package com.smuraha.telegram.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Command {
    RANDOM("/random"),
    HELP("/help"),
    LIST_MUSIC("/list"),
    DOWNLOAD("/download"),

    SEARCH("search"),
    AUTO_SAVE("save");

    private String name;

    public static Command getCommandByName(String name) throws Exception {
       return Arrays.stream(Command.values())
               .filter(command -> command.getName().equals(name))
               .findAny().orElseThrow(Exception::new);
    }
}
