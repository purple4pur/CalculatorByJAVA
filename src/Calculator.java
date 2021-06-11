import java.util.Collections;
import java.util.Stack;
import java.util.Scanner;

public class Calculator {
    private String token = "";
    private String exception = "";
    public String AF = "0", CF = "0", OF = "0", PF = "0", SF = "0", ZF = "0";

    public String getException() {
        return exception;
    }

    // in为输入的表达式，button可能是=,~,#，cnt表示字长位数
    public boolean checkInput(String in, String button, int cnt) {
        int opCount = 0;
        int numCount = 0;
        boolean valid = true;
        String[] nums = {"", ""};

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
    public String readInput(String exp, String button, int cnt) {
        int ans = 0;
        exp = exp.replaceAll(" ", "");
        System.out.println(exp);
        char[] str = exp.toCharArray();
        if (button.equals("~") || button.equals("#")) {
            int x = Integer.parseInt(exp, 2);
            if (button.equals("~")) ans = (~x) & ((1 << cnt) - 1);
            else {
                if ((x >> (cnt - 1) & 1) == 1) {
                    ans = ((~x) + 1) & ((1 << cnt) - 1);
                } else ans = x;
            }
        } else {
            int pos = 0;
            for (int i = 0; i < exp.length(); i++) {
                if (isOperator(Character.toString(exp.charAt(i)))) {
                    pos = i;
                    break;
                }
            }
            int a = Integer.parseInt(exp.substring(0, pos), 2), b = Integer.parseInt(exp.substring(pos + 1), 2);
            System.out.println(a + ", " + b);
            char op = exp.charAt(pos);
            switch (op) {
                case '+': {
                    ans = a + b;
                }
                break;
                case '-': {
                    ans = (1 << cnt) + a - b;
                }
                break;
                case '&': {
                    ans = a & b;
                }
                case '|': {
                    ans = a | b;
                }
                case '^': {
                    ans = a ^ b;
                }
                break;
            }
        }
        if (ans >= (1 << cnt)) {
            OF = "1";
            ans = ans % (1 << cnt);
        } else OF = "0";

        String ret = Integer.toString(ans, 2);

        if (ans == 0) ZF = "1";
        else ZF = "0";

        SF = ret.substring(0, 1);

        for (int i = ret.length() - 1, tmp = 0; i >= ret.length() - 8; i++) {
            if (ret.charAt(i) == '1') {
                tmp++;
                if (tmp % 2 == 0) PF = "1";
                else PF = "0";
            }
        }
        return ret;

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

    private static double evaluate(String operation, double num2, double num1) {
        if (operation.equals("+")) {
            num1 = num1 + num2;
        } else if (operation.equals("-")) {
            num1 = num1 - num2;
        } else if (operation.equals("*")) {
            num1 = num1 * num2;
        } else if (operation.equals("/")) {
            num1 = num1 / num2;
        } else if (operation.equals("^")) {
            num1 = Math.pow(num1, num2);
        }
        return num1;
    }
}

