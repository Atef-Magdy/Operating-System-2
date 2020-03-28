package project;

public class Disk {

    private Directory directory;
    int numberOfBlocks;
    int emptySpace;
    int allocatedSpace;
    Block [] blocks;
    int mode = 0; //1 for contiguous Allocation, 2 for indexed Allocation

    public Disk(int numberOfBlocks, int mode) {
        directory= new Directory("root");
        this.numberOfBlocks = numberOfBlocks;
        this.emptySpace = numberOfBlocks;
        this.allocatedSpace = 0;
        this.mode = mode;
        blocks = new Block[numberOfBlocks];
        for(int i = 0 ; i < numberOfBlocks ;++i){
            blocks[i] = new Block();
            blocks[i].allocated = false;
        }
    }

    public void runCommand(String command){
        Parser parser = new Parser();
        Boolean isValid = parser.parse(command);
        if (!isValid){
            System.out.println("ERROR :(");
        }
        else {
            String []dirs = parser.path.split(" ");
            if (parser.cmd.matches("CreateFile")){
                if(!dirs[0].equals("root"))
                {
                    System.out.println("Wrong path!");
                    return;
                }
                if(allocateContiguous(parser.size).length != 0) {
                    createFile(dirs, parser.folderName, parser.fileName, parser.size, 1, directory);
                }
                else{
                    System.out.println("No enough space :(");
                }
            }
            else if (parser.cmd.matches("CreateFolder")){
                if(!dirs[0].equals("root"))
                {
                    System.out.println("Wrong path!");
                    return;
                }
                createFolder(dirs, parser.folderName , 1 , directory);
            }
            else if (parser.cmd.matches("DeleteFile")){
                if(!dirs[0].equals("root"))
                {
                    System.out.println("Wrong path!");
                    return;
                }
                deleteFile(parser.path, parser.folderName, parser.fileName);
            }
            else if (parser.cmd.matches("DeleteFolder")){
                if(!dirs[0].equals("root"))
                {
                    System.out.println("Wrong path!");
                    return;
                }
                deleteFolder(dirs, parser.folderName , 1 , directory);
            }
            else if (parser.cmd.matches("DisplayDiskStatus")){
                displayDiskStatus();
            }
            else if (parser.cmd.matches("DisplayDiskStructure")){
                System.out.println("root");
                displayDiskStructure(directory , 4 , 1);
            }
        }
    }

    public void createFile(String[] path, String folderName, String fileName, int size , int index , Directory current) {
        if(index < path.length)
        {
            if(checkName(current ,path[index]))
            {
                createFile(path, folderName, fileName, size , index+1 , getDirectory(current , path[index]));
            }
            else
            {
                current.subDirectories.add(new Directory(path[index]));
                createFile(path , folderName , fileName , size , index+1 , getDirectory(current , path[index]));
            }
        }
        else if (index == path.length)
        {
            String completePath = "";
            for(int i=0 ; i<path.length ; i++) completePath += path[i] + " ";

            File newFile =new File(fileName, completePath , mode == 1 ? allocateContiguous(size) : allocateIndexed(size));
            current.files.add(newFile);
        }
    }

    public boolean checkName(Directory dir, String name)
    {
        for(int i=0 ; i<dir.subDirectories.size() ; i++)
        {
            if(dir.subDirectories.get(i).name == name) return true;
        }
        return false;
    }
    public Directory getDirectory(Directory dir, String name)
    {
        for(int i=0 ; i<dir.subDirectories.size() ; i++)
        {
            if(dir.subDirectories.get(i).name == name) return dir.subDirectories.get(i);
        }
        return null;
    }
    public void createFolder(String[] path, String folderName , int index , Directory current) {
        if(index < path.length)
        {
            if(checkName(current ,path[index]))
            {
                createFolder(path, folderName,index+1 , getDirectory(current , path[index]));
            }
            else
            {
                current.subDirectories.add(new Directory(path[index]));
                createFolder(path , folderName ,index+1 , getDirectory(current , path[index]));
            }
        }
        else if (index == path.length)
        {
            if (checkName(current ,path[index])){
                System.out.println("Folder already exist");
                return;
            }
            current.subDirectories.add(new Directory(folderName));
        }
    }

    public void deleteFile(String path, String folderName, String fileName) {
        //TODO
    }

    public void deleteFolder(String[] path, String folderName , int index , Directory current) {
        if(index < path.length)
        {
            if(checkName(current ,path[index]))
            {
                createFolder(path, folderName,index+1 , getDirectory(current , path[index]));
            }
            else
            {
                System.out.println("Wrong path :(");
                return;
            }
        }
        else if (index == path.length)
        {
            //TODO
            if(checkName(current , folderName))
            current.subDirectories.remove(getDirectory(current , folderName));
        }
    }

    public void displayDiskStatus(){
        System.out.println("1-Empty space : " + emptySpace);
        System.out.println("2-Allocated space : " + allocatedSpace);
        System.out.println("3-Empty Blocks in the Disk : ");
        for (int i = 0 ; i < numberOfBlocks ; ++i){
            if (!blocks[i].allocated){
                System.out.print(i + " ");
            }
            if (i % 29 == 0){
                System.out.println("");
            }
        }
        System.out.println("\n4-Allocated  Blocks in the Disk : ");
        for (int i = 0 ; i < numberOfBlocks ; ++i){
            if (blocks[i].allocated){
                System.out.print(i + " ");
            }
            if (i % 29 == 0){
                System.out.println("");
            }
        }
    }

    public void displayDiskStructure(Directory current , int spaces , int level){

        if(current.subDirectories.size() == 0 && current.files.size() == 0) return;

        else if(current.subDirectories.size() ==0)
        {
            for (int i=0 ; i<current.files.size() ; i++)
            {
                for (int j=0 ; j<spaces*level ; j++) System.out.print(" ");
                System.out.println(current.files.get(i).name);
            }
            return;
        }
        else
        {
            for (int i=0 ; i<current.files.size() ; i++)
            {
                for (int j=0 ; j<spaces*level ; j++) System.out.print(" ");
                System.out.println(current.files.get(i).name);
            }
            for (int i=0 ; i<current.subDirectories.size() ; i++)
            {
                for (int j=0 ; j<spaces*level ; j++) System.out.print(" ");
                System.out.println(current.subDirectories.get(i).name);
                displayDiskStructure(current.subDirectories.get(i) , spaces ,level+1);
            }
        }
    }

    // Should provide => Empty space , Allocated space , Empty blocks , Allocated blocks
    public Block[] allocateContiguous(int size)
    {
        int count = 0;
        int start = -1;
        int currentStart = 0;
        for(int i=0; i<numberOfBlocks ;++i){
            if(!blocks[i].allocated){
                count++;
            }
            else{
                count = 0;
                currentStart = i +1;
            }
            if (size  == count){
                start = currentStart;
                break;
            }
        }
        if(start != -1){
            Block [] arr = new Block[size];
            for(int i=start; i<size ;++i){
                blocks [i].allocated = true;
                arr[i] = blocks [i];
            }
            return arr;
        }
        else{
            Block [] arr = new Block[0];
            return arr;
        }
    }

    public Block[] allocateIndexed(int size)
    {
        //TODO
        Block [] arr = new Block[0];
        return arr;
    }

    public void releaseMemory(String name)
    {
        //TODO
    }
}
