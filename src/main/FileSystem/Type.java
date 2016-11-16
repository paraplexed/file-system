package main.FileSystem;

import java.util.HashMap;

/**
 * Created by dclifford on 11/15/16.
 */
public class Type {
    FileType fileType;
    String name;
    HashMap<String, Type> inside = new HashMap<String, Type>();

    enum FileType {
        DRIVE,
        FOLDER,
        TEXTFILE,
        ZIPFILE
    }

    Type() {}

    Type(FileType fileType, String name) {
        setType(fileType);
        setName(name);
    }

    private void setType(FileType fileType) {
        this.fileType = fileType;
    }

    public FileType getType() {
        return fileType;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Type get(String name) throws FileSystemException {
        if (! inside.containsKey(name)) {
            throw new FileSystemException("Path not found");
        }
        return inside.get(name);
    }

    public void remove(String name) {
        inside.remove(name);
    }

    public void add(Type type) throws FileSystemException {
        if (inside.containsKey(type.getName())) {
            throw new FileSystemException("Path already exists");
        }
        if (type.fileType == FileType.DRIVE) {
            throw new FileSystemException("Illegal File System Operation");
        }
        inside.put(type.getName(), type);
    }

    public int size() {
        int size = 0;
        for (Type type : inside.values()) {
            size += type.size();
        }
        return size;
    }
}
