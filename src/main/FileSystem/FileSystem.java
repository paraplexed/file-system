package main.FileSystem;

import java.util.HashMap;

/**
 * Created by dclifford on 11/15/16.
 */
public class FileSystem {
    HashMap<String, Drive> drives = new HashMap<String, Drive>();

    public Drive createDrive(String path) throws FileSystemException {
        if (drives.containsKey(path))
            throw new FileSystemException("Path already exists");

        // Drives cannot be a child of anything else
        if (path.contains("\\"))
            throw new FileSystemException("Illegal File System Operation");
        Drive drive = new Drive(path);
        drives.put(path, drive);
        return drive;
    }

    public Folder createFolder(String path) throws FileSystemException {
        Drive drive = getDrive(path);
        Folder folder = new Folder(getName(path));

        Type type = drive.get(getPathWithoutNameOrDrive(path));
        type.add(folder);
        return folder;
    }

    public TextFile createTextFile(String path, String content) throws FileSystemException {
        Drive drive = getDrive(path);
        TextFile textFile = new TextFile(getName(path), content);

        Type type = drive.get(getPathWithoutNameOrDrive(path));
        type.add(textFile);
        return textFile;
    }

    public ZipFile createZipFile(String path) throws FileSystemException {
        Drive drive = getDrive(path);
        ZipFile zipFile = new ZipFile(getName(path));

        Type type = drive.get(getPathWithoutNameOrDrive(path));
        type.add(zipFile);
        return zipFile;
    }

    public Type get(String path) throws FileSystemException {
        Drive drive = getDrive(path);
        return drive.get(getPathWithoutDrive(path));
    }

    public Void remove(String path) throws FileSystemException {
        Drive drive = getDrive(path);
        String name = getName(path);
        String pathWithoutNameOrDrive = getPathWithoutNameOrDrive(path);
        if (pathWithoutNameOrDrive != null) {
            Type type = drive.get(getPathWithoutNameOrDrive(path));
            type.remove(name);
        }
        else {
            drives.remove(path);
        }
        return null;
    }

    public Void move(String originPath, String destPath) throws FileSystemException {
        Drive originDrive = getDrive(originPath);
        Drive destDrive = getDrive(destPath);

        Type type = originDrive.get(getPathWithoutDrive(originPath));
        if (destDrive.pathExists(getPathWithoutDrive(destPath))) {
            //remove(destPath); // if this were the same as linux
            throw new FileSystemException("Path already exists");
        }

        // Add to destination hash the object ref, and remove it from the old hash
        Type destType = destDrive.get(getPathWithoutNameOrDrive(destPath));
        type.setName(getName(destPath));
        destType.add(type);
        Type originParent = originDrive.get(getPathWithoutNameOrDrive(originPath));
        originParent.inside.remove(getName(originPath));
        return null;
    }

    public Void updateTextFile(String path, String content) throws FileSystemException {
        Drive drive = getDrive(path);
        String pathWithoutDrive = getPathWithoutDrive(path);
        Type type = drive.get(getPathWithoutDrive(path));
        if (type.equals(drive) && (pathWithoutDrive != null && ! pathWithoutDrive.isEmpty()))
            throw new FileSystemException("Path not found");
        if (type.getType() != Type.FileType.TEXTFILE)
            throw new FileSystemException("Not a text file");

        ((TextFile) type).updateContent(content);
        return null;
    }

    private String getName(String path) {
        // name is the last field in the path
        String[] p = path.split("\\\\");
        return p[p.length - 1];
    }

    private String getPathWithoutNameOrDrive(String path) {
        String[] p = path.split("\\\\");
        String innerPath = "";
        for (int i = 0; i < p.length; i++) {
            if (i != 0 && i != (p.length - 1))
                innerPath += p[i] + "\\";
        }

        if (innerPath.isEmpty())
            innerPath = p[p.length - 1];

        return innerPath;
    }

    private String getPathWithoutDrive(String path) {
        String[] p = path.split("\\\\");
        String innerPath = "";
        for (int i = 0; i < p.length; i++) {
            if (i != 0 )
                innerPath += p[i] + "\\";
        }

        return innerPath;
    }

    private Drive getDrive(String path) throws FileSystemException {
        // drive is the first field in the path
        String driveName = path.split("\\\\")[0];
        if (! drives.containsKey(driveName))
            throw new FileSystemException("Path not found");

        return drives.get(driveName);
    }
}
