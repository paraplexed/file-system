# In Memory File System

This file-system consists of 4 types of entities: Drives, Folders, Text files, Zip files.

## Entities
These entities, very much like their “real” file-system counterparts, obey the following relations.
- A folder, a drive or a zip file, may contain zero to many other folders, or files (text or zip).
- A text file does not contain any other entity.
- A drive is not contained in any entity.
- Any non-drive entity must be contained in another entity.
- If entity A contains entity B then we say that A is the parent of B.

Every entity has the following properties:
- Type – The type of the entity (one of the 4 type above).
- Name - An alphanumeric string. Two entities with the same parent cannot have the same name. Similarly, two drives cannot have the same name.
- Path – The concatenation of the names of the containing entities, from the drive down to and including the entity. The names are separated by ‘\’.
- A text file has a property called Content which is a string. 
- Size – an integer defined as follows:
  - For a text file – it is the length of its content.
  - For a drive or a folder, it is the sum of all sizes of the entities it contains.
  - For a zip file, it is one half of the sum of all sizes of the entities it contains.


The system should be capable of supporting file-system like operations

## Operations

- Create – Creates a new entity.
  - Arguments: Type, Name, Path of parent.
  - Exceptions: Path not found; Path already exists; Illegal File System Operation.
- Delete – Deletes an existing entity (and all the entities it contains).
  - Arguments: Path
  - Exceptions: Path not found.
- Move – Changing the parent of an entity.
  - Arguments: Source Path, Destination Path. 
  - Exceptions: Path not found; Path already exists, Illegal File System Operation.
- WriteToFile – Changes the content of a text file.
  - Arguments: Path, Content
  - Exceptions: Path not found; Not a text file.

