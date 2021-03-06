package project;

import java.io.Serializable;
import java.util.ArrayList;

public class Directory implements Serializable {
    //private String directoryPath;
    public ArrayList<File> files;
    public String name;
    public ArrayList<Directory> subDirectories;

    public Directory() {
        files = new ArrayList<>();
        subDirectories = new ArrayList<>();
        this.name = name;
    }
    public Directory(String name)
    {
        files = new ArrayList<>();
        subDirectories = new ArrayList<>();
        this.name = name;
    }
}
