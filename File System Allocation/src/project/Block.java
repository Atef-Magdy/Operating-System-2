package project;

import java.util.ArrayList;

public class Block {
    boolean allocated;
    public ArrayList<Integer> subBlocks;

    public Block() {
    }

    public Block(boolean allocated, ArrayList<Integer> subBlocks) {
        this.allocated = allocated;
        this.subBlocks = subBlocks;
    }

}
