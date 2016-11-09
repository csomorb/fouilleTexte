/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package disco;

/**
 *
 */
public class Test {

    public int size;
    private int count;
    protected int age;
    final static int score = 5;

    public Test(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
         System.out.println("Début d'exécution...");

        Test obj1 = new Test(23);
        obj1.setAge(30);
        for (int i = 0; i < 10; i++) {
            System.out.println("Age=" + obj1.getAge());
        }
    }

}
