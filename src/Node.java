public class Node {
    public Node left;
    public Node Right;
    Character Key;
    public Node(Character data)
    {
        Key=data;
    }

    public String toString() {
        return Key.toString();
    }
}