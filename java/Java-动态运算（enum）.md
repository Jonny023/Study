# 枚举类动态运算（+-*/%）

```java

public enum Operator {

    ADD("+") {
        @Override
        public double apply(double x1, double x2) {
            return x1 + x2;
        }
    },
    SUB("-") {
        @Override
        public double apply(double x1, double x2) {
            return x1 - x2;
        }
    },
    MULTIPLY("*") {
        @Override
        public double apply(double x1, double x2) {
            return x1 - x2;
        }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x1, double x2) {
            return x1 / x2;
        }
    },
    MOD("%") {
        @Override
        public double apply(double x1, double x2) {
            return x1 % x2;
        }
    };

    private final String text;

    Operator(String text) {
        this.text = text;
    }

    public abstract double apply(double x1, double x2);

    @Override
    public String toString() {
        return text;
    }

    public static void main(String[] args) {
        double result = Operator.ADD.apply(1, 2);
        System.out.println(result);
    }
}

```
