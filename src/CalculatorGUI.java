import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.text.DecimalFormat;

public class CalculatorGUI {
    // 按钮
    CreateButton add, sub, and, or, xor, not, comp, clear, one, zero, ans, equals;
    // 显示框
    JTextArea displayBox;
    private String button = "";
    private String input = "";
    private boolean newEntry = false;
    private double result;
    private int AF, CF, OF, PF, SF, ZF; // 状态标志位
    DecimalFormat formater = new DecimalFormat("0.##########");
    private int cnt = 8;  // 字长位数

    public static void main(String[] args) {
        CalculatorGUI gui = new CalculatorGUI();
        gui.go();
    }

    public void go() {
        // 新建一个窗口，参数为title
        JFrame frame = new JFrame("Calculator");
        JPanel choicePane = new JPanel();
        JPanel bottomPane = new JPanel();

        // 3行4列
        bottomPane.setLayout(new GridLayout(3, 4));
        bottomPane.setPreferredSize(new Dimension(400, 150));

        Integer[] choices = new Integer[] { 8, 16, 32 };
        final JComboBox<Integer> comboBox = new JComboBox<Integer>(choices);
        comboBox.setSelectedIndex(0); // 默认选择8位
        JLabel label = new JLabel("字长选择");
        choicePane.add(label);
        choicePane.add(comboBox);

        // 添加选中状态改变的监听器
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cnt = (int)comboBox.getSelectedItem();
                }
            }
        });

        // 显示框
        displayBox = new JTextArea();
        displayBox.setPreferredSize(new Dimension(400, 50));

        add = new CreateButton("+", new OperationButton(), bottomPane);
        sub = new CreateButton("-", new OperationButton(), bottomPane);
        and = new CreateButton("&", new OperationButton(), bottomPane);
        or = new CreateButton("|", new OperationButton(), bottomPane);
        xor = new CreateButton("^", new OperationButton(), bottomPane);
        not = new CreateButton("~", new EqualsButton(), bottomPane);  // ~表示求反码
        comp = new CreateButton("#", new EqualsButton(), bottomPane); // # 表示求补码
        clear = new CreateButton("clear", new OperationButton(), bottomPane);
        one = new CreateButton("1", new NumberButton(), bottomPane);
        zero = new CreateButton("0", new NumberButton(), bottomPane);
        ans = new CreateButton("ans", new OperationButton(), bottomPane);
        equals = new CreateButton(" = ", new EqualsButton(), bottomPane);

        frame.getContentPane().add(BorderLayout.NORTH, displayBox);
        frame.getContentPane().add(BorderLayout.CENTER, choicePane);
        frame.getContentPane().add(BorderLayout.SOUTH, bottomPane);

        frame.setSize(new Dimension(400, 286));
        frame.getContentPane().setPreferredSize(new Dimension(400, 250));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 实现了ActionListener这个接口的数字键按钮
    class NumberButton implements ActionListener {
        // 实现该接口必须实现的函数
        public void actionPerformed(ActionEvent event) {

            if (newEntry) { // 如果上一个表达式已经计算完成了，newEntry已经被标记为true
                displayBox.setText(""); // 显示框清空
                newEntry = false; // 标记置为false
                input = ""; // input存放的是当前已经输入的表达式的一部分
            }

            button = event.getActionCommand();
            displayBox.setText(displayBox.getText() + button); // 输入框也需要更新
            input = input + button; // 将当前按钮表示的东西加到表达式上
        }
    }

    // 实现了ActionListener这个接口的运算符键按钮
    class OperationButton implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            button = event.getActionCommand();

            if (newEntry) {
                displayBox.setText("");
                newEntry = false;
                input = "";
            }

            if (button == "ans") {
                // 这里需要修改把result写成二进制形式
                displayBox.setText(displayBox.getText() + formater.format(result));
                input = input + Double.toString(result);
            } else if (button == "clear") {
                displayBox.setText("");
                input = "";
            } else {
                displayBox.setText(displayBox.getText() + button);
                input = input + button;
            }
        }
    }

    // 实现了ActionListener这个接口的=键按钮
    class EqualsButton implements ActionListener {
        Calculator evaluate = new Calculator();

        public void actionPerformed(ActionEvent event) {
            newEntry = true; // 标记表达式已经计算完成
            // System.out.println(cnt);
            button = event.getActionCommand();  // button可能是=，~,#
            if (evaluate.checkInput(input, button, cnt)) { // 表达式合法
                result = evaluate.readInput(input, button, cnt); // result为答案
                // 第二行显示框显示标志位
                // AF, CF, OF, PF, SF, ZF;
                String flags = "A: " + AF + "    C: " + CF + "    O: " + OF + "    P: " + PF + "    S: " + SF
                        + "    Z: " + ZF;
                displayBox.setText(displayBox.getText() + button + formater.format(result) + '\n' + flags);
                // 显示框显示：完整表达式=运算结果
            } else
                displayBox.setText(evaluate.getException()); // 显示框显示错误信息
        }
    }
}
