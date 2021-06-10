import java.util.Stack;
import java.util.Scanner;

public class Calculator {
    private String token = "";
    private String exception = "";

    public String getException() {
        return exception;
    }

    // in为输入的表达式，button可能是=,~,#，cnt表示字长位数
    public boolean checkInput(String in, String button, int cnt) {
        int       opCount  = 0;
        int       numCount = 0;
        boolean   valid    = true;
        String[]  nums     = {"", ""};

        Scanner reader = new Scanner(in);
        while (reader.hasNext()) {
            token = reader.next();

            if (isNumber(token)) {
                nums[numCount] = token;
                numCount++;
            } else if (isOperator(token)) {
                opCount++;
            }
        }
        reader.close();
        
        if (button.equals("~") || button.equals("#")) {
            if (numCount != 1 || opCount != 0) {
                exception = "Error: needs only one binary number";
                valid = false;
            } else if (nums[0].length() != cnt) {
                exception = "Error: do not match word length";
                valid = false;
            }

        // button.equals("=")
        } else {
            // 直接按 "=" 键
            if (numCount == 0 && opCount == 0) {
                exception = "";
                valid = false;

            // 只有一个数
            } else if (numCount == 1 && opCount == 0) {
                if (nums[0].length() != cnt) {
                    exception = "Error: do not match word length";
                    valid = false;
                }

            // 两个数 + 一个操作符
            } else if (numCount != 2 || opCount != 1) {
                exception = "Error: wrong pattern";
                valid = false;
            } else if (nums[0].length() != cnt || nums[1].length() != cnt) {
                exception = "Error: do not match word length";
                valid = false;
            }
        }

        return valid;
    }

    // exp为输入的表达式，button可能是=,~,#，cnt表示字长位数
    public double readInput(String exp, String button, int cnt) {
        Scanner input = new Scanner(exp); // 获得需要计算的表达式字符串
        Stack<Double> num = new Stack<Double>();
        Stack<String> op = new Stack<String>();

        while (input.hasNext()) {
            token = input.next();
            if (isNumber(token)) {
                num.push(Double.parseDouble(token));
            } else if (token.equals("(")) {
                op.push(token);
            } else if (token.equals(")")) {
                while (!op.peek().equals("(")) {
                    if (op.peek().equals("sqrt")) {
                        num.push(Math.sqrt(num.pop()));
                        op.pop();
                    } else
                        num.push(evaluate(op.pop(), num.pop(), num.pop()));
                }
                op.pop();
            } else {
                while (!op.empty() && hasPrecedence(token, op.peek())) {
                    if (op.peek().equals("sqrt")) {
                        num.push(Math.sqrt(num.pop()));
                        op.pop();
                    } else
                        num.push(evaluate(op.pop(), num.pop(), num.pop()));
                }
                op.push(token);
            }
        }

        while (!op.empty()) {
            if (op.peek().equals("sqrt")) {
                num.push(Math.sqrt(num.pop()));
                op.pop();
            } else
                num.push(evaluate(op.pop(), num.pop(), num.pop()));
        }
        return num.pop();
    }

    private static boolean isNumber(String exp) {
        if (exp.startsWith("-") && (exp.length() > 1))
            return true;
        else if (exp.startsWith("0"))
            return true;
        else if (exp.startsWith("1"))
            return true;
        else
            return false;
    }

    private static boolean isOperator(String exp) {
        if (exp.equals("+"))
            return true;
        else if (exp.equals("-"))
            return true;
        else if (exp.equals("&"))
            return true;
        else if (exp.equals("|"))
            return true;
        else if (exp.equals("^"))
            return true;
        else
            return false;
    }

    private static boolean hasPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")"))
            return false;
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-")))
            return false;
        if (op1.equals("^") && (op2.equals("/") || op2.equals("*")))
            return false;
        if (op1.equals("^") && (op2.equals("-") || op2.equals("+")))
            return false;
        if (op1.equals("sqrt") && (op2.equals("/") || op2.equals("*")))
            return false;
        if (op1.equals("sqrt") && (op2.equals("-") || op2.equals("+")))
            return false;
        else
            return true;
    }

     private static double evaluate (String operation, double num2, double num1) {
        if(operation.equals("+")) {
            num1 = num1 + num2;
        } else if(operation.equals("-")){
            num1 = num1 - num2;
        } else if(operation.equals("*")){
            num1 = num1 * num2;
        } else if(operation.equals("/")){
            num1 = num1 / num2;
        } else if(operation.equals("^")){
            num1 = Math.pow(num1, num2);
        } return num1;
    }
}

