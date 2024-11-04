import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SierpinskiTriangle {
    public static int numDisks = 0;

    public static Map<String, Node> NODE_MAP = new HashMap<>();

    private final int iterationNumber;
    private final List<List<Integer>> gameState;
    private final int[] left;
    private final int[] right;
    private final int[] bottom;
    private final Triangle triangle = new Triangle();
    private final int countUp;
    private final int countRight;
    private final int countLeft;

    public SierpinskiTriangle(int iterationNumber, List<List<Integer>> gameState, int[] left, int[] right, int[] bottom, int countUp, int countRight, int countLeft) {
        this.iterationNumber = iterationNumber;
        this.gameState = gameState;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.countUp = countUp;
        this.countRight = countRight;
        this.countLeft = countLeft;
    }

    public Triangle init() {
        if (numDisks < 1) return new Triangle();

        if (this.iterationNumber > 0) {
            buildTriangles();
        } else {
            buildTowers();
        }

        if (this.iterationNumber == 0) {
            setupInitialTriangle();
        }

        if (this.triangle.left != null) {
            updateTriangleNodes();
        }

        return this.triangle;
    }

    // Submétodo para construir os triângulos
    private void buildTriangles() {
        this.buildLeftTriangle();
        this.buildTopTriangle();
        this.buildRightTriangle();
    }

    // Submétodo para construir as torres
    private void buildTowers() {
        this.buildLeftTower();
        this.buildTopTower();
        this.buildRightTower();
    }

    // Submétodo para configurar o triângulo inicial
    private void setupInitialTriangle() {
        Node leftNode = this.triangle.leftNode;
        Node topNode = this.triangle.topNode;
        Node rightNode = this.triangle.rightNode;

        if (shouldGoRight(leftNode)) {
            leftNode.bestName = rightNode.name;
            leftNode.midName = topNode.name;
        } else {
            leftNode.bestName = topNode.name;
            leftNode.midName = rightNode.name;
        }

        topNode.worstName = leftNode.name;
        rightNode.worstName = leftNode.name;

        NODE_MAP.put(leftNode.name, leftNode);
        NODE_MAP.put(topNode.name, topNode);
        NODE_MAP.put(rightNode.name, rightNode);
    }

    // Submétodo para atualizar os nós do triângulo
    private void updateTriangleNodes() {
        // Atualizando referências de nós
        this.triangle.leftNode = this.triangle.left.leftNode;
        this.triangle.topNode = this.triangle.top.topNode;
        this.triangle.rightNode = this.triangle.right.rightNode;

        // Atualizando o bestName e o midName conforme a lógica de posicionamento
        setBestAndMidName(this.triangle.left.rightNode, this.triangle.left.topNode, this.triangle.right.leftNode);
        setBestAndMidName(this.triangle.top.rightNode, this.triangle.top.topNode, this.triangle.right.topNode);
        setBestAndMidName(this.triangle.left.topNode, this.triangle.top.leftNode, this.triangle.left.rightNode);
        setBestAndMidName(this.triangle.right.topNode, this.triangle.top.rightNode, this.triangle.right.rightNode);

        // Atualizando o worstName quando necessário
        if (this.triangle.top.leftNode.worstName == null) {
            this.triangle.top.leftNode.worstName = this.triangle.left.topNode.name;
        }

        if (this.triangle.right.leftNode.worstName == null) {
            this.triangle.right.leftNode.worstName = this.triangle.left.rightNode.name;
        }
    }

    // Submétodo para definir o bestName e o midName de acordo com a lógica de posição
    private void setBestAndMidName(Node node, Node primaryOption, Node secondaryOption) {
        if (node.bestName == null) {
            if (shouldGoRight(node)) {
                node.bestName = secondaryOption.name;
                node.midName = primaryOption.name;
            } else {
                node.bestName = primaryOption.name;
                node.midName = secondaryOption.name;
            }
        }
    }

    private boolean shouldGoRight(Node node) {
        if (node.name.startsWith("2")) {
            return true;
        }

        if (node.name.startsWith("1")) {
            return false;
        }

        return node.rightCount > node.upCount;
    }

    /** Creates a smaller triangle case
     */
    public void buildLeftTriangle() {

        List<List<Integer>> newGameState = newInstance(carryDisks(this.gameState, this.iterationNumber, this.left[0]));
        triangle.left = new SierpinskiTriangle(this.iterationNumber-1, newGameState, this.bottom, flip(right), this.left, this.countUp, this.countRight, this.countLeft + 1).init();
    }

    /** Creates a smaller triangle case
     */
    public void buildRightTriangle() {
        List<List<Integer>> newGameState = newInstance(carryDisks(this.gameState, this.iterationNumber, this.right[1]));
        triangle.right = new SierpinskiTriangle(this.iterationNumber-1, newGameState, flip(this.left), this.bottom, this.right, this.countUp, this.countRight + 1, this.countLeft).init();
    }

    /** Creates a smaller triangle case
     */
    public void buildTopTriangle() {
        List<List<Integer>> newGameState = newInstance(carryDisks(this.gameState, this.iterationNumber, this.left[1]));
        triangle.top = new SierpinskiTriangle(this.iterationNumber-1, newGameState, flip(this.right), flip(this.left), flip(this.bottom), this.countUp + 1, this.countRight, this.countLeft).init();
    }

    /** Determines create the visual representation of this current state.
     */
    public void buildLeftTower() {
        List<List<Integer>> newGameState = newInstance(carryDisks(this.gameState, this.iterationNumber, this.left[0]));
        String name = buildNodeName(newGameState);
        triangle.leftNode = new Node(name, newGameState);
        triangle.leftNode.setCounters(this.countUp, this.countRight, this.countLeft + 1);
    }

    /** Determines create the visual representation of this current state.
     */
    public void buildRightTower() {
        List<List<Integer>> newGameState = newInstance(carryDisks(this.gameState, this.iterationNumber, this.right[1]));
        String name = buildNodeName(newGameState);
        triangle.rightNode = new Node(name, newGameState);
        triangle.rightNode.setCounters(this.countUp, this.countRight + 1, this.countLeft);
    }

    public void buildTopTower() {
        List<List<Integer>> newGameState = newInstance(carryDisks(this.gameState, this.iterationNumber, this.left[1]));
        String name = buildNodeName(newGameState);
        triangle.topNode = new Node(name, newGameState);
        triangle.topNode.setCounters(this.countUp + 1, this.countRight, this.countLeft);
    }

    public List<List<Integer>> carryDisks(List<List<Integer>> a, int base, int to) {
        int from = this.getDiskLocation(a,base);
        if(from == to) return a;
        for(int i = 0; a.get(from).size() > i; i++) if(a.get(from).get(i) <= base) a.get(to).add(a.get(from).get(i));
        for(int i = a.get(from).size()-1; i >= 0; i--) if(a.get(from).get(i) <= base) a.get(from).remove(i);
        return a;
    }

    public int getDiskLocation(List<List<Integer>> a, int base) {
        int location = -1;
        for(int i = 0; 3 > i; i++)
            for(int j = 0; a.get(i).size() > j; j++)
                if(a.get(i).get(j) == base) { location = i; break; }

        return location;
    }

    public int[] flip(int a[]) {
        return new int[]{a[1],a[0]};
    }

    private String buildNodeName(List<List<Integer>> gameState) {
        StringBuilder name = new StringBuilder();
        for(int i = numDisks-1; 0 <= i; i--) {
            for(int j = 0; 3 > j; j++) {
                if (gameState.get(j).contains(i)) {
                    name.append(j);
                    break;
                }
            }
        }

        return name.toString();
    }

    private List<List<Integer>> newInstance(List<List<Integer>> gameState) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> innerList : gameState) {
            copy.add(new ArrayList<>(innerList)); // Cria uma nova lista para cada sublista
        }
        return copy;
    }
}
