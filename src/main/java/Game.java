import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {

    private static final int[] LEFT = new int[]{0, 1};
    private static final int[] RIGHT = new int[]{1, 2};
    private static final int[] TOP = new int[]{0, 2};

    public static void main(String[] args) {

        int numDisks = 3;
        List<List<Integer>> initialGameState = new ArrayList<>(
                List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );

        for(int i = 0; numDisks > i; i++) initialGameState.getFirst().addFirst(i);

        SierpinskiTriangle.numDisks = numDisks;
        SierpinskiTriangle sierpinskiTriangle = new SierpinskiTriangle(numDisks-1, initialGameState, LEFT, RIGHT, TOP, 0, 0, 0);
        Triangle triangle = sierpinskiTriangle.init();

        Node node = SierpinskiTriangle.NODE_MAP.get("000");
        while (node.bestName != null) {
            node = SierpinskiTriangle.NODE_MAP.get(node.bestName);
        }

        System.out.println(Objects.equals(node.name, "111") ? "Success" : "Failure");
        System.out.println("Fim");
    }
}
