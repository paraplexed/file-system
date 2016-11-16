package main.FileSystem;

/**
 * Created by dclifford on 11/15/16.
 */
public class Drive extends Type {
    Drive(String name) {
        super(FileType.DRIVE, name);
    }

    @Override
    public Type get(String path) throws FileSystemException {
        Type type = null;
        for(String p : path.split("\\\\")) {
            if (type == null)
                type = inside.get(p);
            else
                type = type.get(p);
        }
        if (type == null)
            type = this;
        return type;
    }

    boolean pathExists(String path) throws FileSystemException {
        try {
            Type type = get(path);
            if (type.equals(this)) // temp workaround
                return false;
            return true;
        } catch(FileSystemException e) {
            if (e.getMessage().contains("Path not found"))
                return false;
            throw new RuntimeException(e);
        }
    }
}
