import java.util.List;

public class Node {
    public String name;
    public String midName;
    public String bestName;
    public String worstName;
    public List<List<Integer>> gameState;

    public int upCount = 0;
    public int rightCount = 0;
    public int leftCount = 0;

    public Node(String name, List<List<Integer>> gameState) {
        this.name = name;
        this.gameState = gameState;
    }

    public void setCounters(int upCount, int rightCount, int leftCount) {
        this.upCount = upCount;
        this.rightCount = rightCount;
        this.leftCount = leftCount;
    }
}
