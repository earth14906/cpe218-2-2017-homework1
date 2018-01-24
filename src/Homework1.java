
import javax.swing.*;

import sun.reflect.generics.tree.Tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Stack;

public class Homework1 extends JPanel
		implements TreeSelectionListener {
	private JEditorPane htmlPane;
	private JTree jTree;

	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";
	private static boolean useSystemLookAndFeel = false;

	public Homework1() {
		super(new GridLayout(1,0));

		//Create the nodes.
		DefaultMutableTreeNode top =
				new DefaultMutableTreeNode(jTre);
		CreateNode(top,jTre);

		//Create a tree that allows one selection at a time.
		jTree = new JTree(top);
		jTree.getSelectionModel().setSelectionMode
				(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		jTree.addTreeSelectionListener(this);

		if (playWithLineStyle) {
			System.out.println("line style = " + lineStyle);
			jTree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(jTree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));

		ImageIcon leafIcon = new ImageIcon(Homework1.class.getResource("middle.gif"));
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setClosedIcon(leafIcon);
		renderer.setOpenIcon(leafIcon);
		jTree.setCellRenderer(renderer);
		add(splitPane);
	}

    public void  CreateNode(DefaultMutableTreeNode top,Node n)
    {
        if(n.left!=null)
        {
            DefaultMutableTreeNode left = new DefaultMutableTreeNode(n.left);
            top.add(left);
            CreateNode(left,n.left);
        }
        if(n.Right!=null)
        {
            DefaultMutableTreeNode Right = new DefaultMutableTreeNode(n.Right);
            top.add(Right);
            CreateNode(Right,n.Right);
        }
    }

    public void DisplayNode(Node n)
    {
        InfixRoot(n);
        if(!IsLeaf)
        {
            Screen = Screen + "=" + Calculated(n);
        }
        htmlPane.setText(Screen);
    }


	public static boolean IsLeaf = false;
	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				jTree.getLastSelectedPathComponent();

		if (node == null) return;
		IsLeaf = node.isLeaf();
		Object nodeInfo = node.getUserObject();
		DisplayNode((Node)nodeInfo);
	}


    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        jFrame frame = new jFrame("Binary Tree Calculator");
        frame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new Homework1());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

	public void DisplayNode(Node n)
	{
		InfixRoot(n);
		if(!IsLeaf)
		{
			Screen=Screen + "=" + Calculated(n);
		}
		htmlPane.setText(Screen);
	}


	public static Stack<Character> NumberSet = new Stack<Character>();
	public static Node JTree;
	public static String Screen;

	public static void main(String[] args) {
		// Begin of arguments input sample
		String input ="251-*32*+";
		if (args.length > 0) {
			input = args[0];
			if (input.equalsIgnoreCase( "251-*32*+")) {
				System.out.println("(2*(5-1))+(3*2)=14");
			}
		}
		for (int i = 0; i < input.length(); i++) {
			NumberSet.push(input.charAt(i));
		}
		JTree=new Node(NumberSet.pop());
		Inorder(JTree);
		InfixRoot(JTree);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		}
		);
	}

    public static void Inorder(Node a)
    {
        if(IsOperator(a.Key))
        {
            a.Right = new Node(NumberSet.pop());
            Inorder(a.Right);
            a.left = new Node(NumberSet.pop());
            Inorder(a.left);
        }
    }

    public static Boolean IsNumber(Character s)
    {
        if("0123456789".indexOf(s)!= -1)
        {
            return true;
        }
        return false;
    }


    public static Boolean IsOperator(Character s)
	{
		if("+-*/".indexOf(s)!=-1)
		{
			return true;
		}
		return false;
	}

    public static void Infix(Node post)
    {
        if(IsNumber(post.Key))
        {
            Screen += post.Key;
            System.out.print(post.Key);
        }else if(IsOperator(post.Key))
        {
            Screen += "(";
            System.out.print("(");
            Infix(post.left);
            Screen += post.Key;
            System.out.print(post.Key);
            Infix (post.Right);
            Screen += ")";
            System.out.print(")");
        }
    }

    public static void InfixRoot(Node post)
    {
        Screen="";
        if(IsNumber(post.Key))
        {
            Screen+=post.Key;
            System.out.print(post.Key);
        }else if(IsOperator(post.Key))
        {
            Infix(post.left);
            Screen+=post.Key;
            System.out.print(post.Key);
            Infix(post.Right);
        }
        System.out.println("=" + Calculated(post));
    }

	public static int Calculated(Node x)
	{
		if(IsNumber(x.Key))
		{
			return Integer.parseInt(x.Key.toString());
		}else if(IsOperator(x.Key))
		{
			if(x.Key=='+')
			{
				return Calculated(x.left)+Calculated(x.Right);
			}else if(x.Key=='-')
			{
				return Calculated(x.left)-Calculated(x.Right);
			}else if(x.Key=='*')
			{
				return Calculated(x.left)*Calculated(x.Right);
			}else if(x.Key=='/')
			{
				return Calculated(x.left)/Calculated(x.Right);
			}
		}
		return 0;
	}

	}