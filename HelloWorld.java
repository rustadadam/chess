public class HelloWorld {
    public HelloWorld() {

    }

    public static void main(String[] var0) {
        System.out.println("Hello World");
        int[] numbers = new int[5];
        String[] names = {"Johm", "Alice", "David", "Hunter", "Darlin"};

        numbers[0] = 1;
        numbers[1] = 2;
        numbers[2] = 3;
        numbers[3] = 4;
        numbers[4] = 5;

        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
            System.out.println(names[i]);
        }
    }
}

